/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprintattendance.utils;

import com.digitalpersona.uareu.ReaderCollection;
import com.digitalpersona.uareu.UareUException;
import java.util.Vector;
import javafx.scene.control.Alert;

/**
 *
 * @author hp
 */
public class Selection {
    
    static Vector<String> refreshList(ReaderCollection m_collection){
       //acquire available readers
		try{
			m_collection.GetReaders();
		} 
		catch(UareUException e) { 
			             Alert alert = new Alert(Alert.AlertType.ERROR, "There was an unexpected error");
                               alert.showAndWait();
		} 
            
            Vector<String> strNames = new Vector<>();
            
            for(int i = 0; i < m_collection.size(); i++){
			strNames.add(m_collection.get(i).GetDescription().name);
		}
            
            return strNames;
       
    }
    
}
