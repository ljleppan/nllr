package loez.nllr.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import loez.nllr.domain.Corpus;
import loez.nllr.domain.Document;
import loez.nllr.preprocessor.PreProcessor;

/**
 * A multi purpose corpus reader.
 * @author loezi
 */
public class CorpusReader {

    /**
     * Reads a corpus from a file.
     * @param path          Path to file
     * @param dateFormat    DateFormat for parsing datestrings to dates
     * @param preprocessor  Preprocessor for processing words to tokens
     * @return              A corpus parsed from the file
     */
    public Corpus readCorpus(String path, DateFormat dateFormat, PreProcessor preprocessor){
        try(BufferedReader in = new BufferedReader(new FileReader(path))) {
            String rawDocumentString;
            Corpus corpus = new Corpus();
            System.out.println("Parsing corpus");
            int i = 0;
            while ((rawDocumentString = in.readLine()) != null){
                System.out.print("Document "+ (++i) +" ... ");
                Document document = DocumentConverter.rawStringToDocument(rawDocumentString, dateFormat, preprocessor);
                if (document != null){
                    corpus.add(document);
                    System.out.print("succesfull.");
                }
                System.out.println("");
            }

            return corpus;
        } catch (IOException e) {
            return null;
        }
    }
    
    
}
