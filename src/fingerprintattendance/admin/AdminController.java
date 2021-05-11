/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprintattendance.admin;

import com.digitalpersona.uareu.Engine;
import com.digitalpersona.uareu.Fid;
import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.ReaderCollection;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import fingerprintattendance.attendance.AttendanceController;
import fingerprintattendance.utils.ReaderUtils;
import fingerprintattendance.capture.CaptureThread;
import fingerprintattendance.dao.AttendanceDAO;
import fingerprintattendance.dao.CourseDAO;
import fingerprintattendance.dao.StudentDAO;
import fingerprintattendance.model.Course;
import fingerprintattendance.model.Student;
import fingerprintattendance.utils.StageUtils;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class AdminController implements Initializable {

    @FXML
    private JFXComboBox<String> device_list;

    private ReaderCollection mCollection;
    @FXML
    private JFXButton add_course_id;
    @FXML
    private JFXButton add_student_id;
    @FXML
    private JFXButton view_att_id;
    @FXML
    private JFXButton take_att_id;

    @FXML
    private Pane pane_id;
    @FXML
    private Pane pane2_id;
    @FXML
    private Pane pane3_id;
    @FXML
    private Pane pane4_id;
    @FXML
    private JFXTextField course_title_id;
    @FXML
    private JFXTextField course_code_id;
    @FXML
    private JFXTextField course_teacher_id;
    @FXML
    private JFXButton submit_course_id;
    private JFXButton exit_btn;

    private StageUtils sutils = new StageUtils();
    @FXML
    private JFXTextField stu_name_id;
    @FXML
    private JFXTextField matric_no_id;
    @FXML
    private JFXComboBox<String> level_id;
    @FXML
    private JFXButton submit_student_id;
    @FXML
    private JFXButton capture_id;

    private byte[] image_byte;
    
    private Fid fid = null; 
    @FXML
    private JFXComboBox<?> courseList;
    @FXML
    private JFXTextField studentName;
    @FXML
    private JFXTextField studentMatricNo;

    @FXML
    private ImageView fingerprint_id;
    @FXML
    private JFXSpinner spinnerId1;
    @FXML
    private JFXSpinner spinnerId;
    @FXML
    private ImageView fingerprint_attendance;
    @FXML
    private JFXButton capture_attendance_id;

    //Fingerprint variables
    List<Fmd> m_fmdList = new ArrayList<>();
    Fmd[] m_fmdArray = null;

    Fmd[] m_fmds = null;

    Fid image;

    //Students from database
    List<Student> students;
    @FXML
    private JFXComboBox<String> courseListView;
    @FXML
    private JFXDatePicker dateOfAttendance;
    @FXML
    private JFXButton searchAttendance;
    @FXML
    private JFXListView<String> studentDisplay;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Set spinner to be invisible
        spinnerId.setVisible(false);
        spinnerId1.setVisible(false);

        // TODO
        ReaderUtils.refreshListNoLabel(mCollection, device_list);

        ObservableList level_list = FXCollections.observableArrayList("100L", "200L", "300L", "400L", "500L");

        level_id.setItems(level_list);
        level_id.getSelectionModel().selectFirst();

        //Load courses on startup
        loadCourses(courseList);

    }

    @FXML
    private void handleScene(ActionEvent event) {

        if (event.getSource() == add_course_id) {
            pane_id.toFront();
        } else if (event.getSource() == add_student_id) {
            pane2_id.toFront();
        } else if (event.getSource() == take_att_id) {
            //Load courses on startup
            loadCourses(courseListView);
            pane3_id.toFront();
        } else if (event.getSource() == view_att_id) {
            pane4_id.toFront();
        }

//
    }


    private void exit_system(ActionEvent event) {
        sutils.closeStage(exit_btn);
    }

    @FXML
    private void submit_student(ActionEvent event) throws SQLException, ClassNotFoundException {

        //Collect field values with .getText() functions
        String name = stu_name_id.getText().trim();
        String matric_no = matric_no_id.getText().trim();
        String level = level_id.getValue();

        /**
         * Validations on fields collected Check if field values are not empty
         *
         */
        boolean not_empty = (!name.isEmpty() && !matric_no.isEmpty() && !level.isEmpty() && image_byte != null) ? true : false;

        
        //Check if Fingerprint device is connected
        if (device_list.getItems().size() != 0 && device_list.getItems() != null) {
            if (not_empty) {


                //Make the Course(code and title) uppercase before storing in DB
                name = name.toUpperCase();
                matric_no = matric_no.toUpperCase();

                boolean result = StudentDAO.insertStudent(name, matric_no, level, image_byte);


                if (result) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Student added successfully");
                    alert.showAndWait();

                    stu_name_id.setText("");
                    matric_no_id.setText("");

                    //invalidate the byte[] image value
                    this.image_byte = null;
                }


            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "One or more fields has an error");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please, connect a fingerprint device to continue");
            alert.showAndWait();
        }

        //refresh fingerprint scanner devices and update
        ReaderUtils.refreshListNoLabel(mCollection, device_list);

    }

    @FXML
    private void capture(ActionEvent event) throws UareUException {

        capture_id.setDisable(true);

//        CaptureThread ct = new CaptureThread();
        Task<byte []> task = new Task<byte []>() {
                @Override
            protected byte[] call() throws Exception {

                    byte[] imageByte = null;
                    imageByte = CaptureThread.capture_event(fingerprint_id, image_byte);

                    return imageByte;
                }
            };

        task.setOnRunning((e) -> {
            spinnerId.setVisible(true);

            }
        );

        task.setOnSucceeded((e) -> {

            spinnerId.setVisible(false);

            capture_id.setDisable(false);

            this.image_byte = task.getValue();
            });

        new Thread(task).start();
    }

    private void back_to_prev(ActionEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
        final String FXML = "/fingerprintattendance/HomeDocument.fxml";
        sutils.newStage(FXML, true, event);
    }

    @FXML
    private void submitCourse(ActionEvent event) throws SQLException, ClassNotFoundException {
        //Collect field values with .getText() functions

        String title = course_title_id.getText();
        String code = course_code_id.getText();
        String teacher = course_teacher_id.getText();

        /**
         * Validations on fields collected Check if field values are not empty
         *
         */
        boolean not_empty = (!title.isEmpty() && !code.isEmpty() && !teacher.isEmpty()) ? true : false;

        if (not_empty) {

            //Make the Course(code and title) uppercase before storing in DB
            title = title.toUpperCase();
            code = code.toUpperCase();

            boolean result = CourseDAO.insertCourse(title, code, teacher);

            if (result) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Course successfully added");
                alert.showAndWait();

                course_code_id.setText("");
                course_title_id.setText("");
                course_teacher_id.setText("");

            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "One or more fields has an error");
            alert.showAndWait();
        }
    }

    @FXML
    private void capture_attendance(ActionEvent event) throws SQLException, ClassNotFoundException {

        if (courseList.getSelectionModel().getSelectedItem() != null) {

            capture_attendance_id.setDisable(true);

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
                    Fid image = CaptureThread.capture_event_2(fingerprint_attendance, image_byte);
                    return image;
                }
            };

            task.setOnRunning((e) -> {
                spinnerId1.setVisible(true);
            });

            task.setOnSucceeded((e) -> {

                capture_attendance_id.setDisable(false);

                spinnerId1.setVisible(false);

                this.image = task.getValue();

                if (this.image != null) {
                    try {
                        //Call process captured fingerprint result
                        if (ProcessCaptureResult()) {
                            String courseSelected = courseList.getSelectionModel().getSelectedItem().toString();

                            //Assert student has not taken attendance already -- the same day, the same course
                            if (!assertStudentAttendance(courseSelected)) {
                                //Mark Student attendance as present
                                if (takeAttendance(courseSelected)) {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setHeaderText("Attendance");
                                    alert.setTitle("Student Attendance");
                                    alert.setContentText(studentName.getText() + " is marked present.");
                                    alert.show();

                                    //Clear the fields after student attendance is marked
//                                clearFieldsAfterAttendanceMarked();
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setHeaderText("Attendance");
                                    alert.setTitle("Student Attendance");
                                    alert.setContentText("Something went wrong, Try again!");
                                    alert.show();
                                }
                            } else {

                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText("Attendance");
                                alert.setTitle("Student Attendance");
                                alert.setContentText(studentName.getText() + " marked present already.");
                                alert.show();
                            }

                        }


                    } catch (UareUException ex) {
                        Logger.getLogger(AttendanceController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
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

    private void loadCourses(JFXComboBox courseList) {

        try {

            //DB Operation - - get all courses from the DB
            ObservableList<Course> courses = CourseDAO.getAllCourses();

            ObservableList course_titles = FXCollections.observableArrayList();

            courses.forEach(course -> {
                course_titles.add(course.course_title_get());
            });

            courseList.setItems(course_titles);

            //initialize fmds[]
            m_fmds = new Fmd[2];

        } catch (SQLException ex) {
            Logger.getLogger(AttendanceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AttendanceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Process Captured fingerprint
    private boolean ProcessCaptureResult() throws UareUException {

        if (this.students.size() != 0) {

            Engine engine = UareUGlobal.GetEngine();

            Fmd fmd;

            fmd = engine.CreateFmd(image, Fmd.Format.DP_VER_FEATURES);

            this.m_fmds[0] = fmd;

            int target_falsematch_rate = Engine.PROBABILITY_ONE / 100000; // target rate is  0.00001

            Engine.Candidate[] matches = engine.Identify(m_fmds[0], 0, m_fmdArray, target_falsematch_rate, 1);

            if (matches.length == 1) {
                Student student = this.students.get(matches[0].fmd_index);
                studentMatricNo.setText(student.getMatric_no());
                studentName.setText(student.getName());

                return true;
            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setTitle("Student not found");
                alert.setContentText("No student found with that fingerprint");
                alert.show();
                return false;
            }
        }

        return false;
    }

    private boolean takeAttendance(String course) throws SQLException, ClassNotFoundException {

        String courseSelected = course;

        String date = getCurrentDate();

        String studentName = this.studentName.getText();

        boolean added = AttendanceDAO.takeAttendance(courseSelected, studentName, date);

        if (added) {
            return true;
        }
        return false;
    }

    private void clearFieldsAfterAttendanceMarked() {
        studentMatricNo.setText(null);
        studentName.setText(null);
    }

    private boolean assertStudentAttendance(String course) throws ClassNotFoundException, SQLException {

        return AttendanceDAO.checkStudentAttended(course, studentName.getText(), getCurrentDate());
    }

    private String getCurrentDate() {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(new Date());
    }

    @FXML
    private void searchStudentAttendance(ActionEvent event) throws SQLException, ClassNotFoundException {

        String course = courseListView.getSelectionModel().getSelectedItem();

        LocalDate localDate = dateOfAttendance.getValue();

        String pattern = "yyyy-MM-dd";
        DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern(pattern);

        String dateSelected = localDate.format(simpleDateFormat);

        ObservableList<String> students = AttendanceDAO.getAllAttendanceByCourseAndDate(course, dateSelected);

        studentDisplay.setItems(students);
    }
}
