
// Import the File class
// Import the Scanner class to read text files
import java.io.*;
import java.util.List;
import java.nio.charset.StandardCharsets;

// Import the stanford CoreNLP libraries
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;

public class CoreNLP {
    private static String help = "usage: CoreNLP [-r input_dir] [-o output_dir]";
    private static String version = "version 2.0.0";
    private static StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();

    private static void run(int fileorder, String filename) {
        try {
            // Open a file in order to input sentences
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8));

            String line = br.readLine();

            while (line != null) {
                CoreDocument coreDocument = new CoreDocument(line);

                stanfordCoreNLP.annotate(coreDocument);

                List<CoreLabel> coreLabels = coreDocument.tokens();

                for (CoreLabel coreLabel : coreLabels) {
                    String ner = coreLabel.get(NamedEntityTagAnnotation.class);
                    String text = coreLabel.originalText();
                    text.replace(",", ".");
                    System.out.println(fileorder + "," + text + "," + ner);
                }
                line = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.err.println("thrown FileNotFoundException in " + filename);
        } catch (IOException e) {
            System.err.println("thrown IOException in " + filename);
        }
    }

    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++)
            if (args[i].equals("-h") || args[i].equals("--help"))
                System.out.println(help);
            else if (args[i].equals("-v") || args[i].equals("--version"))
                System.out.println(version);
            else
                run(i, args[i]);
    }
}