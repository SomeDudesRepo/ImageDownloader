/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imagedownloader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import javax.imageio.ImageIO;

/**
 *
 * @author dans
 */
public class ImageFetchJobCreator {
    private PrintWriter out;
    
    class Parameters {
        public final Integer galleryStart;
        public final Integer galleryCount;
        public final Integer imagesPerGallery;
        public final String baseDirectory;
        public final String baseUrl;
        public final String person;
        public final String imageType;
        public final String logFile = "download.log";
        
        Parameters(String[] args) throws FileNotFoundException, IOException {
            if (args.length != 1) {
                System.out.println("Need 1 arg [path to properties file]");
                throw new IllegalArgumentException();
            }
            
            FileInputStream in = new FileInputStream(args[0]);
            Properties p = new Properties();
            p.load(in);
            galleryStart = Integer.parseInt(p.getProperty("galleryStart"));
            galleryCount = Integer.parseInt(p.getProperty("galleryCount"));
            imagesPerGallery = Integer.parseInt(p.getProperty("imagesPerGallery"));
            person = p.getProperty("person").toLowerCase().replace(' ', '-');
            baseDirectory = p.getProperty("baseDirectory") + p.getProperty("person");
            baseUrl = p.getProperty("baseUrl");
            imageType = p.getProperty("imageType");
         }
    }
    
    private void GetImage(URL url, String fullPath, String imageType) throws IOException {
        BufferedImage img = ImageIO.read(url);
        File outputfile = new File(fullPath);
        ImageIO.write(img, imageType, outputfile);
    }
    
    private String CreateDirectory(Parameters p, Integer i) {
        String thisDir = p.baseDirectory + "\\" + i.toString() + "\\";
        new File(thisDir).mkdirs();
        return thisDir;
    }

    private void LogDownload(URL url, String fullPath) {
        out.println("Downloaded: " + url.toString() + " to " + fullPath);
        System.out.println("Downloaded: " + url.toString() + " to " + fullPath);    
    }

    public void Run(String[] args) 
            throws MalformedURLException, 
                   IOException, 
                   IllegalArgumentException,
                   FileNotFoundException {
       Parameters p = new Parameters(args);
       out = new PrintWriter (p.baseDirectory + "\\" + p.logFile);
       for (Integer i = p.galleryStart; i <= p.galleryCount; ++i) {
           String galleryDir = CreateDirectory(p, i);
           for (Integer j = 1; j <= p.imagesPerGallery; ++j) {
               String fileName = p.person + "-" + j.toString() + "." + p.imageType;
               URL url = new URL(p.baseUrl + p.person + "/" + 
                                 i.toString() + "/" + fileName);
               String fullPath = galleryDir + fileName;
               GetImage(url, fullPath, p.imageType);
               LogDownload(url, fullPath);
           }
       }
    }
}
