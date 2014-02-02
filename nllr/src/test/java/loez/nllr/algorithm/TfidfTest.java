/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package loez.nllr.algorithm;

import junit.framework.TestCase;
import loez.nllr.datastructure.ArrayList;
import loez.nllr.domain.Corpus;
import loez.nllr.domain.Document;

/**
 *
 * @author loezi
 */
public class TfidfTest extends TestCase{
    
    private Document docA;
    private Document docB;
    private Document docC;
    private Document docD;   
    private ArrayList<Document> refDocs;
    private Corpus reference;
    
    @Override
    public void setUp() {
        docA = new Document(null, "auto juttu juttu auto asia");
        docB = new Document(null, "auto hämminki kiva hässäkkä");
        docC = new Document(null, "testaus olla aina tosi kiva auto");
        docD = new Document(null, "mutta token keksiä joskus vaikea");
        
        refDocs = new ArrayList<>();
        refDocs.add(docA); 
        refDocs.add(docB); 
        refDocs.add(docC); 
        refDocs.add(docD);
        reference = new Corpus(null, null, refDocs);
    }
    
    public void testIdf(){
        /**
         * IDF for a corpus of 4 texts, of which 3 contain the token:
         * log (4 / 3) = 0.287682072451780
         */
        double expected = 0.287682072451780;
        double got = Tfidf.idf(refDocs, "auto");
        assertTrue("IDF test #1 failed, see test code for further details. Expected "+expected+" got "+got,
                equal(got , expected));
        
        /**
         * IDF for a corpus of 4 texts, of which 1 contain the token:
         * log (4 / 1) = 1.386294361119890
         */
        expected = 1.386294361119890;
        got = Tfidf.idf(refDocs, "asia");
        assertTrue("IDF test #2 failed, see test code for further details. Expected "+expected+" got "+got,
                equal(got , expected));
    }
    
    public void testTfidf(){
        /**
         * TF-IDF for a corpus of 4 texts, of which 3 contain the token that has a TF of 2:
         * 2 * log (4 / 3) = 0.5753641449035618548784
         */
        double expected = 0.5753641449035618548784;
        double got = Tfidf.tfidf("auto", docA, reference);
        assertTrue("TF-IDF test #1 failed, see test code for further details. Expected "+expected+" got "+got,
                equal(got , expected));
        
        /**
         * TF-IDF for a corpus of 4 texts, of which 1 contain the token that has a TF of 1:
         * (1 / 5) * log (4 / 1) = 1.386294361119890
         */
        expected = 1.386294361119890;
        got = Tfidf.tfidf("token", docD, reference);
        assertTrue("TF-IDF test #2 failed, see test code for further details. Expected "+expected+" got "+got,
                equal(got , expected));
    }
    
    public boolean equal(double a, double b){
        double epsilon = 0.00000001;
        return Math.abs(a - b) < epsilon;
    }
    
    
}
