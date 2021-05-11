/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprintattendance.capture;

import com.digitalpersona.uareu.Engine;
import com.digitalpersona.uareu.Fid;
import com.digitalpersona.uareu.Fid.Format;
import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;
import fingerprintattendance.admin.AdminController;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author hp
 */
public class CaptureThread {
   
    Fid image;

    public static byte [] capture_event(ImageView image_view, byte[] image_byte) {

        Reader reader;

        try {

            reader = UareUGlobal.GetReaderCollection().get(0);
            reader.Open(Reader.Priority.COOPERATIVE);
            
                    Reader.CaptureResult xr = reader.Capture(Format.ISO_19794_4_2005, Reader.ImageProcessing.IMG_PROC_DEFAULT, 500, -1);
                    BufferedImage m_image;
                    Fid image = xr.image;

                    Fid.Fiv view = image.getViews()[0];
                    m_image = new BufferedImage(view.getWidth(), view.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                    m_image.getRaster().setDataElements(0, 0, view.getWidth(), view.getHeight(), view.getImageData());

                    Image real_image = SwingFXUtils.toFXImage(m_image, null);

            image_view.setImage(real_image);

            Engine engine = UareUGlobal.GetEngine();

            Engine.EnrollmentCallback ec = new Engine.EnrollmentCallback() {
                @Override
                public Engine.PreEnrollmentFmd GetFmd(Fmd.Format format) {

                    Fmd fmd = null;
                    try {
                        fmd = engine.CreateFmd(image, Fmd.Format.DP_PRE_REG_FEATURES);
                    } catch (UareUException ex) {
                        Logger.getLogger(CaptureThread.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    Engine.PreEnrollmentFmd prefmd = new Engine.PreEnrollmentFmd();
                    prefmd.fmd = fmd;

                    prefmd.view_index = 1;
                    return prefmd;
                }
            };



            Fmd fmd = engine.CreateEnrollmentFmd(Fmd.Format.DP_REG_FEATURES, ec);

            image_byte = fmd.getData();

            reader.Close();

            System.out.println(image_byte.length);

            return image_byte;
        } catch (UareUException ex) {
            Logger.getLogger(AdminController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }
    
    public static Fid capture_event_2(ImageView image_view, byte[] image_byte) {

        Reader reader;

        try {

            reader = UareUGlobal.GetReaderCollection().get(0);
            reader.Open(Reader.Priority.EXCLUSIVE);
//
                    Reader.CaptureResult xr = reader.Capture(Format.ISO_19794_4_2005, Reader.ImageProcessing.IMG_PROC_DEFAULT, 500, -1);
                    BufferedImage m_image;
                    Fid image = xr.image;

                    Fid.Fiv view = image.getViews()[0];
                    m_image = new BufferedImage(view.getWidth(), view.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                    m_image.getRaster().setDataElements(0, 0, view.getWidth(), view.getHeight(), view.getImageData());

                    Image real_image = SwingFXUtils.toFXImage(m_image, null);

                    image_view.setImage(real_image);

//            
            image_byte = image.getData();
            

            reader.Close();

            return image;
        } catch (UareUException ex) {
            Logger.getLogger(AdminController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }


}
