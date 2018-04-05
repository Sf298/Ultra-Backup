
package ultra.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author Saud
 */
public class Backups {
    private ArrayList<BackupItem> backupList = new ArrayList<>();;
    
    public ArrayList<BackupItem> getBackupItems() {
        return backupList;
    }
    
    public void saveBackups() {
        try {
            Viewer.metaFile.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(Viewer.metaFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(backupList);
            oos.close();
            fos.close();
        } catch(IOException e) {
            e.printStackTrace(System.out);
        }
    }
    
    public void loadBackups() {
        if(Viewer.metaFile.exists()) {
            // load metadata
            try {
                FileInputStream fis = new FileInputStream(Viewer.metaFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                backupList = (ArrayList<BackupItem>) ois.readObject();
                ois.close();
                fis.close();
            } catch(IOException | ClassNotFoundException e) {
                e.getMessage();
            }
        }
    }
    
    public void newBackup(String name, String rootDir) {
        backupList.add(new BackupItem(name, new File(rootDir)));
    }
    
    public void removeBackup(int index) {
        backupList.remove(index);
    }
    
    public BackupItem getBackupItemAt(int i) {
        return backupList.get(i);
    }
}
