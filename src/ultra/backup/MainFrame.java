
package ultra.backup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author Saud
 */
public class MainFrame extends JFrame {
    
    public MainFrame() {
        JPanel mainPanel = new JPanel(new BorderLayout());
            
            mainPanel.add(getBackupsListPanel(), BorderLayout.WEST);
            
            JPanel controlPanel = new JPanel(new BorderLayout());
            
                controlPanel.add(getInfoPanel(), BorderLayout.CENTER);
                
                controlPanel.add(getButtonPanel(), BorderLayout.SOUTH);
                     
            mainPanel.add(controlPanel, BorderLayout.CENTER);
             
        add(mainPanel);
        
        setComponentsEnabled(false);
    }
    
    private static JList backupList;
    private DefaultListModel dlm;
    private JPanel getBackupsListPanel() {
        JPanel blp = new JPanel(new BorderLayout());
        
            dlm = new DefaultListModel();
             for(BackupItem bi : Viewer.backups.getBackupItems()) {
                dlm.addElement(bi.getName());
             }
             dlm.addElement("Add");

            backupList = new JList(dlm);
             backupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
             backupList.setLayoutOrientation(JList.VERTICAL_WRAP);
             backupList.setVisibleRowCount(-1);
             backupList.setPreferredSize(new Dimension(250, 0));
             backupList.addListSelectionListener((ListSelectionEvent e) -> {
                if(!e.getValueIsAdjusting()) {
                    if(backupList.getSelectedValue() != null) {
                        if(backupList.getSelectedValue().equals("Add")) {
                            Viewer.backups.newBackup("New Backup", "");
                            dlm.set(dlm.getSize()-1, "New Backup");
                            dlm.addElement("Add");
                        }
                        updateInfoPanel();
                        setComponentsEnabled(true);
                    }
                }
             });
             
             blp.add(backupList, BorderLayout.CENTER);

       return blp;
    }
    
    private static JTextField nameField;
    private static JTextField rootDirField;
    private static JLabel dateBackedUpLabel;
    private static JLabel computerNameLabel;
    private static JLabel fileCountLabel;
    private static JButton button;
    private JPanel getInfoPanel() {
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

            label = new JLabel("Last Backup");
            c.gridy = 2;
            infoPanel.add(label, c);

            label = new JLabel("Computer Name");
            c.gridy = 3;
            infoPanel.add(label, c);

            label = new JLabel("File Count");
            c.gridy = 4;
            infoPanel.add(label, c);

            nameField = new JTextField();
            nameField.setPreferredSize(new Dimension(300, 20));
            nameField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {save();}
                @Override
                public void removeUpdate(DocumentEvent e) {save();}
                @Override
                public void changedUpdate(DocumentEvent e) {save();}
                private void save() {
                    if(backupList.getSelectedIndex() != -1) {
                        Viewer.backups.getBackupItemAt(backupList.getSelectedIndex())
                                .setName(nameField.getText());
                        dlm.set(backupList.getSelectedIndex(), nameField.getText());
                    }
                }
            });
            c.gridx = 1;
            c.gridy = 0;
            infoPanel.add(nameField, c);

            rootDirField = new JTextField();
            rootDirField.setPreferredSize(new Dimension(300, 20));
            rootDirField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {save();}
                @Override
                public void removeUpdate(DocumentEvent e) {save();}
                @Override
                public void changedUpdate(DocumentEvent e) {save();}
                public void save() {
                    Viewer.backups.getBackupItemAt(backupList.getSelectedIndex())
                            .setRooDirectory(new File(rootDirField.getText()));
                }
            });
            c.gridy = 1;
            infoPanel.add(rootDirField, c);

            dateBackedUpLabel = new JLabel();
            c.gridy = 2;
            infoPanel.add(dateBackedUpLabel, c);

            computerNameLabel = new JLabel();
            c.gridy = 3;
            infoPanel.add(computerNameLabel, c);

            button = new JButton("...");
            button.setMaximumSize(new Dimension(300, 30));
            button.addActionListener((ActionEvent e) -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    rootDirField.setText(fileChooser.getSelectedFile().getPath());
                }
            });
            c.gridx = 2;
            c.gridy = 1;
            infoPanel.add(button, c);

            fileCountLabel = new JLabel();
            c.gridx = 1;
            c.gridy = 4;
            infoPanel.add(fileCountLabel, c);

            label = new JLabel("");
            c.gridx = 3;
            c.gridy = 5;
            c.weightx = 1;
            c.weighty = 1;
            infoPanel.add(label, c);
        
        return infoPanel;
    }
    public static void updateInfoPanel() {
        BackupItem bi = Viewer.backups.getBackupItemAt(backupList.getSelectedIndex());
        nameField.setText(bi.getName());
        rootDirField.setText(bi.getRooDirectory().getPath());
        dateBackedUpLabel.setText(bi.getDate()+"");
        computerNameLabel.setText(bi.getComputerName());
        fileCountLabel.setText(bi.getSize()+"");
    }
    
    JButton backupButton;
    JButton restoreButton;
    JButton deleteButton;
    private JPanel getButtonPanel() {
        JPanel buttonPanel = new JPanel();
         buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

            backupButton = new JButton("Backup");
             backupButton.addActionListener((ActionEvent e) -> {
                System.out.println("backup");
                
                Thread t = new Thread(() -> {
                    BackupItem bi = Viewer.backups.getBackupItemAt(backupList.getSelectedIndex());
                    bi.backup();
                    System.err.println("STRUCTURE COMPLETE");
                });
                t.start();
             });
             buttonPanel.add(backupButton);

            restoreButton = new JButton("Restore");
             restoreButton.addActionListener((ActionEvent e) -> {
                 System.out.println("restore");
             });
             buttonPanel.add(restoreButton);

            deleteButton = new JButton("Delete");
             deleteButton.addActionListener((ActionEvent e) -> {
                 Viewer.backups.removeBackup(backupList.getSelectedIndex());
                 dlm.remove(backupList.getSelectedIndex());
                 setComponentsEnabled(false);
             });
             buttonPanel.add(deleteButton);
        return buttonPanel;
    }
    
    private void setComponentsEnabled(boolean b) {
        nameField.setEnabled(b);
        rootDirField.setEnabled(b);
        button.setEnabled(b);
        if(!b) {
            nameField.setText("");
            rootDirField.setText("");
        }
        
        backupButton.setEnabled(b);
        restoreButton.setEnabled(b);
        deleteButton.setEnabled(b);
    }
}
