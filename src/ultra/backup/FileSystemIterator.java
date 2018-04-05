/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultra.backup;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * NOTE: an empty folder is treated as a file
 * @author saud
 */
public class FileSystemIterator {
    private static final FilenameFilter fileFilter = (File dir, String name) -> {
        return new File(dir.getPath()+"\\"+name).canWrite();
    };
    private final File rootDir;
    private File currentFile;
    
    public FileSystemIterator(String startPath) {
        currentFile = new File(startPath);
        rootDir = currentFile;
        try {
            // search down first branch of tree
            while(currentFile.isDirectory() && currentFile.listFiles(fileFilter).length != 0) {
                currentFile = currentFile.listFiles(fileFilter)[0];
            }
        } catch(Exception e) {}
    }
    
    public void next() {
        int[] pos = getFilePos(currentFile);
        if(pos[0] == pos[1]) { // reached end of sibblings
            currentFile = searchUpTree(currentFile);
            if(currentFile == null) return;
        }
        File[] files = currentFile.getParentFile().listFiles(fileFilter);
        pos = getFilePos(currentFile);
        currentFile = files[pos[0]];
        try {
            while(currentFile.isDirectory() && currentFile.listFiles(fileFilter).length != 0) {
                currentFile = currentFile.listFiles(fileFilter)[0];
            }
        } catch(Exception e) {}
    }
    public boolean hasNext() {
        return currentFile != null;
    }
    public File getFile() {
        return currentFile;
    }
    
    /**
     * used to deal with a file that has no next sibling
     * @param input A file that does not have a next sibling
     * @return the first ancestor folder that has a next sibling
     */
    private File searchUpTree(File input) {
        while(true) {
            input = input.getParentFile();
            if(input.equals(rootDir)) return null;
            int[] pos = getFilePos(input);
            if(pos[0] != pos[1]) {
                return input;
            }
        }
    }
    
    /**
     * 
     * @param input the file to search
     * @return int[0]=posInSibblings, int[1] = numOfSibblings
     */
    public static int[] getFilePos(File input) {
        int[] out = new int[2];
        File[] files = input.getParentFile().listFiles(fileFilter); // sibbling files
        for(File file : files) {
            if(file.equals(input)) break;
            out[0]++;
        }
        out[0]++;
        out[1] = files.length;
        return out;
    }
    
}
