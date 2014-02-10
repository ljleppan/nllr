package loez.nllr.algorithm;

import loez.nllr.datastructure.ArrayList;

/**
 * An implementation of the argmax algorithm.
 * @author loezi
 */
public class Argmax {
    
    /**
     * Calculates argmax for given algorithm and arguments.
     * The method call made will be algorithm(arg, constants) where arg is from args and constants is the array of constants.
     * @param algorithm Algorithm
     * @param args      A list of changing arguments
     * @param constants An array of contants.
     * @return
     */
    public static Object[] single(Algorithm algorithm, ArrayList<Object> args, Object[] constants){
        double maxVal = Double.MIN_VALUE;
        Object maxArg = null;
        Object[] argList = new Object[constants.length + 1];
        System.arraycopy(constants, 0, argList, 1, constants.length);
        
        for (Object arg : args){
            argList[0] = arg;
            double result = algorithm.calculate(argList);
            if (maxVal < result){
                maxVal = result;
                maxArg = arg;
            }
        }
        
        Object[] max = {maxArg, maxVal};
        return max;
    }
}
