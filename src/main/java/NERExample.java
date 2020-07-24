import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class NERExample {
    public static void main(String[] args) {
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();

        String text = "Hey! My Name is Adnan DOGAN, I have many friends living in Istanbul of Turkey.";

        CoreDocument coreDocument = new CoreDocument(text);

        stanfordCoreNLP.annotate(coreDocument);

        List<CoreLabel> coreLabels = coreDocument.tokens();

        for (CoreLabel coreLabel : coreLabels)
        {
            String ner = coreLabel.get(NamedEntityTagAnnotation.class);
            System.out.println(coreLabel.originalText() + " == " + ner);
        }
    }
}