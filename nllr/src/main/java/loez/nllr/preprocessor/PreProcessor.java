package loez.nllr.preprocessor;

import loez.nllr.preprocessor.exception.StemmerCreationException;

/**
 * A preprocessor that is used for processing the raw input string into tokens for further analysis.
 * @author loezi
 */
public interface PreProcessor {

    /**
     * Processes given string (of words) to proper tokens.
     * @param input
     * @return
     */
    public String process(String input);
    
    public void setLanguage(String language) throws StemmerCreationException;
    
    public String getLanguage();
}
