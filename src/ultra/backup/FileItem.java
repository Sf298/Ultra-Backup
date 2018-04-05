
package ultra.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Saud
 */
public class FileItem implements Serializable {
    private String hashCode;
    private File originalLocation;

    /**
     * @return the hashCode
     */
    public String getHashCode() {
        return hashCode;
    }
    /**
     * @param code
     */
    public void setHashCode(String code) {
        hashCode = code;
    }
    /**
     * @return the originalLocation
     */
    public File getFile() {
        return originalLocation;
    }
    /**
     * @param originalLocation the originalLocation to set
     */
    public void setFile(File originalLocation) {
        this.originalLocation = originalLocation;
    }
    
    public FileItem(File file) {
        this.originalLocation = file;
    }
    
    public FileItem clone(){  
        try{  
            return (FileItem) super.clone();  
        }catch(Exception e){ 
            return null; 
        }
    }
    
    /*public static byte[] getHashCodeFromFile(File file) {
        if(file.length() == 0) return new byte[] {0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,0};
        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] dataBytes = new byte[(int)Math.min(524288, file.length())];
            int nread = 0;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            byte[] mdbytes = md.digest();
            fis.close();
            return mdbytes;
        } catch (NoSuchAlgorithmException | IOException ex) {}
        return null;
        
    }*/
    
    public static String getHashCodeFromFile(File file) {
        if(file.isDirectory() || file.length() == 0) return "";
        byte[] arr;
        FileInputStream fis;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] dataBytes = new byte[(int)Math.min(524288, file.length())];
            int nread = 0;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            byte[] mdbytes = md.digest();
            fis.close();
            arr = mdbytes;
            
            String result = "";

            for (int i=0; i < arr.length; i++) {
                result += Integer.toString( ( arr[i] & 0xff ) + 0x100, 16).substring( 1 );
            }
            return result;

        } catch (NoSuchAlgorithmException | IOException ex) {}
        return null;
        
    }
}
