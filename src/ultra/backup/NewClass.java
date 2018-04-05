/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultra.backup;

/**
 *
 * @author Saud
 */
public class NewClass {
    
    public static void main(String[] args) {
        FileSystemIterator fsi = new FileSystemIterator("C:\\Users\\Saud\\Desktop\\SAUD");
        while(fsi.hasNext()) {
            System.out.println(fsi.getFile());
            fsi.next();
        }
    }
    
}
