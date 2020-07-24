import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;

public class Pipeline {
    private static Properties properties;
    private static String propertiesString;
    private static StanfordCoreNLP stanfordCoreNLP;

    private Pipeline() { }

    static {
        properties = new Properties();
        properties.setProperty("annotators", propertiesString);
    }

    public static StanfordCoreNLP getPipeline() {

        if (stanfordCoreNLP == null)
            stanfordCoreNLP = new StanfordCoreNLP(properties);
        
        return stanfordCoreNLP;
    }
}