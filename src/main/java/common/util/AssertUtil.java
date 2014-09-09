
package common.util;

import org.apache.log4j.Logger;
/** 
 * @author amarnath
 */
public class AssertUtil
{

	/* ***************************************************************************
	 *                               assert
	 *****************************************************************************/
	// assertDebug() catches and prints debug message instead of throwing an exception
	public static void ourAssertDebug(boolean b, String errMsg)
	{
		try {
			if (b == false)
				throw new DebugException(errMsg);
		} catch (DebugException e)
		{
			e.printStackTrace();
		}
	}
	public static void ourAssertDebug(boolean b)
	{
		ourAssertDebug(b, "");
	}
	public static void ourAssertDebug(boolean b, String errMsg, Logger logger)
	{
		try {
			if (b == false)
				throw new DebugException(errMsg);
		} catch (DebugException e)
		{
			logger.error(e.getMessage(), e);
		}
	}

	// ***************** AssertException **********************
	public static void ourAssert(boolean b, String errMsg)
		throws AssertException
	{
		if (b == false)
			throw new AssertException(errMsg);
		
	}
	public static void ourAssert(boolean b)
		throws AssertException
	{
		ourAssert(b, "");
	}
	
	//	 ***************** IllegalArgumentException **********************
	public static void assertArg(boolean b, String errMsg)
		throws java.lang.IllegalArgumentException
	{
		if (b == false)
			throw new java.lang.IllegalArgumentException(errMsg);
		
	}
	public static void assertArg(boolean b)
		throws java.lang.IllegalArgumentException
	{
		assertArg(b, "");
	}
	
	// during debug, sometimes need to skip throwing exceptions 
	public static void assertArgSkip(boolean b, String errMsg)
	{
		// no op
	}
		
} // Util