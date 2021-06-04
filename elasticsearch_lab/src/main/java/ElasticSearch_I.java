import partc.ExampleQuery;
import partc.Q1;
import partc.Q2;
import partc.Q3;
import partc.Q4;
import partc.Q5;
import partc.Q6;
import partc.Q7;
import partc.Q8;

/**
 * Hello world!
 *
 */
public class ElasticSearch_I {

    public static void main( String[] args ) throws Exception {
		
    	if (args.length != 1) {
			throw new Exception("Wrong number of parameters, usage: <example | q1 | q2 | q3 | q4 | q5 | q6 | q7 | q8>)");
		}
    	String q = args[0];
    	
    	if (q.equals("example")) {
    		ExampleQuery.execute();
    	} else if (q.equals("q1")) {
    		Q1.execute();
    	} else if (q.equals("q2")) {
    		Q2.execute();
    	} else if (q.equals("q3")) {
    		Q3.execute();
    	} else if (q.equals("q4")) {
    		Q4.execute();
    	} else if (q.equals("q5")) {
    		Q5.execute();
    	} else if (q.equals("q6")) {
    		Q6.execute();
    	} else if (q.equals("q7")) {
    		Q7.execute();
    	} else if (q.equals("q8")) {
    		Q8.execute();
    	}
    }
}
