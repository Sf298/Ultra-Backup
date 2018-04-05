
package ultra.backup;

import java.io.File;
import javax.swing.JFrame;

/**
 *
 * @author Saud
 */
public class Viewer {
    public static String projectFolder;
    public static File backupsFile;
    public static File metaFile;
    public static Backups backups;
    
    public static void main(String[] args) {
        //projectFolder = System.getenv("ProgramFiles")+"\\Sf Apps\\Ultra Backup\\";
        projectFolder = "\\Sf Apps\\Ultra Backup\\";
        backupsFile = new File(projectFolder+"Files\\");
        metaFile = new File(projectFolder+"metadata.obj");
        
        backups = new Backups();
        backups.loadBackups();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            backups.saveBackups();
        }));
        
        MainFrame frame = new MainFrame();
         frame.setTitle("Ultra Backup");
         frame.setSize(1000, 700);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
    }
}

/*
store all files in 1 folder
xml to store backup name[prev location (string), curr file name (string), date modified (long), hashcode (Str), computer name (str)]
xml strct:
<Backups>
    <Backup backupName="" date="" computerName="" sourceLoc="">
        <File>
            <hash> n7480ct349n83t47n </hash>
            <prevLocation> C:/file.txt </prevLocation>
            <currLocation> /backup/fnisubrrfuns </currLocation>
            <dateMod> 56856516 </dateMod>
        </File>
    </Backup>
</Backups>

interface uses xml data
click backup name to list files for that backup
*/