package loez.nllr.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import junit.framework.TestCase;

/**
 * Tests for Corpus
 * @author ljleppan@cs
 */
public class CorpusTest extends TestCase {
    
    private Corpus corpus;
    private Document docA;
    private Document docB;
    private ArrayList<Document> docs;
    
    
    @Override
    protected void setUp() throws Exception {
        String docTextA = "asia asia auto asia asia";
        Calendar date = new GregorianCalendar();
        docA = new Document(date, docTextA);
        
        String docTextB = "juttu juttu hässäkkä";
        docB = new Document(date, docTextB);
        
        docs = new ArrayList<Document>();
        docs.add(docA);
        docs.add(docB);
        
        corpus = new Corpus(date, date, docs);
    }
    
    public void testDatelessConstructorDoesNotGenerateDates(){
        Corpus c = new Corpus(null, null, docs);
        assertTrue("When creating a corpus without dates, the end date should be null.",
                c.getEndDate() == null);
        assertTrue("When creating a corpus without dates, the start date should be null.",
                c.getStartDate() == null);
    }
    
    public void testConstructorsShouldUpdateStatsProperly(){
        assertTrue("getTotalTokens should return 8 after construction with documents consisting of 5 and 3 tokens, instead got "+ corpus.getTotalTokens(),
                corpus.getTotalTokens() == 8);
    }
    
    public void testGet(){       
        ArrayList<Document> docList = new ArrayList<Document>();
        docList.add(docA);
        Corpus c = new Corpus(null, null, docList);
        
        assertTrue("Getting the only document should return the only document.",
                c.get(0) == docA);
        
        assertTrue("Getting an index that doesn't exist should return null",
                c.get(15) == null);
        
        c = new Corpus();
        
        assertTrue("Getting a document from an empty corpus should return null",
                c.get(0) == null);
    }
    
    public void testRemove(){
        corpus.remove(0);
        assertTrue("Removing a document updates the stats",
                corpus.getTotalTokens() != 8);
        assertTrue("Removing a document removes the document",
                corpus.get(1) == null);
    }
    
    public void testAdd(){
        corpus.add(null);
        assertTrue("Trying to add a null document does nothing",
                corpus.get(2) == null);
        corpus.add(docB);
        assertTrue("Adding a document adds the document",
                corpus.get(2) == docB);
    }
}
