package loez.nllr.userinterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import loez.nllr.algorithm.Argmax;
import loez.nllr.algorithm.Nllr;
import loez.nllr.datastructure.ArrayList;
import loez.nllr.domain.Corpus;
import loez.nllr.domain.Document;
import loez.nllr.preprocessor.PreProcessor;
import loez.nllr.preprocessor.SimplePreprocessor;
import loez.nllr.preprocessor.exception.StemmerCreationException;
import loez.nllr.reader.CorpusReader;

/**
 * A command line interface for the NLLR-application
 * @author ljleppan
 */
public class CommandLineInterface implements UserInterface{
    private final Scanner in = new Scanner(System.in);
    private DateFormat dateFormat;
    private ArrayList<String> preProcessorNames;
    private ArrayList<PreProcessor> preProcessors;
    private PreProcessor preProcessor;
    private CorpusReader corpusReader;
    private Corpus referenceCorpus;
    private ArrayList<Object> timePartitions;
    private Nllr nllr;
    
    private static final String DEFAULT_DATE_STRING = "d-MMM-yyyy";
    
    @Override
    public void setupPreprocessors(ArrayList<PreProcessor> preProcessors, ArrayList<String> preProcessorNames){
        this.preProcessors = preProcessors;
        this.preProcessorNames = preProcessorNames;
    }

    @Override
    public void run() {
        getDateFormat(); 
        getPreprocessor();
        getLanguage();
        getCorpusReader();
        getReferenceCorpus();
        getTimePartitionSize();
        processTimePartitions();
        setupNllr();
        
        printCommands();
        while(true){
            printCommandPrompt();
            String input = in.nextLine();
            
            if (input.equals("quit")){
                return;
            }
            if (input.equals("help")){
                printCommands();
            }
            if (input.equals("random")){
                processRandom();
            }
            if (input.equals("in")){
                processInput();
            }
        }
    }

    private void getDateFormat() {
        String dateFormatString = queryFor("Set date format:");
        if (dateFormatString.isEmpty()){
            dateFormatString = DEFAULT_DATE_STRING;
        }
        dateFormat = new SimpleDateFormat(dateFormatString, Locale.US);
    }
    
    private void getPreprocessor(){       
        printPreProcessorPrompt();
        printCommandPrompt();
        String preprocessorString = in.nextLine();
        
        while (!preProcessorNames.contains(preprocessorString.toLowerCase().trim())){
            System.out.println("\tNo such preprocessor. ");
            printPreProcessorPrompt();
            preprocessorString = in.nextLine();
        }
        
        int index = preProcessorNames.indexOf(preprocessorString);
        preProcessor = preProcessors.get(index);
    }

    private void printPreProcessorPrompt() {
        System.out.print("Set preprocessor ["+preProcessorNames.get(0));
        for (int i = 1; i < preProcessorNames.size(); i++) {
            System.out.print(", "+preProcessorNames.get(i));
        }
        System.out.println("]: ");
    }
    
    private void getLanguage() {
        if (!(preProcessor instanceof SimplePreprocessor)){
            String language = queryFor("Set language:");
            try {
                preProcessor.setLanguage(language);
            } catch (StemmerCreationException e){
                System.out.println("\tSomething went wrong, attempting to use default language ...");
            }
            System.out.println("\tLanguage set to "+preProcessor.getLanguage());
        }
    }
    
    private void getCorpusReader() {
        corpusReader = new CorpusReader();
    }
    
    private void getReferenceCorpus(){
        String referenceCorpusPath = queryFor("Set path to reference corpus:");
        processReferenceCorpus(referenceCorpusPath);        
    }

    private void processReferenceCorpus(String referenceCorpusPath) {
        System.out.println("\tProcessing reference corpus (this might take long) ... ");
        referenceCorpus = corpusReader.readCorpus(referenceCorpusPath, dateFormat, preProcessor);
        System.out.println("\tDone processing reference corpus. \n");
    }
    
    private void getTimePartitionSize(){
        printTimePartitionSizePrompt();
        printCommandPrompt();
        String input = in.nextLine();
        while (!input.toLowerCase().trim().equals("daily")){
            System.out.println("Invalid time partition size.");
            printTimePartitionSizePrompt();
            input = in.nextLine();
        }        
    }
    
    private void printTimePartitionSizePrompt() {
        System.out.println("Set time partition size [daily]: ");
    }

