package loez.nllr.algorithm;

import java.util.ArrayList;
import loez.nllr.domain.Corpus;
import loez.nllr.domain.Document;

/**
 *
 * @author ljleppan@cs
 */
public class Tfidf {
    
    /**
     * Calculates a tf-idf (text frequency - inverse document frequency) score.
     * The tf-idf weights the frequency of a token based on how well it represents the text in question.
     * F.ex. the word "the" is common in all texts, so its high tf is weighted down by the low idf.
     * Similarly, the word "Abracadabra" is uncommon, so its low tf is weighted up by the high idf.
     * 
     * Here, raw frequency is used for tf:
     *      tf = (#_of_instances_of_token_in_text) / (#_of_total_tokens_in_text)
     * The definition for idf is:
     *      idf = log ( #_of_texts / #_of_texts_containing_token )
     * And tf-idf is finally calculated by:
     *      tf-idf = tf * idf
     * @param token     The token for which the tf-idf score is calculated
     * @param candidate The text, as an instance of Document
     * @param reference The document, as an instance of Corpus
     * @return          A tf-idf score for the given parameters, as a double
     */
    public static double tfidf(String token, Document query, Corpus reference){
        int tf = query.getFrequency(token);
        
        ArrayList<Document> referenceDocs = reference.getDocuments();
        int totalDocs = referenceDocs.size();
        
        int docsContainingTerm = 0;
        for (Document doc : referenceDocs){
            if (doc.getFrequency(token) != 0){
                docsContainingTerm++;
            }
        }
        
        double idf = Math.log( (double) totalDocs / docsContainingTerm);
        return tf * idf;
    }
}
