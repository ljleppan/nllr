package loez.nllr.domain;

import java.util.Calendar;
import java.util.GregorianCalendar;
import junit.framework.TestCase;
import loez.nllr.datastructure.HashSet;

/**
 * Tests for Document
 * @author ljleppan@cs
 */
public class DocumentTest extends TestCase {
    private Document doc;
    
    
    @Override
    protected void setUp() throws Exception {
        String docBody = "auto asia auto auto auto";
        Calendar docDate = new GregorianCalendar();
        doc = new Document(docDate, docBody);
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testDatelessConstructorDoesNotGenerateDates(){
        Document d = new Document("asd");
        assertTrue("Creating a new Document without a date resulted in a non-null date",
                d.getDate() == null);

    }
    
    public void testGetUniqueTokens(){
        HashSet<String> uniqueTokens = doc.getUniqueTokens();
        assertTrue("getUniqueTokens() returned an incorrect amount of unique tokens.",
                uniqueTokens.size() == 2);
        assertTrue("getUniqueTokens() should return an array that includes the String \"asia\"",
                uniqueTokens.contains("asia"));
        assertTrue("getUniqueTokens() should return an array that includes the String \"auto\"",
                uniqueTokens.contains("auto"));
    }
    
    public void testGetTotalTokens(){
        assertTrue("getTotalTokens should return 5 when the document body has 5 tokens", 
               doc.getTotalTokens() == 5);
    }
    
    public void testGetFrequency(){
        assertTrue("getFrequency should return 4 when the document has 4 of the queried token",
                doc.getFrequency("auto") == 4);
        assertTrue("getFrequency should return 1 when the document has 1 of the queried token",
                doc.getFrequency("asia") == 1);
        assertTrue("getFrequency should return 0 when the document has 0 of the queried token",
                doc.getFrequency("tokenThatDoesNotExist") == 0);
    }
}