    private void processTimePartitions(){
        timePartitions = new ArrayList<>();
        
        Calendar startDate = (Calendar) referenceCorpus.getStartDate().clone(); 
        Calendar endDate = (Calendar) referenceCorpus.getEndDate().clone();
        
        System.out.println("Getting time partitions for the reference corpus spanning "+ dateFormat.format(startDate.getTime()) + " to " + dateFormat.format(endDate.getTime())+" :");
        Calendar partitionStartDate = (Calendar) referenceCorpus.getStartDate().clone();
        clearDate(partitionStartDate);
        
        Calendar partitionEndDate = (Calendar) referenceCorpus.getStartDate().clone();
        partitionEndDate.add(Calendar.DAY_OF_MONTH, 1);
        clearDate(partitionEndDate);
        
        while (!endDate.before(partitionEndDate)){
            processSingleTimepartition(partitionStartDate, partitionEndDate);

            partitionStartDate.add(Calendar.DAY_OF_MONTH, 1);
            partitionEndDate.add(Calendar.DAY_OF_MONTH, 1);
        }
        System.out.println("Done building time partitions.\n");
        
    
    }

    private void clearDate(Calendar date) {
        date.clear(Calendar.HOUR);
        date.clear(Calendar.MINUTE);
        date.clear(Calendar.SECOND);
    }
    
    private void processSingleTimepartition(Calendar partitionStartDate, Calendar partitionEndDate){     
        System.out.println("\t"+dateFormat.format(partitionStartDate.getTime()) + " - " +dateFormat.format(partitionEndDate.getTime()));        
        Corpus timePartition = referenceCorpus.getTimePartition(partitionStartDate, partitionEndDate);
        
        if (timePartition != null){
                timePartitions.add(timePartition);
        }
    }
    
    private void setupNllr(){
        nllr = new Nllr(referenceCorpus);
    }
    
    private void printCommands(){
        System.out.println("Known commands:");
        System.out.println("\trandom -- Processes a random document from the Reference corpus.");
        System.out.println("\tin     -- Input custom text for processing.");
        System.out.println("\tquit   -- Quits the application.");
        System.out.println("\thelp   -- Shows this help.");
    }
    
    private String queryFor(String query){
        System.out.println(query);
        printCommandPrompt();
        String command = in.nextLine();
        return command;
    }
    
    private void printCommandPrompt(){
        System.out.print(" > ");
    }
    
    private void processRandom(){
        int referenceCorpusSize = referenceCorpus.getDocuments().size();
        int randomDocumentId = new Random().nextInt(referenceCorpusSize);
        Document document = referenceCorpus.getDocuments().get(randomDocumentId);
        
        System.out.println("Random document #"+randomDocumentId + " (real date "+dateFormat.format(document.getDate().getTime())+ ")");
        processDocument(document);
    }
    
    private void processInput(){
        String raw = queryFor("Input text body:");
        
        String body = preProcessor.process(raw);
        System.out.println("Processed text:");
        System.out.println("\t"+body);
        
        Document document = new Document(body);
        processDocument(document);
    }
    
    private void processDocument(Document document){
        System.out.print("Processing document ... ");
        
        Object[] argMaxArgs = {document};
        Object[] result = Argmax.single(nllr, timePartitions, argMaxArgs);
        
        Corpus resultCorpus = (Corpus) result[0];
        double resultNllr = (double) result[1];
        
        System.out.println(" done.");
        printResult(document, resultCorpus, resultNllr);
    }
        
    private void printResult(Document document, Corpus resultCorpus, double resultNllr){
        String docDate = "UNKNOWN";
        if (document.getDate() != null){
            docDate = dateFormat.format(document.getDate().getTime());
        }
        
        String corpStartDate = "UNKNOWN";
        if (resultCorpus != null && resultCorpus.getStartDate() != null){
            corpStartDate = dateFormat.format(resultCorpus.getStartDate().getTime());
        }
        
        String corpEndDate = "UNKNOWN";
        if (resultCorpus != null && resultCorpus.getEndDate() != null){
            corpEndDate = dateFormat.format(resultCorpus.getEndDate().getTime());
        }
        
        StringBuilder out = new StringBuilder();
        out.append("Most likely time partition:\n\t");
        out.append(corpStartDate);
        out.append(" - ");
        out.append(corpEndDate);
        out.append(" : ");
        out.append(resultNllr);
        out.append("\n");
        
        System.out.println(out.toString());
    }
}