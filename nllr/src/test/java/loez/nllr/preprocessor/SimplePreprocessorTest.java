package loez.nllr.preprocessor;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author loezi
 */
public class SimplePreprocessorTest {
    private SimplePreprocessor pp = new SimplePreprocessor();


    @Test
    public void correctlyProcesses(){
        assertEquals("a string should be correctly processed",
                "THIS IS A STRING",
                pp.process("tHiS, is; a string...."));
        assertEquals("a string should be correctly processed",
                "ALSO A STRING",
                pp.process("a.l.so A- string"));
    }
}
