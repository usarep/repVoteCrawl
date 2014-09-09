/*
 * This class is originally Knoltex code, transferred
 * to 1255 Patterns in 2002, to Clarifyre in 2005, and
 * to Vugle in 2006.
 * 
 * It's made available to UrlTrails as part of the
 * Shared Framework Agreement which states that 
 * it's owned by the original owners, but UrlTrails
 * has a perpetual royalty free license to use it
 * in all of its products, and modify it as
 * necessary. 
 * 
 * Created: 1999.
 * Copyright Vugle, 1999-2007.
 * 
 */
package common.util;

/**
 * @author amarnath
 */
public class DebugException extends Exception
{
	public DebugException()
	{
		super();
	}
	
	public DebugException(String str)
	{
		super(str);
	}
}