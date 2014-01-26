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
     * 
     * @param token
     * @param candidate
     * @param reference
     * @return 
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
