/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprintattendance.utils;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author hp
 */
public class StageUtils {
    
     public void newStage(String fxml, boolean closeParent, ActionEvent event) {
//       to open a new stage
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource(fxml).openStream());
//                 CaptureFXMLController userCon = (CaptureFXMLController)loader.getController();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/fingerprintattendance/admin/admin.css").toExternalForm());

            stage.setScene(scene);
            stage.setResizable(true);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();

//            if closeParent == true, the parent stage is closed
            if (closeParent) {
                ((Node) event.getSource()).getScene().getWindow().hide();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
     
     public void closeStage(Node exit_btn){
         //get current stage
        Stage stage = (Stage) exit_btn.getScene().getWindow();
   
        //close the stage
        stage.close();
     }
}
