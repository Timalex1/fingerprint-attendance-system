/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprintattendance.attendance;

import com.digitalpersona.uareu.Engine;
import com.digitalpersona.uareu.Engine.Candidate;
import com.digitalpersona.uareu.Fid;
import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import fingerprintattendance.capture.CaptureThread;
import fingerprintattendance.dao.CourseDAO;
import fingerprintattendance.dao.StudentDAO;
import fingerprintattendance.model.Course;
import fingerprintattendance.model.Student;
import fingerprintattendance.utils.StageUtils;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class AttendanceController implements Initializable {

    @FXML
    private JFXButton scan_finger_id;
    @FXML
    private ImageView fingerprint_id;
    @FXML
    private JFXComboBox<?> class_list;
    @FXML
    private JFXDatePicker date_id;

    //stage utils initializer
    private StageUtils sutils = new StageUtils();

    //Declare byte image variable
    byte[] image_byte;

    //Fingerprint variables
    List<Fmd> m_fmdList = new ArrayList<>();
    Fmd[] m_fmdArray = null;

    Fmd[] m_fmds = null;

    Fid image;

    //Students from database
    List<Student> students;
    @FXML
    private JFXSpinner spinner_id;
    @FXML
    private JFXTextField student_mat_id;
    @FXML
    private JFXTextField student_name_id;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Set spinner to be invisible
        spinner_id.setVisible(false);

        try {

            //DB Operation - - get all courses from the DB
            ObservableList<Course> courses = CourseDAO.getAllCourses();

            ObservableList course_titles = FXCollections.observableArrayList();

            courses.forEach(course -> {
                course_titles.add(course.course_title_get());
            });

            class_list.setItems(course_titles);

            //initialize fmds[]
            m_fmds = new Fmd[2];

        } catch (SQLException ex) {
            Logger.getLogger(AttendanceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AttendanceController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void scan_finger(ActionEvent event) throws SQLException, ClassNotFoundException, UareUException, InterruptedException, ExecutionException {

        if (class_list.getSelectionModel().getSelectedItem() != null) {

            students = StudentDAO.getAllStudents();


            students.forEach(e -> {

                Fmd fmd = null;
                try {
                    fmd = UareUGlobal.GetImporter().ImportFmd(e.getImage_byte(), Fmd.Format.DP_REG_FEATURES, Fmd.Format.DP_REG_FEATURES);
                } catch (UareUException ex) {
                    Logger.getLogger(AttendanceController.class.getName()).log(Level.SEVERE, null, ex);
                }
                m_fmdList.add(fmd);
            });

            m_fmdArray = new Fmd[this.m_fmdList.size()];
            this.m_fmdList.toArray(m_fmdArray);

            Task<Fid> task = new Task<Fid>() {
                @Override
                protected Fid call() throws Exception {
                    Fid image = CaptureThread.capture_event_2(fingerprint_id, image_byte);
                    return image;
                }
            };

            task.setOnRunning((e) -> {
                this.loadingSpinner();
            });

            task.setOnSucceeded((e) -> {
                this.stopSpinner();
                this.image = task.getValue();

                if (this.image != null) {
                    try {
                        ProcessCaptureResult();
                    } catch (UareUException ex) {
                        Logger.getLogger(AttendanceController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            });

            new Thread(task).start();


        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setTitle("No Selection");
            alert.setContentText("Please, select a course to continue");
            alert.show();
        }
    }

    @FXML
    private void back_to_prev(ActionEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
        final String FXML = "/fingerprintattendance/HomeDocument.fxml";
        sutils.newStage(FXML, true, event);

    }

    private void ProcessCaptureResult() throws UareUException {

        if (this.students.size() != 0) {

            Engine engine = UareUGlobal.GetEngine();

            Fmd fmd;

            fmd = engine.CreateFmd(image, Fmd.Format.DP_VER_FEATURES);


            this.m_fmds[0] = fmd;

            int target_falsematch_rate = Engine.PROBABILITY_ONE / 100000; // target rate is  0.00001

            Candidate[] matches = engine.Identify(m_fmds[0], 0, m_fmdArray, target_falsematch_rate, 1);

            if (matches.length == 1) {
                Student student = this.students.get(matches[0].fmd_index);
                student_mat_id.setText(student.getMatric_no());
                student_name_id.setText(student.getName());

                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setContentText("Match found");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Match not found");
                alert.showAndWait();

            }
        }

    }

    public void loadingSpinner() {
        spinner_id.setVisible(true);
    }

    public void stopSpinner() {
        spinner_id.setVisible(false);
    }

}
