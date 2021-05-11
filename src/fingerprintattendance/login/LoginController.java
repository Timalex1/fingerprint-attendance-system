package fingerprintattendance.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import fingerprintattendance.dao.AdminDAO;
import fingerprintattendance.utils.StageUtils;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class LoginController implements Initializable {

    @FXML
    private JFXPasswordField password_id;
    @FXML
    private JFXTextField email_id;
    @FXML
    private JFXButton login_id;

    boolean authenticated;

    private StageUtils sutils = new StageUtils();
    @FXML
    private JFXButton close;
    @FXML
    private JFXSpinner login_spin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //For dev-mode only----tiring to keep typing
        email_id.setText("admin@fuoye.edu.ng");
        password_id.setText("admin");

        login_spin.setVisible(false);
    }

    @FXML
    private void login(ActionEvent event) throws SQLException, ClassNotFoundException {

        String email = email_id.getText().trim();
        String password = password_id.getText().trim();

        boolean not_empty = !email.isEmpty() && !password.isEmpty();

        if (not_empty) {


            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    boolean authenticated = AdminDAO.login(email, password);
                    return authenticated;
                }
            };

            task.setOnRunning((e) -> {
                //Disable fields and buttons

                disableFields();
            });

            task.setOnSucceeded((e) -> {

                //enable fields after task succeeds
                enableFields();

                //...checks for true after authentication completed
                authenticated = task.getValue();

                if (authenticated) {

                    email_id.getStyleClass().remove("wrong-credentials");
                    password_id.getStyleClass().remove("wrong-credentials");

                    final String FXML = "/fingerprintattendance/admin/admin.fxml";
                    sutils.newStage(FXML, true, event);

                } else {

                    email_id.getStyleClass().add("wrong-credentials");
                    password_id.getStyleClass().add("wrong-credentials");

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Login Failed");
                    alert.setContentText("Wrong credentials, try again");
                    alert.showAndWait();
                }
            });

            new Thread(task).start();


        } else {
            email_id.getStyleClass().add("wrong-credentials");
            password_id.getStyleClass().add("wrong-credentials");
        }

    }

    @FXML
    private void email_click(KeyEvent event) {

        //remove red error from TextField
        email_id.getStyleClass().remove("wrong-credentials");
    }

    @FXML
    private void password_click(KeyEvent event) {

        //remove red error from PasswordField
        password_id.getStyleClass().remove("wrong-credentials");
    }

    @FXML
    private void close_stage(ActionEvent event) {

        //close and exit application
        sutils.closeStage(close);
    }

    //... method to disable button and fields
    private void disableFields() {

        //..start spinner
        login_spin.setVisible(true);

        login_id.setDisable(true);
        email_id.setEditable(false);
        password_id.setEditable(false);

    }

    //..method to enable button and fields back
    private void enableFields() {

        //...stop spinner
        login_spin.setVisible(false);

        login_id.setDisable(false);
        email_id.setEditable(true);
        password_id.setEditable(true);
    }

}
