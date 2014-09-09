
package repVote.billinfo;





/**
 * parses command line arguments.
 * 
 * @author amukhe1
 *
 */
public class Arguments
{

		
	
	// url
	String url = null;
	
	int year;
	
	
	private boolean error = false;
	
	
	
	public Arguments() {}

	public static Arguments newInstance(String[] args) 
    {
		Arguments result = new Arguments();
		
		if (args == null || args.length == 0) {
			result.usage();
			return result;
		}
    	
    	
    	int index = 0;
        while (index < args.length) 
        {
        	boolean match = false;
        	String nextArg = args[index].trim().toLowerCase();
        	
        	if (nextArg.startsWith("-u")) {
        		match = true;
        		result.url = args[index+1];
        		index+=2;
        		continue; // still need to check for solr server
        	}

        	if (nextArg.startsWith("-y")) {
        		match = true;
        		result.year = Integer.parseInt(args[index+1]);
        		index+=2;
        		continue;
        	}
        	
        	

        	if (!match) {
        		System.err.println("Invalid argument: " + args[index]);
        		result.usage();
        	}
        	
        } /* while */
        
       
        
        
        return result;	
    }
    
    private void usage() 
    {
    	error = true;
    	System.out.println("Usage: java mainClass [-u url] [-y year]  ");
    }
    
    private void usage(String s) {
    	error = true;
    	System.out.println(s);
    }

	public String getUrl() {
		return url;
	}

	public int getYear() {
		return year;
	}

	public boolean isError() {
		return error;
	}
    
   

}