package loez.nllr.preprocessor.util;

/**
 *
 * @author ljleppan
 */
public class Numeral implements PreprocessorUtil {

    @Override
    public String replace(String input, String replacement){
        return input.replaceAll("\\p{Digit}+", replacement);
    }

    @Override
    public String remove(String input){
        return replace(input, "");
    }
}
