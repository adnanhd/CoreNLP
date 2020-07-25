
// Import the File class
// Import this class to handle errors
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
// Import the Scanner class to read text files
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;

public class CoreNLP {
    public static void main(String[] args) {
        int count = 0;
        File file = new File("data/");
        File[] files = file.listFiles();

        while (count < files.length)
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("ner/" + files[0].getName()), StandardCharsets.UTF_8));
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream("data/" + files[0].getName()), StandardCharsets.UTF_8));

                String line = br.readLine();

                while (line != null) {
                    CoreDocument coreDocument = new CoreDocument(line);
                    StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
                    
                    stanfordCoreNLP.annotate(coreDocument);

                    List<CoreLabel> coreLabels = coreDocument.tokens();

                    for (CoreLabel coreLabel : coreLabels) {
                        String ner = coreLabel.get(NamedEntityTagAnnotation.class);
                        bw.write(coreLabel.originalText() + " == " + ner);
                    }
                    line = br.readLine();
                }

                bw.close();
                br.close();

            } catch (FileNotFoundException e) {
                System.err.println("File " + args[count] + "not found...");
            } catch (IOException e) {
                System.err.println("IOException in " + args[count] + "occured...");
            }
    }
}
