package loez.nllr.domain;

import java.util.Calendar;
import java.util.GregorianCalendar;
import loez.nllr.datastructure.ArrayList;
import loez.nllr.datastructure.HashSet;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for Corpus
 * @author ljleppan@cs
 */
public class CorpusTest{
    
    private Corpus corpus;
    private Document docA;
    private Document docB;
    private ArrayList<Document> docs;
    
    
    @Before
    public void setUp() throws Exception {
        String docTextA = "asia asia auto asia asia";
        Calendar date = new GregorianCalendar();
        docA = new Document(date, docTextA);
        
        String docTextB = "juttu juttu hässäkkä";
        docB = new Document(date, docTextB);
        
        docs = new ArrayList<>();
        docs.add(docA);
        docs.add(docB);
        
        corpus = new Corpus(date, date, docs);
    }
    
    @Test
    public void testDatelessConstructorGenerateDates(){
        Corpus c = new Corpus(null, null, docs);
        assertNotNull("When creating a corpus without dates, the end date should not be null.",
                c.getEndDate());
        assertNotNull("When creating a corpus without dates, the start date should not be null.",
                c.getStartDate());
    }
    
    @Test
    public void testConstructorsShouldUpdateStatsProperly(){
        assertEquals("getTotalTokens should return 8 after construction with documents consisting of 5 and 3 tokens, instead got "+ corpus.getTotalTokens(),
                corpus.getTotalTokens(), 8);
    }
    
    @Test
    public void testGet(){       
        ArrayList<Document> docList = new ArrayList<>();
        docList.add(docA);
        Corpus c = new Corpus(null, null, docList);
        
        assertEquals("Getting the only document should return the only document.",
                c.get(0), docA);
        
        assertNull("Getting an index that doesn't exist should return null",
                c.get(15));
        
        c = new Corpus();
        
        assertNull("Getting a document from an empty corpus should return null",
                c.get(0));
    }
    
    @Test
    public void testRemove(){
        corpus.remove(0);
        assertNotSame("Removing a document updates the stats",
                corpus.getTotalTokens(), 8);
        assertNull("Removing a document removes the document",
                corpus.get(1));
    }
    
    @Test
    public void testAdd(){
        corpus.add(null);
        assertNull("Trying to add a null document does nothing",
                corpus.get(2));
        corpus.add(docB);
        assertEquals("Adding a document adds the document",
                corpus.get(2), docB);
    }
    
    @Test
    public void testGetFrequency(){
        assertEquals("getFrequency() should the correct frequency of the queried token",
                corpus.getFrequency("asia"), 4);
        assertEquals("getFrequency() should return 0 when the token is not present",
                corpus.getFrequency("blaaa"), 0);
        assertEquals("getFrequency() should return 0 when the token is null",
                corpus.getFrequency(null), 0);
    }
    
    @Test
    public void testUpdatingDates(){
        String body = "auto auto asia";
        
        Calendar first = new GregorianCalendar();
        first.set(2001, 1, 1);
        Calendar second = new GregorianCalendar();
        second.set(2002, 1, 1);
        Calendar third = new GregorianCalendar();
        third.set(2003, 1, 1);
        Calendar fourth = new GregorianCalendar();
        fourth.set(2004, 1, 1);
        
        ArrayList<Document> cDocs = new ArrayList<>();
        cDocs.add(new Document(second, body));
        cDocs.add(new Document(third, body));
        
        Corpus c = new Corpus(second, third, cDocs);
        Document d1 = new Document(second, body);
        
        c.add(d1);
        assertTrue("Adding a document with timestamp equal to corpus start timestamp doesn't change corpus timestamps",
                isSameDate(c.getStartDate(), second) && isSameDate(c.getEndDate(), third));
        
        Document d2 = new Document(third, body);
        c.add(d2);
        assertTrue("Adding a document with timestamp equal to corpus end timestamp doesn't change corpus timestamps",
                isSameDate(c.getStartDate(), second) && isSameDate(c.getEndDate(), third));
        
        Document d3 = new Document(first, body);
        c.add(d3);
        assertTrue("Adding a document with timestamp before the corpus start timestamp changes corpus start timestamp",
                isSameDate(c.getStartDate(), first) && isSameDate(c.getEndDate(), third));
        
        Document d4 = new Document(fourth, body);
        c.add(d4);
        assertTrue("Adding a document with timestamp after the corpus end timestamp changes corpus end timestamp",
                isSameDate(c.getStartDate(), first) && isSameDate(c.getEndDate(), fourth));
        
        Document d5 = new Document(second, body);
        c.add(d5);
        assertTrue("Adding a document with timestamp within the corpus start and end timestamp doesn't changes corpus timestamps",
                isSameDate(c.getStartDate(), first) && isSameDate(c.getEndDate(), fourth));
        
        Document d6 = new Document(null, body);
        c.add(d6);
        assertTrue("Adding a document with null timestamp doesn't change corpus timestamps",
                isSameDate(c.getStartDate(), first) && isSameDate(c.getEndDate(), fourth));
    }
    
    @Test
    public void testAddingDocumentsUpdatesTotalTokens(){
        int tokensBefore = corpus.getTotalTokens();
        Document doc = new Document(null, "auto testo");
        corpus.add(doc);
        assertEquals("Adding a document updates the total number of tokens correctly",
                corpus.getTotalTokens(), tokensBefore + 2);
    }
    
    @Test
    public void testGetDocuments(){
        assertTrue("getDocuments should return an arraylist",
                corpus.getDocuments() instanceof ArrayList);
        assertTrue("getDocuments should return ArrayList even when no documents were added",
                new Corpus(null, null, null).getDocuments() instanceof ArrayList);
    }
    
    @Test
    public void testGetUniqueTokens(){
        HashSet<String> uniqueTokens = corpus.getUniqueTokens();
        assertEquals("getUniqueTokens() returned an incorrect amount of unique tokens.",
                uniqueTokens.size(), 4);
        assertTrue("getUniqueTokens() should return a hashset that includes the String \"asia\"",
                uniqueTokens.contains("asia"));
        assertTrue("getUniqueTokens() should return a hashset that includes the String \"auto\"",
                uniqueTokens.contains("auto"));
    }
    
    private boolean isSameDate(Calendar a, Calendar b){
        return (a.get(Calendar.YEAR) == b.get(Calendar.YEAR)
                && a.get(Calendar.MONTH) == b.get(Calendar.MONTH)
                && a.get(Calendar.DAY_OF_MONTH) == b.get(Calendar.DAY_OF_MONTH));
    }
}
