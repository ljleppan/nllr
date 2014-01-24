package loez.nllr.domain;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Document is a single text.
 * @author ljleppan@cs
 */
public class Document {
    private String body;
    private Calendar date;
    private HashMap<String, Integer> tokenAmounts;
    private int numTokens;
    
    /**
     * Creates a new document with known date.
     * @param date  Date of document's creation.
     * @param body  The text of the document.
     */
    public Document(Calendar date, String body){
        this.body = body;
        this.date = date;
        
        this.tokenAmounts = new HashMap<String, Integer>();
        this.numTokens = 0;
        
        countTokenFrequencies();
    }
    
    /**
     * Creates a new document without a known date.
     * @param body  The text of the document.
     */
    public Document(String body){
        this(null, body);
    }

    private void countTokenFrequencies() {
        numTokens = 0;
        
        String[] tokens = body.split(" ");
        for (String t : tokens){
            numTokens++;
            
            if (tokenAmounts.containsKey(t)){
                int amountNow = tokenAmounts.get(t);
                tokenAmounts.put(t, amountNow + 1);
            } else {
                tokenAmounts.put(t, 1);
            }
        }
    }
    
    /**
     * Get the frequency of a given token in the document.
     * @param token The query token.
     * @return      The frequency of the token.
     */
    public int getFrequency(String token){
        return tokenAmounts.get(token);
    }
    
    /**
     * Get the total amount of tokens (non-unique) in the document.
     * @return  The total amount of token in the document body.
     */
    public int getTotalTokens(){
        return numTokens;
    }
    
    /**
     * Get the document's unique tokens.
     * @return  An array of unique tokens in the documents. Order is not specified.
     */
    public String[] getUniqueTokens(){
        return tokenAmounts.keySet().toArray(new String[tokenAmounts.keySet().size()]);
    }
    
    /**
     * Get the date of creation of the document.
     * @return The date of creation.
     */
    public Calendar getDate(){
        return date;
    }
}