package loez.nllr.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Corpus is a collection of Documents.
 * @author ljleppan@cs
 */
public class Corpus {
    private ArrayList<Document> documents;
    private Calendar startDate;
    private Calendar endDate;
    private int totalTokens;
    private HashMap<String, Integer> tokenFrequensies;
    
    /**
     * Creates a new corpus with known first and final dates of the included documents.
     * @param startDate The earliest date of creation for the corpus documents.
     * @param endDate   The latest date of creationg for the corpus documents.
     * @param documents The documents the corpus is comprised of.
     */
    public Corpus(Calendar startDate, Calendar endDate, ArrayList<Document> documents){
        this.startDate = startDate;
        this.endDate = endDate;
        this.documents = documents;
        
        this.tokenFrequensies = new HashMap<String, Integer>();
        totalTokens = 0;
        
        refreshStats();
    }
    
    /**
     * Create an empty corpus.
     */
    public Corpus(){
        this(null, null, new ArrayList<Document>());
    }
    
    /**
     * Add a document to the corpus.
     * @param document  The document to be added.
     */
    public void add(Document document){
        if (document != null){
            documents.add(document);
            refreshStats();
        }
    }
    
    /**
     * Gets the document with the specified index.
     * @param index Index of the document.
     * @return      The document with the specified index.
     */
    public Document get(int index){
        if (documents.size() < index){
            return documents.get(index);
        } else {
            return null;
        }
        
    }
    
    /**
     * Removes a document from the corpus.
     * @param index The index of the document.
     */
    public void remove(int index){
        documents.remove(index);
        refreshStats();
    }

    /**
     * Get the total amount of (non-unique) tokens in the corpus's documents.
     * @return  The total amount of (non-unique) tokens.
     */
    public int getTotalTokens() {
        return this.totalTokens;
    }
    
    /**
     * Get the frequency of a token within the corpus.
     * @param token The token.
     * @return      The frequency of the token withing the corpus.
     */
    public int getFrequency(String token) {
        return this.tokenFrequensies.get(token);
    }

    private void refreshStats() {
        for (Document doc : documents){
            
            if (getStartDate().after(doc.getDate())){
                startDate = doc.getDate();
            } else if (getEndDate().before(doc.getDate())){
                endDate = doc.getDate();
            }
            
            for (String token : doc.getUniqueTokens()){
                int docTokenAmount = doc.getFrequency(token);
                
                int amountNow = 0;
                if (tokenFrequensies.containsKey(token)) {
                    amountNow = tokenFrequensies.get(token);
                } 
                
                tokenFrequensies.put(token, amountNow + docTokenAmount);
                
                totalTokens += docTokenAmount;
            }
        }
    }

    /**
     * @return the startDate
     */
    public Calendar getStartDate() {
        return startDate;
    }

    /**
     * @return the endDate
     */
    public Calendar getEndDate() {
        return endDate;
    }
}