package loez.nllr;

import loez.nllr.datastructure.ArrayList;
import loez.nllr.preprocessor.PreProcessor;
import loez.nllr.preprocessor.SimplePreprocessor;
import loez.nllr.userinterface.CommandLineInterface;
import loez.nllr.userinterface.UserInterface;

public class Main 
{

    public static void main( String[] args )
    {
        UserInterface ui = new CommandLineInterface();
        
        ArrayList<String> ppNames = new ArrayList<>();
        ppNames.add("");
        ppNames.add("simple");
        
        ArrayList<PreProcessor> pps = new ArrayList<>();
        pps.add(new SimplePreprocessor());
        pps.add(new SimplePreprocessor());
        
        ui.setupPreprocessors(pps, ppNames);
        
        ui.run();
    }
}
