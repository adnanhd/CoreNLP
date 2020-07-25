
// Import the File class
// Import this class to handle errors
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.nio.charset.StandardCharsets;
// Import the Scanner class to read text files

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;

public class CoreNLP {
    public static void main(String[] args) {
        int count = 0;
        List<File> files = new LinkedList<File>();

        while (count < args.length) {
            File file = new File(args[count++]);
            if (file.isDirectory()) {
                File[] fileList = file.listFiles();
                int count2 = 0;
                while (count2 < fileList.length)
                    files.add(fileList[count2++]);
            } else
                files.add(file);
        }

        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();

        System.out.printf("CoreNLP process %d files...\n", args.length);

        for (File file : files)
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("ner/" + file.getName()), StandardCharsets.UTF_8));
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream("test/" + file.getName()), StandardCharsets.UTF_8));

                String line = br.readLine();

                while (line != null) {
                    CoreDocument coreDocument = new CoreDocument(line);

                    stanfordCoreNLP.annotate(coreDocument);

                    List<CoreLabel> coreLabels = coreDocument.tokens();

                    for (CoreLabel coreLabel : coreLabels) {
                        String ner = coreLabel.get(NamedEntityTagAnnotation.class);
                        bw.write(coreLabel.originalText() + " == " + ner + "\n");
                    }

                    bw.write(line);
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
