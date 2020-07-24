import java.util.List;

import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class POSExample {
    public static void main(String[] args) {
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();

        String text = "Hey I am Adnan Harun DOGAN.";

        CoreDocument coreDocument = new CoreDocument(text);

        stanfordCoreNLP.annotate(coreDocument);

        List<CoreLabel> coreLabels = coreDocument.tokens();

        for (CoreLabel coreLabel : coreLabels)
            System.out.println(coreLabel.category());

    }
}
