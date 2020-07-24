
// Import the File class
import java.io.File;

// Import this class to handle errors
import java.io.FileNotFoundException;
import java.util.List;
// Import the Scanner class to read text files
import java.util.Scanner;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;

public class CoreNLP {
    public static void main(String[] args) {
        try {
            // Creating an object of the file for reading the data
            File file = new File("data/10-1055-s-0040-1713678.txt");
            Scanner scanner = new Scanner(file);

            StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();

            String text = "";

            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                text = text + data;
            }

            CoreDocument coreDocument = new CoreDocument(text);

            stanfordCoreNLP.annotate(coreDocument);

            List<CoreLabel> coreLabels = coreDocument.tokens();

            for (CoreLabel coreLabel : coreLabels) {
                String ner = coreLabel.get(NamedEntityTagAnnotation.class);
                System.out.println(coreLabel.originalText() + " == " + ner);
            }

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}