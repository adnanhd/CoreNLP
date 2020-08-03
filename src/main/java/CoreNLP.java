
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
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;

public class CoreNLP {
    private static String help = "usage: CoreNLP [-r input_dir] [-o output_dir] [ files ]";
    private static String version = "version 2.0.0";
    private static String in_path = "./data/";
    private static String ou_path = "./csv/";
    private static StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();

    private static void run(int fileorder, String filename) {
        try {
            // Open a file in order to input sentences
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(in_path + filename), StandardCharsets.UTF_8));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(ou_path + filename.replace(".txt", ".csv")), StandardCharsets.UTF_8));

            String line = br.readLine();

            while (line != null) {
                CoreDocument coreDocument = new CoreDocument(line);

                stanfordCoreNLP.annotate(coreDocument);

                List<CoreLabel> coreLabels = coreDocument.tokens();

                for (CoreLabel coreLabel : coreLabels) {
                    String ner = coreLabel.get(NamedEntityTagAnnotation.class);
                    String pos = coreLabel.get(PartOfSpeechAnnotation.class);
                    String text = coreLabel.originalText();
                    text.replace(",", ".");
                    bw.write(fileorder + "," + text + "," + ner + ","+ pos +"\n");
                }
                line = br.readLine();
            }
            bw.close();
            br.close();
        } catch (FileNotFoundException e) {
            System.err.println("thrown FileNotFoundException in " + filename);
        } catch (IOException e) {
            System.err.println("thrown IOException in " + filename);
        }
    }

    public static void main(String[] args) {
        /*
         * File file = new File(in_path); File[] files = file.listFiles();
         * 
         * for (int forder = 0; forder < files.length; forder++) run(forder,
         * file.getName());
         * 
         */

        run(1, "123.txt");
    }
}