package loez.nllr.algorithm;

import loez.nllr.domain.Corpus;
import loez.nllr.domain.Document;

/**
 *
 * @author ljleppan@cs
 */
public class Nllr {
    private final Corpus corpus;
    
    /**
     * Creates a new NLLR-calculator for the specified corpus.
     * @param corpus    The corpus this NLLR-calculator is tied to.
     */
    public Nllr(Corpus corpus){
        this.corpus = corpus;
    }
    
    /**
     * Calculates the NLLR-score for the Query document and the Candidate corpus.
     * The candidate corpus MUST be a sub-corpus of the corpus the NLLR-instance is tied to.
     * @param query     The query document.
     * @param candidate The candidate corpus.
     * @return          The NLLR-score for the query document and the candidate corpus.
     */
    public float calculateNllr(Document query, Corpus candidate){
        float nllr = 0;
        
        for (String uniqueToken : query.getUniqueTokens()){
            float tokenProbQuery = calculateTokenProbability(uniqueToken, query);
            float tokenProbCandidate = calculateTokenProbability(uniqueToken, candidate);
            float tokenProbCorpus = calculateTokenProbability(uniqueToken, corpus);
            
            nllr += tokenProbQuery * Math.log(tokenProbCandidate / tokenProbCorpus);
        }
        
        return nllr;
    }

    /**
     * Calculates the token probability for the given token in the given document.
     * Token probability is defined as the frequency of the token divided by the total amount of tokens.
     * @param token     The token.
     * @param document  The document.
     * @return          The probability of the token in the document.
     */
    private float calculateTokenProbability(String token, Document document) {
        float prob =  (float) document.getFrequency(token) / document.getTotalTokens();
        if (prob == 0){
            return 1 / Float.MAX_VALUE;
        } else {
            return prob;
        }
    }
    
    /**
     * Calculates the token probability for the given token in the given corpus.
     * Token probability is defined as the frequency of the token divided by the total amount of tokens.
     * @param token     The token.
     * @param corpus    The corpus.
     * @return          The probability of the token in the corpus.
     */
    private float calculateTokenProbability(String token, Corpus corpus) {
        float prob = (float) corpus.getFrequency(token) / corpus.getTotalTokens();
        if (prob == 0){
            return 1 / Float.MAX_VALUE;
        } else {
            return prob;
        }
    }
    
   
}