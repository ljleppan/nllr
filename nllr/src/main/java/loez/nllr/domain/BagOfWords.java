package loez.nllr.domain;

import loez.nllr.datastructure.HashSet;

/**
 *
 * @author loezi
 */
public interface BagOfWords {
    public int getTotalTokens();
    public int getFrequency(String token);
    public HashSet<String> getUniqueTokens();
}
