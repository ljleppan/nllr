package loez.nllr.preprocessor;

import loez.nllr.preprocessor.util.Numeral;
import loez.nllr.preprocessor.util.Punctuation;

/**
 * An extremely simple preprocessor, for use as a fallback.
 * @author loezi
 */
public class SimplePreprocessor implements PreProcessor{
    private final Punctuation punctuation = new Punctuation();
    private final Numeral numeral = new Numeral();
    
    /**
     * Processes the input string into tokens.
     * @param input String of words
     * @return      String of tokens
     */
    @Override
    public String process(String input){
        String output = input;
        output = punctuation.remove(output);
        output = numeral.replace(output, "NUMERAL");
        output = output.trim().toUpperCase();
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
