package loez.nllr.domain;

import loez.nllr.datastructure.HashMap;
import java.util.Calendar;
import java.util.GregorianCalendar;
import loez.nllr.datastructure.ArrayList;
import loez.nllr.datastructure.HashSet;


/**
 * Corpus is a collection of Documents.
 * @author ljleppan@cs
 */
public class Corpus implements BagOfWords{
    private ArrayList<Document> documents = new ArrayList<>();
    private Calendar startDate = new GregorianCalendar();
    private Calendar endDate = new GregorianCalendar();
    private int totalTokens;
    private HashMap<String, Integer> tokenFrequensies;
    
    /**
     * Creates a new corpus with known first and final dates of the included documents.
     * @param startDate The earliest date of creation for the corpus documents.
     * @param endDate   The latest date of creation for the corpus documents.
     * @param documents The documents the corpus is comprised of.
     */
    public Corpus(Calendar startDate, Calendar endDate, ArrayList<Document> documents){
        if (startDate != null){
            this.startDate = startDate;
        }
        if (endDate != null){
            this.endDate = endDate;
        }
        if (documents != null){
            this.documents = documents;
        }

        this.tokenFrequensies = new HashMap<>();
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
        if (documents.size() > index){
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
    @Override
    public int getTotalTokens() {
        return this.totalTokens;
    }
    
    /**
     * Get the frequency of a token within the corpus.
     * @param token The token.
     * @return      The frequency of the token withing the corpus.
     */
    @Override
    public int getFrequency(String token) {
        if (this.tokenFrequensies.containsKey(token)){
            return this.tokenFrequensies.get(token);
        }
        return 0;
    }

    private void refreshStats() {
        totalTokens = 0;
        tokenFrequensies = new HashMap<>();
        
        for (Document doc : documents){
            refreshDates(doc);
            refreshFrequencies(doc);
            totalTokens += doc.getTotalTokens();
        }
    }

    private void refreshFrequencies(Document doc) {
        for (String token : doc.getUniqueTokens()){    
            int docTokenAmount = doc.getFrequency(token);
            
            int amountNow = 0;
            if (tokenFrequensies.containsKey(token)) {
                amountNow = tokenFrequensies.get(token);
            }

            tokenFrequensies.put(token, amountNow + docTokenAmount);
        }
    }

    private void refreshDates(Document doc) {
        if (doc.getDate() != null){
            if (getStartDate().after(doc.getDate())){
                startDate.setTime(doc.getDate().getTime());
            } else if (getEndDate().before(doc.getDate())){
                endDate.setTime(doc.getDate().getTime());
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

    /**
     * @return 
     */
    public ArrayList<Document> getDocuments() {
        return documents;
    }
    
    @Override
    public HashSet<String> getUniqueTokens() {
        HashSet<String> uniqueTokens = new HashSet<>();
        for (Document d : documents){
            for (String token : d.getUniqueTokens()){
                uniqueTokens.add(token);
            }
        }
        return uniqueTokens;
    }
}