package loez.nllr.algorithm;

import loez.nllr.domain.BagOfWords;
import loez.nllr.domain.Corpus;
import loez.nllr.domain.Document;

/**
 * An NLLR-calculator.
 * @author ljleppan@cs
 */
public class Nllr {
    private final Corpus corpus;

    /**
     * A small near-zero constant that is used in place of zero to prevent divisions by zero.
     */
    public static final double NONZERO = 0.0000001;
    
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
    public double calculateNllr(Document query, Corpus candidate){
        double nllr = 0;
        
        for (String uniqueToken : query.getUniqueTokens()){
            double tokenProbQuery = calculateTokenProbability(uniqueToken, query);
            double tokenProbCandidate = calculateTokenProbability(uniqueToken, candidate);
            double tokenProbCorpus = calculateTokenProbability(uniqueToken, corpus);

            nllr += tokenProbQuery * Math.log(tokenProbCandidate / tokenProbCorpus);
        }
        
        return nllr;
    }

    /**
     * Calculates the token probability for the given token in the given BagOfWords.
     * Token probability is defined as the frequency of the token divided by the total amount of tokens.
     * @param token      The token.
     * @param bagOfWords The BagOfWords
     * @return           The probability of the token in the document.
     */
    public double calculateTokenProbability(String token, BagOfWords bagOfWords) {
        double prob =  (double) bagOfWords.getFrequency(token) / bagOfWords.getTotalTokens();
        if (prob == 0){
            return NONZERO;
        } else {
            return prob;
        }
    }
}