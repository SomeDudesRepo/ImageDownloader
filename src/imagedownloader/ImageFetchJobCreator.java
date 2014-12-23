/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imagedownloader;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
        public final Integer start;
        public final Integer end;
        public final Integer imagesPerGallery;
        public final String baseDirectory;
        public final String baseUrl;
        public final String imageBaseName;
        public final String imageType;
        public final String logFile = "download.log";
        
        private void CreateBaseDir() {
            File f = new File(this.baseDirectory);
            if (!f.exists()) {
                f.mkdirs();
            }
        }
        
        Parameters(String[] args) throws FileNotFoundException, IOException {
            if (args.length != 1) {
                System.out.println("Need 1 arg [path to properties file]");
                throw new IllegalArgumentException();
            }
            
            FileInputStream in = new FileInputStream(args[0]);
            Properties p = new Properties();
            p.load(in);
            start = Integer.parseInt(p.getProperty("galleryStart"));
            end = Integer.parseInt(p.getProperty("galleryCount"));
            imagesPerGallery = Integer.parseInt(p.getProperty("imagesPerGallery"));
            imageBaseName = p.getProperty("imageBaseName");
            baseDirectory = p.getProperty("baseDirectory");
            baseUrl = p.getProperty("baseUrl");
            imageType = p.getProperty("imageType");
            
            CreateBaseDir();
        }
        
        Parameters(Integer start,
                   Integer end,
                   Integer imagesPerGallery,
                   String baseDirectory,
                   String baseUrl,
                   String imageBaseName,
                   String imageType) {
            this.start = start;
            this.end = end;
            this.imagesPerGallery = imagesPerGallery;
            this.imageBaseName = imageBaseName;
            this.baseDirectory = baseDirectory;
            this.baseUrl = baseUrl;
            this.imageType = imageType;
        }
           
    }
    
    private void GetImage(URL url, String fullPath, String imageType) 
            throws IOException {
        BufferedImage img = ImageIO.read(url);
        File outputfile = new File(fullPath);
        ImageIO.write(img, imageType, outputfile);
    }
    
    private String CreateDirectory(Parameters p, Integer i, boolean test) {
        String thisDir = p.baseDirectory + "\\" + i.toString() + "\\";
        if (!test) {
            new File(thisDir).mkdirs();
        }
        return thisDir;
    }

    private void LogDownload(URL url, String fullPath, boolean test) {
        if (!test) {
            out.println("Downloaded: " + url.toString() + " to " + fullPath);
            out.flush();
        }
        System.out.println("Downloaded: " + url.toString() + " to " + fullPath);    
    }
    
    private void GetWithParameters(Parameters p, boolean test)
            throws MalformedURLException, 
                   IOException, 
                   IllegalArgumentException,
                   FileNotFoundException,
                   InterruptedException
    {
        if (!test) {
            out = new PrintWriter (p.baseDirectory + "\\" + p.logFile);
        }
        for (Integer i = p.start; i <= p.end; ++i) {
            String galleryDir = CreateDirectory(p, i, test);
            for (Integer j = 1; j <= p.imagesPerGallery; ++j) {
                String fileName = p.imageBaseName + "-" + j.toString() + "." + 
                                  p.imageType;
                URL url = new URL(p.baseUrl + p.imageBaseName + "/" + 
                                  i.toString() + "/" + fileName);
                String fullPath = galleryDir + fileName;
                if (!test) {
                    GetImage(url, fullPath, p.imageType);
               }
               LogDownload(url, fullPath, test);
             }
            System.out.println("-----------------------------------------");
            Thread.sleep(1000);
        }
        if (!test) {
            out.close();
        }    
    }

    public void GetGalleriesFromFile(int imagesPerGallery, 
                                     String jpg,
                                     String baseUrl,
                                     String file,
                                     boolean test) 
           throws MalformedURLException, 
                   IOException, 
                   IllegalArgumentException,
                   FileNotFoundException,
                   InterruptedException {
        File theFile = new File(file);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            String[] split = line.split(",");
            if (split.length != 3) {
                continue;
            }
            String[] urlSplit = split[0].split("/");
            for (int n = 0; n != urlSplit.length; ++n) {
                System.out.print(urlSplit[n] + " - ");
            }
            String imageName = urlSplit[urlSplit.length - 1];
            String dirName = imageName.replace('-', ' ');
            int space = dirName.indexOf(' ');
            StringBuilder sb = new StringBuilder(dirName);
            sb.setCharAt(0, Character.toUpperCase(dirName.charAt(0)));
            sb.setCharAt(space + 1, 
                         Character.toUpperCase(dirName.charAt(space + 1)));
            dirName = sb.toString();
            System.out.print(imageName + " - " + dirName + " - " + 
                             theFile.getParentFile().getAbsolutePath());
            System.out.println("");
            Integer start = new Integer(Integer.valueOf(split[1]));
            Integer end = new Integer(Integer.valueOf(split[2]));
            Parameters p = 
                new Parameters(start, end, imagesPerGallery, 
                               theFile.getParentFile().getAbsolutePath() + "\\" + dirName, 
                               baseUrl, imageName, jpg);
            GetWithParameters(p, test);
        }
        br.close();        
    }
    
    public void GetSequentialImages(int start, 
                                    int end, 
                                    String baseUrl,
                                    String directory,
                                    String imageType) 
            throws MalformedURLException, IOException {
        new File(directory).mkdirs();
        for (int i = start; i <= end; ++i) {
            String imageName = String.valueOf(i) + "." + imageType;
            GetImage(new URL(baseUrl + imageName),
                     directory + imageName,
                     imageType);
        }
    }
          
    public void GetAllGalleries(String[] args) 
            throws MalformedURLException, 
                   IOException, 
                   IllegalArgumentException,
                   FileNotFoundException,
                   InterruptedException {
        Parameters p = new Parameters(args);
        GetWithParameters(p, false);
    }
}
