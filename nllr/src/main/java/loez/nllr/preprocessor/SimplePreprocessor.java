package loez.nllr.preprocessor;

/**
 * An extremely simple preprocessor, for use as a fallback.
 * @author loezi
 */
public class SimplePreprocessor implements PreProcessor{
    
    /**
     * Processes the input string into tokens.
     * @param input String of words
     * @return      String of tokens
     */
    @Override
    public String process(String input){
        String output = input.toUpperCase();
        output = output.replaceAll("[^A-Z ]", "");
        output = output.trim();
        return output;
    }
    
    @Override
    public void setLanguage(String language){
        //Do nothing!
    }
    
    @Override
    public String getLanguage(){
        return "Simple preprocessor is language independent";
    }
}
