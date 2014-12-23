/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imagedownloader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dans
 */
public class ImageDownloader {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        ImageFetchJobCreator imageFetcher = new ImageFetchJobCreator();
        try {
//            imageFetcher.GetAllGalleries(args);
            imageFetcher.GetGalleriesFromFile(
                12, 
                "jpg",
                "http://www.japanesebeauties.net/japanese/",
                "C:\\Users\\dans\\Pictures\\New folder\\Asia\\jb.txt",
                true);
//            imageFetcher.GetSequentialImages(
//                1, 47,
//                "http://www.alrincon.com/imagenesblog/mike-dowson-2/", 
//                "C:\\Users\\dans\\Pictures\\New folder\\Dowson\\Blue Dress\\", 
//                "jpg");
        } catch (MalformedURLException ex) {
            Logger.getLogger(ImageFetchJobCreator.class.getName()).log(
                Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ImageFetchJobCreator.class.getName()).log(
                Level.SEVERE, null, ex);
        }  catch (FileNotFoundException ex) {
            Logger.getLogger(ImageFetchJobCreator.class.getName()).log(
                Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ImageDownloader.class.getName()).log(
                Level.SEVERE, null, ex);
        } 
    }  
}
