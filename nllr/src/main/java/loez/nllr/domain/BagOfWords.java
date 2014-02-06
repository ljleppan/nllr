package loez.nllr.domain;

import loez.nllr.datastructure.ArrayList;

/**
 *
 * @author loezi
 */
public interface BagOfWords {
    public int getTotalTokens();
    public int getFrequency(String token);
    public ArrayList<String> getUniqueTokens();
}
