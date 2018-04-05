
package ultra.backup;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * goes through and makes a file list
 * @author Saud
 */
public class AnalizingThread implements Runnable, Serializable {
    
    File rootDir;
    
    private ArrayList<FileItem> fileList;
    private static LinkedList<FileItem> toHashList;
    private static Thread hasherThread;
    private long listedCount;
    
    
    public AnalizingThread(File rootDir, ArrayList<FileItem> fileList) {
        this.rootDir = rootDir;
        this.fileList = fileList;
    }
    
    public long getCount() {
        return listedCount;
    }
    
    @Override
    public void run() {
        toHashList = new LinkedList<>();
        hasherThread = new Thread(new HashingThread(toHashList));
        hasherThread.start();
        
        FileSystemIterator iter = new FileSystemIterator(rootDir.getAbsolutePath());
        
        // add files to file list
        while(iter.hasNext()) {
            FileItem fi = new FileItem(iter.getFile());;
            listedCount++;
            fileList.add(fi);
            toHashList.add(fi);
            //System.out.println(iter.getFile()+"     "+iter.getFile().length()+"     "+Arrays.toString(fileList.get(fileList.size()-1).getHashCode()));
            //System.out.println(fi.getFile() +" "+ listedCount +" "+ iter.hasNext())
            iter.next();
        }
        System.out.println("done scan");
        hasherThread.interrupt();
        
        try {
            hasherThread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(BackupItem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
