
package ultra.backup;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 *
 * @author Saud
 */
public class CopyingThread implements Runnable, Serializable {
    
    private ArrayList<FileItem> fileList;
    private int current;
    private String currentPath;
    private AnalizingThread scanThread;
    
    public CopyingThread(ArrayList<FileItem> fileList, AnalizingThread scanThread) {
        this.fileList = fileList;
        this.scanThread = scanThread;
    }
    
    public int getCount() {
        return current;
    }
    
    public String getFilePath() {
        return currentPath;
    }

    @Override
    public void run() {
        current = 0;
        double max = Double.POSITIVE_INFINITY;
        while(current < max) {
            if(Thread.currentThread().isInterrupted()) max = scanThread.getCount();
            try {
                if(fileList.size() > current) {
                    FileItem fi = fileList.get(current);
                    currentPath = fi.getFile().getPath();
//                    System.out.println("got file");
                    if(fi.getHashCode() != null) {
                        if(fi.getHashCode().length() == 0) {
                            current++;
                            continue;
                        }
                        
//                        System.out.println("got hash");
                        File backupFile = new File(Viewer.backupsFile +"/"+ fi.getHashCode());
                        
                        if(!backupFile.exists()) {
//                            System.out.println("copying");
                            backupFile.getParentFile().mkdirs();
                            Files.copy(fi.getFile().toPath(), backupFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
                        }
                        current++;
                    }
                } else {
                    Thread.sleep(50);
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
//            System.out.println(current+" / "+max);
        }
//            System.out.println("quitting");
    }
}
