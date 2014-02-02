package loez.nllr.algorithm;

import java.util.ArrayList;
import junit.framework.TestCase;
import loez.nllr.algorithm.Nllr;
import loez.nllr.domain.Corpus;
import loez.nllr.domain.Document;


/**
 *
 * @author loezi
 */
public class NllrTest extends TestCase{
    
    private Document docA;
    private Document docB;
    private Document docC;
    private Document docD;   
    private Corpus reference;
    private Corpus candidateA;
    private Corpus candidateB;    
    private Document query;
    private Nllr nllr;

    @Override
    public void setUp() {
        docA = new Document(null, "auto juttu juttu auto asia");
        docB = new Document(null, "auto hämminki kiva hässäkkä");
        docC = new Document(null, "testaus olla aina tosi kiva auto");
        docD = new Document(null, "mutta token keksiä joskus vaikea");
        
        ArrayList<Document> refDocs = new ArrayList<>();
        refDocs.add(docA); 
        refDocs.add(docB); 
        refDocs.add(docC); 
        refDocs.add(docD);
        reference = new Corpus(null, null, refDocs);
        
        ArrayList<Document> candADocs = new ArrayList<>();
        candADocs.add(docA);
        candADocs.add(docB);
        candidateA = new Corpus(null, null, candADocs);
        
        ArrayList<Document> candBDocs = new ArrayList<>();
        candBDocs.add(docC);
        candBDocs.add(docD);
        candidateB = new Corpus(null, null, candBDocs);
        
        query = new Document(null, "token token hässäkkä auto joskus");
        
        nllr = new Nllr(reference);
    }
    
    public void testCalculateTokenProbabilityForDocument(){
        assertTrue("Probability for tokens not present in the document should be 0.0000000000000001",
                equal(nllr.calculateTokenProbability("testi", docA), Nllr.NONZERO));
        assertTrue("Probability for token that is present 2 times in a document with 5 tokens should be 0.4",
                equal(nllr.calculateTokenProbability("auto", docA), 0.4));
        assertTrue("Probability for token that is present 1 times in a document with 5 tokens should be 0.2",
                equal(nllr.calculateTokenProbability("asia", docA), 0.2));
    }
    
    public void testCalculateTokenProbabilityForCorpus(){
        assertTrue("Probability for tokens not present in the corpus should be 0.0000000000000001",
                equal(nllr.calculateTokenProbability("testi", candidateA), Nllr.NONZERO));
        assertTrue("Probability for token that is present 3 times in a corpus with 9 tokens should be 1/3",
                equal(nllr.calculateTokenProbability("auto", candidateA), 1.0/3));
        assertTrue("Probability for token that is present 1 times in a corpus with 9 tokens should be 1/9",
                equal(nllr.calculateTokenProbability("asia", candidateA), 1.0/9));
    }
    
    public void testCalculateNllr(){       
        /*
        NLLR #1
        sub-results for each token in query:
        token:      (2/5) * log( 0.0000001 / (2/20) ) = -5.248945350961732
        hässäkkä:   (1/5) * log(   (1/9)   / (1/20) ) =  0.1597015392435543
        auto:       (1/5) * log(   (3/9    / (4/20) ) =  0.10216512475319812
        joskus:     (1/5) * log( 0.0000001 / (1/20) ) = -2.624472675480866

        sum:
        -7.611551362445845
        */
        double expected = -7.611551362445845;
        double actual = nllr.calculateNllr(query, candidateA);
                
        assertTrue("NLLR calculation #1 gave incorrect result, see test file for more information. Expected "+ expected +" got "+ actual,
                equal(expected, actual));
        
        System.out.println("---");
        
        /*
        docA = new Document(null, "auto juttu juttu auto asia");
        docB = new Document(null, "auto hämminki kiva hässäkkä");
        docC = new Document(null, "testaus olla aina tosi kiva auto");
        docD = new Document(null, "mutta token keksiä joskus vaikea");
        
        NLLR #2
        sub-results for each token in query:
        token:      (2/5) * log(   (1/11)  / (2/20) ) =  0.23913480030224818
        hässäkkä:   (1/5) * log( 0.0000001 / (1/20) ) = -2.624472675480866
        auto:       (1/5) * log(   (1/11   / (4/20) ) = -0.15769147207285406
        joskus:     (1/5) * log(   (1/11)  / (1/20) ) =  0.11956740015112409

        sum:
        -2.4234619471003476
        */
        expected = -2.4234619471003476;
        actual = nllr.calculateNllr(query, candidateB);
                
        assertTrue("NLLR calculation #2 gave incorrect result, see test file for more information. Expected "+ expected +" got "+ actual,
                equal(expected, actual));
        
        
    }
    
    public boolean equal(double a, double b){
        double epsilon = 0.00000001;
        return Math.abs(a - b) < epsilon;
    }

}