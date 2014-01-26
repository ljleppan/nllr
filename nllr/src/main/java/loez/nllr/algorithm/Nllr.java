package loez.nllr.algorithm;

import loez.nllr.domain.Corpus;
import loez.nllr.domain.Document;

/**
 * An NLLR-calculator.
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
            double tokenProbQuery = calculateTokenProbability(uniqueToken, query);
            double tokenProbCandidate = calculateTokenProbability(uniqueToken, candidate);
            double tokenProbCorpus = calculateTokenProbability(uniqueToken, corpus);
            
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
    public double calculateTokenProbability(String token, Document document) {
        double prob =  (double) document.getFrequency(token) / document.getTotalTokens();
        if (prob == 0){
            return 1 / Double.MAX_VALUE;
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
    public double calculateTokenProbability(String token, Corpus corpus) {
        double prob = (double) corpus.getFrequency(token) / corpus.getTotalTokens();
        if (prob == 0){
            return 1 / Double.MAX_VALUE;
        } else {
            return prob;
        }
    }
    
   
}