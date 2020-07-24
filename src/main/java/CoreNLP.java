// Import the File class
import java.io.File;

public class CoreNLP {
    public static void main(String[] args){
        // Specify the filename
        File obj = new File("data/10-1055-s-0040-1713678.txt", "r");

        System.out.println(obj.exists() ? "Opened" : "Not Opened");
    }
}