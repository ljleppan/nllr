package loez.nllr.algorithm;

import loez.nllr.datastructure.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author loezi
 */
public class ArgmaxTest {

    public class Same implements Algorithm{
        @Override
        public double calculate(Object[] args){
            return ((double) args[0]) / 2;
        }
    }
    
    @Test
    public void argmaxTest(){
        ArrayList<Object> args = new ArrayList<>();
        args.add(2.0);
        args.add(1.0);
        args.add(1.5);
        
        Object[] constants = new Object[]{1};
        
        Object[] max = Argmax.single(new Same(), args, constants);
        
        assertEquals("argmax should return the argument that gave max value in the first place", 
                2.0, (double) max[0], 0.001);
        
        assertEquals("argmax should return the maximum value in the second place",
                1.0, (double) max[1], 0.001);
    }
    
}
