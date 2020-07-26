
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
    private static String input_path = "./tst/", output_path = "./ner/";
    private static String help = "usage: CoreNLP [-r input_dir] [-o output_dir]";
    private static String version = "version 2.0.0";
    //private static StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();

    private static void run(String filename) {
        try {
            // Open the file bw to output Named Entity Recognitions
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(output_path + filename), StandardCharsets.UTF_8));
            // Open a file in order to input sentences
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(input_path + filename), StandardCharsets.UTF_8));
                    StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();

            String line = br.readLine();

            while (line != null) {
                CoreDocument coreDocument = new CoreDocument(line);

                stanfordCoreNLP.annotate(coreDocument);

                List<CoreLabel> coreLabels = coreDocument.tokens();

                for (CoreLabel coreLabel : coreLabels) {
                    String ner = coreLabel.get(NamedEntityTagAnnotation.class);
                    bw.write(coreLabel.originalText() + " == " + ner + "\n");
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
        int index = 0;

        while (index < args.length)
            run(args[index++]);

        
    }
}

/**
 * int count = 0; List<File> files = new ArrayList<File>();
 * 
 * while (count < args.length) { File file = new File(args[count++]); if
 * (file.isDirectory()) { File[] fileList = file.listFiles(); int count2 = 0;
 * while (count2 < fileList.length) files.add(fileList[count2++]); } else
 * files.add(file); }
 * 
 * for (File file : files) System.out.printf("[CoreNLP] %d out of %d files...
 * %s\n", files.indexOf(file), files.size(), file.getName());
 */
