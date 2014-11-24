/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imagedownloader;

import java.io.IOException;

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
        imageFetcher.GetSequentialImages(
            1, 47,
            "http://www.alrincon.com/imagenesblog/mike-dowson-2/", 
            "C:\\Users\\dans\\Pictures\\New folder\\Dowson\\Blue Dress\\", 
            "jpg");
        /*
        try {
            imageFetcher.Run(args);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ImageFetchJobCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ImageFetchJobCreator.class.getName()).log(Level.SEVERE, null, ex);
        }  catch (FileNotFoundException ex) {
            Logger.getLogger(ImageFetchJobCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ImageDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }*/ 
    }  
}
