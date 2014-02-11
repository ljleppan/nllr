package loez.nllr.userinterface;

import loez.nllr.datastructure.ArrayList;
import loez.nllr.preprocessor.PreProcessor;

/**
 *
 * @author ljleppan
 */
public interface UserInterface {
    public void run();
    public void setupPreprocessors(ArrayList<PreProcessor> preProcessors, ArrayList<String> preProcessorNames);
}
