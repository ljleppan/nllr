package loez.nllr;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import loez.nllr.algorithm.Argmax;
import loez.nllr.algorithm.Tfidf;
import loez.nllr.datastructure.ArrayList;
import loez.nllr.domain.Corpus;
import loez.nllr.domain.Document;
import loez.nllr.preprocessor.PreProcessor;
import loez.nllr.preprocessor.SimplePreprocessor;
import loez.nllr.reader.CorpusReader;

public class Main 
{

    public static void main( String[] args )
    {
        
        DateFormat df = new SimpleDateFormat("d-MMM-yyyy HH:mm:ss.SS"); //8-APR-1987 01:12:53.29
        PreProcessor pp = new SimplePreprocessor();
        
        CorpusReader cr = new CorpusReader();
 
        Corpus c = cr.readCorpus("/home/loezi/nllr/reuters-fixed/TEST.csv", df, pp);
        
        System.out.println("UNIQUE TOKENS:");
        for (String s : c.getDocuments().get(0).getUniqueTokens()){
            System.out.println(s);
        }
        
        System.out.println("\n");
        
        System.out.println("MOST REPRESENTATIVE TOKEN FOR EACH DOC:");
        for (Document d : c.getDocuments()) {
            
            ArrayList<Object> tokens = new ArrayList<>();
            for (String token : d.getUniqueTokens()){
                tokens.add(token);
            }
            
            Object[] constants = {d, c};
            
            Object[] max = Argmax.single(new Tfidf(), tokens, constants);
            String maxArg = (String) max[0];
            double maxVal = (double) max[1];
            
            DecimalFormat decim = new DecimalFormat("00.000000000");
            System.out.println(String.format("%15s : %s", maxArg, decim.format(maxVal)));
        }
    }
}
