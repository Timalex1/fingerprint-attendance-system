/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprintattendance.utils;

import fingerprintattendance.utils.Selection;
import com.digitalpersona.uareu.ReaderCollection;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;
import java.util.Vector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 *
 * @author hp
 */
public class ReaderUtils {
    
    /*Method definititons */
    public static void refreshList(ReaderCollection mCollection, ComboBox<?> list_id, Label lbl_device_found) {
        //acquire available readers
        try {
            mCollection = UareUGlobal.GetReaderCollection();

        } catch (UareUException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "There was an unexpected error");
            alert.showAndWait();
        }

        //list reader names
        Vector<String> strNames = Selection.refreshList(mCollection);

        ObservableList names = FXCollections.observableArrayList(strNames);

        int no_of_devices = names.size();

        list_id.setItems(names);
        list_id.getSelectionModel().selectFirst();

        lbl_device_found.setText(no_of_devices + " fingerprint scanner found");
    }
    
    public static void refreshListNoLabel(ReaderCollection mCollection, ComboBox<?> list_id) {
        //acquire available readers
        try {
            mCollection = UareUGlobal.GetReaderCollection();

        } catch (UareUException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "There was an unexpected error");
            alert.showAndWait();
        }

        //list reader names
        Vector<String> strNames = Selection.refreshList(mCollection);

        ObservableList names = FXCollections.observableArrayList(strNames);

        int no_of_devices = names.size();

        list_id.setItems(names);
        list_id.getSelectionModel().selectFirst();

    }
    
}
