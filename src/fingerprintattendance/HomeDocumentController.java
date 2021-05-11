/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprintattendance;

import fingerprintattendance.utils.ReaderUtils;
import com.digitalpersona.uareu.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import fingerprintattendance.utils.DBUtils.DbModel;
import fingerprintattendance.utils.StageUtils;
//import fingerprintattendance.capture.CaptureFXMLController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author hp
 */
public class HomeDocumentController implements Initializable {

    private DbModel model = DbModel.getInstance();

    private ReaderCollection mCollection;
    private Reader mReader;

    @FXML
    private VBox wrapper;
    @FXML
    private JFXButton refresh_list;
    @FXML
    private JFXButton admin_side;
    @FXML
    private JFXButton attendance;
    @FXML
    private ComboBox<String> list_id;
    @FXML
    private Label lbl_device_found;
    @FXML
    private JFXButton exit_btn;

    private StageUtils sutils = new StageUtils();

    private void handleButtonAction(ActionEvent event) {

        System.out.println("You clicked me!");

        ObservableList<String> cars = FXCollections.observableArrayList("Ford", "Toyota", "Kai");

        cars.forEach(e -> {
            System.out.println(e);
        });

        if (model.isDatabaseConnected()) {
            System.out.println("Connected");
        } else {
            System.out.println("Not connected");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ReaderUtils.refreshList(mCollection, list_id, lbl_device_found);
    }

    private void exit_stage(ActionEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    private void open_admin_panel(ActionEvent event) {
        final String FXML = "/fingerprintattendance/admin/admin1.fxml";
        sutils.newStage(FXML, true, event);
    }

    @FXML
    private void take_attendance(ActionEvent event) {
        final String FXML = "/fingerprintattendance/attendance/attendance.fxml";
        sutils.newStage(FXML, true, event);
    }

    @FXML
    private void refresh_device(ActionEvent event) {
        ReaderUtils.refreshList(mCollection, list_id, lbl_device_found);
    }

    @FXML
    private void exit_system(ActionEvent event) {
        sutils.closeStage(exit_btn);
    }
}
