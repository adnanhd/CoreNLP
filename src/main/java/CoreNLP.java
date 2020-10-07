
// Import the File class
// Import the Scanner class to read text files
import java.io.*;
import java.util.ArrayList;
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
    private static String version = "version 2.1.0";
    private static StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
    private static String outfile = "data/article_word_ne_pos.csv";

    private static void run(int fileNumber, BufferedReader br, BufferedWriter bw) throws IOException {
        String line;

        while ((line = br.readLine()) != null) {
            CoreDocument coreDocument = new CoreDocument(line);

            stanfordCoreNLP.annotate(coreDocument);

            List<CoreLabel> coreLabels = coreDocument.tokens();

            for (CoreLabel coreLabel : coreLabels) {
                String ner = coreLabel.get(NamedEntityTagAnnotation.class);
                String pos = coreLabel.get(PartOfSpeechAnnotation.class);
                String text = coreLabel.originalText();
                bw.write(fileNumber + "," + text.replace(",", ".") + "," + ner + "," + pos + "\n");
            }
        }
        bw.close();
        br.close();
    }

    public static void main(String[] args) {
        List<BufferedReader> readerFiles = new ArrayList<>();
        List<String> argv = new ArrayList<>();

	File dir = new File("tests");

	File[] files = dir.listFiles();
        for (File arg : files)
     /*       if (arg.equals("--help"))
                System.out.println(help);
            else if (arg.equals("--version"))
                System.out.println(version);
            else if (arg.equals("--dump"))
                System.out.println("I am going to dump");
            else*/
                try {
                    readerFiles.add(new BufferedReader(
                            new InputStreamReader(new FileInputStream(arg), StandardCharsets.UTF_8)));
                } catch (FileNotFoundException e) {
                    System.err.println("File " + arg + " not found...");
                }

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outfile), StandardCharsets.UTF_8))) {
            for (BufferedReader br : readerFiles)
                run(readerFiles.indexOf(br), br, bw);
        } catch (IOException e) {
            System.err.println("Input Output exception occured");
        }
    }
}
