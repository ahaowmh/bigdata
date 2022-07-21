package file;

import java.io.File;
import java.io.IOException;

public class Testfile {
    public static void main(String[] args) {
        File file = new File("/Users/ahao/Desktop/new");

        boolean flag = file.exists();
        if (flag) {
            file.delete();
            System.out.println("---delete----");
        }else{
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
                System.out.println("---mkdirs---");
            }
            try {
                file.createNewFile();
                System.out.println("-----creatNewFile----");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
