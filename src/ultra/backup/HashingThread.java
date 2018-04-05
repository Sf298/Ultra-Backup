
package ultra.backup;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Saud
 */
public class HashingThread implements Runnable, Serializable {
    
    private LinkedList<FileItem> toHashList;
    
    public HashingThread(LinkedList<FileItem> toHashList) {
        this.toHashList = toHashList;
    }

    @Override
    public void run() {
        while(true) {
            
            if(toHashList.size() > 0) {
                toHashList.getFirst().setHashCode(
                    FileItem.getHashCodeFromFile(toHashList.getFirst().getFile())
                );
                //System.out.println(toHashList.getFirst().getFile()+"" + (toHashList.getFirst().getHashCode()));
                toHashList.remove();
            } else if(Thread.currentThread().isInterrupted()) {
                break;
            } else {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
//        System.out.println("quitting hash");
    }
}
