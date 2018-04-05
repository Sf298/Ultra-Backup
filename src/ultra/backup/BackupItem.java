
package ultra.backup;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Saud
 */
public class BackupItem implements Serializable {
    private String name;
    private File rootDir;
    private long date;
    private String computerName;
    private ArrayList<FileItem> fileList;

    /**
     * @return the name
     */
    public int getSize() {
        if(fileList == null) return 0;
        return fileList.size();
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the rootDir
     */
    public File getRooDirectory() {
        return rootDir;
    }
    /**
     * @param rootDir the rootDir to set
     */
    public void setRooDirectory(File rootDir) {
        this.rootDir = rootDir;
    }
    /**
     * @return the date
     */
    public long getDate() {
        return date;
    }
    /**
     * @param date the date to set
     */
    public void setDate(long date) {
        this.date = date;
    }
    /**
     * @return the computerName
     */
    public String getComputerName() {
        return computerName;
    }
    /**
     * @param computerName the computerName to set
     */
    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }
    /**
     * @return the fileList
     */
    public ArrayList<FileItem> getFileList() {
        return fileList;
    }
    /**
     * @param fileList the fileList to set
     */
    public void setFileList(ArrayList<FileItem> fileList) {
        this.fileList = fileList;
    }

    public BackupItem(String name, File sourceDir) {
        this.name = name;
        this.rootDir = sourceDir;
        this.date = 0;
        try {
            this.computerName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {}
    }
    
    private AnalizingThread scaningRun;
    private CopyingThread copyingRun;
    public void backup() {
        JFrame backupFrame = new JFrame();
        backupFrame.setSize(750, 350);
         populateBackupFrame(backupFrame);
         backupFrame.setVisible(true);
         
         fileList = new ArrayList<>();
        
        // start analizing thread
        scaningRun = new AnalizingThread(rootDir, fileList);
        Thread t2 = new Thread(scaningRun);
        t2.start();
        
        //start copying thread
        copyingRun = new CopyingThread(fileList, scaningRun);
        Thread t3 = new Thread(copyingRun);
        t3.start();
        
        // display updater
        Timer t = new Timer(50, (ActionEvent e) -> {
            updateBackupFrame();
            MainFrame.updateInfoPanel();
        });
        t.start();
        
        try{ t2.join(); } catch(Exception e) {}
        t3.interrupt();
        try{ t3.join(); } catch(Exception e) {}
    }
    private JLabel copyingLabel;
    private JLabel progressLabel;
    private void populateBackupFrame(JFrame frame) {
        JPanel infoPanel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
         c.gridheight = 1;
         c.gridwidth = 1;
         c.anchor = GridBagConstraints.LINE_START;
         c.fill = GridBagConstraints.HORIZONTAL;
         c.ipadx = 10;

            JLabel label = new JLabel("Name");
            c.gridx = 0;
            c.gridy = 0;
            infoPanel.add(label, c);

            label = new JLabel("Root Dir");
            c.gridy = 1;
            infoPanel.add(label, c);
            
            label = new JLabel("Copying");
            c.gridy = 2;
            infoPanel.add(label, c);

            label = new JLabel("Progress");
            c.gridy = 3;
            infoPanel.add(label, c);
            

            JLabel nameLabel = new JLabel(name);
            c.gridx = 1;
            c.gridy = 0;
            infoPanel.add(nameLabel, c);

            JLabel rootDirLabel = new JLabel("Root Dir");
            c.gridy = 1;
            infoPanel.add(rootDirLabel, c);

            copyingLabel = new JLabel();
            c.gridy = 2;
            infoPanel.add(copyingLabel, c);

            progressLabel = new JLabel();
            c.gridy = 3;
            infoPanel.add(progressLabel, c);
            
            
            label = new JLabel("");
            c.weightx = 1;
            c.weighty = 1;
            c.gridx = 2;
            c.gridy = 4;
            infoPanel.add(label, c);

        frame.add(infoPanel);
    }
    private void updateBackupFrame() {
        try {
            progressLabel.setText(copyingRun.getCount() +" / "+ scaningRun.getCount() );
            copyingLabel.setText(copyingRun.getFilePath() + " " + new File(copyingRun.getFilePath()).length());
        } catch(Exception e) {}
    }
    
    public void restoreBackup() {
        for(FileItem fi : fileList) {
            File backupFile = new File(Viewer.backupsFile + new String(fi.getHashCode()));
            if(!backupFile.exists()) {
                try {
                    Files.copy(fi.getFile().toPath(), backupFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {}
            }
        }
    }
}
