import java.nio.channels.Pipe;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class TokenizeExample {
    public static void main(String[] args){
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();

        String text = "Hey! This is Adnan Dogan";

        CoreDocument coreDocument = new CoreDocument(text);

        stanfordCoreNLP.annotate(coreDocument);

        List<CoreLabel> coreLabels = coreDocument.tokens();

        for (CoreLabel coreLabel  : coreLabels)
            System.out.println(coreLabel.originalText());;
    }    
}