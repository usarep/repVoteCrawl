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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
/** 
 * @author amarnath
 */
public class ObjUtil
{
	/* *****************************************************************************
	 *                         isEmpty()
	 *******************************************************************************/
   	public static boolean isEmpty(Object[] arr) 	{	    if (arr == null || arr.length == 0)	        return true;	    else	        return false;	}
	public static boolean isEmpty(Collection c)
	{
		if ( (c == null) || c.isEmpty() )
			return true;
		else
			return false;
	}
	public static boolean isEmpty(Vector v)
	{
		if ( (v == null) || v.isEmpty() )
			return true;
		else
			return false;
	}
	public static boolean isEmpty(AttributeSet s)
	{
		if (s == null)
			return true;
		else if (s instanceof SimpleAttributeSet)
		{
			if ( ((SimpleAttributeSet) s).isEmpty())
				return true;
		}
		return false;	}
	/*
	public static boolean isEmpty(Set s)
	{
		if ( (s == null) || s.isEmpty() )
			return true;
		else
			return false;
	}
	*/
	public static boolean isEmpty(Map m)
	{
		if ( (m == null) || m.isEmpty() )
			return true;
		else
			return false;
	}
   
	public static boolean isEqual(Object one, Object two)
	{
		//0,0
		if (one == null && two == null)
			return true;
		// 0,1 and 1,0
		if (one == null || two == null)
			return false;
		//1,1
		
		return one.equals(two);
	}
	
	/*
	 * return true if both objects are non null and equal
	 */
	public static boolean isEqualNonNull(Object one, Object two)
	{
		//0,0, 0,1 and 1,0
		if (one == null || two == null)
			return false;
		//1,1
		
		return one.equals(two);
	}
	
	// return true if Sets x and y have the same membership
	public static boolean haveSameMembership(Set x, Set y)
	{
		if (isEmpty(x) && isEmpty(y) ) // 0,0
			return true;
		else if (isEmpty(x) || isEmpty(y) ) // 0,1 or 1,0
			return false;
		
		// 1,1
		if (x.size() != y.size())
			return false;
		
		if (x.containsAll(y))
				return true;
		else
			return false;
	}
	
	/**
	 * returns true if o1.equals(o2)
	 * if o1 and o2 are both null, returns true
	 */
	public static boolean isSame(Object o1, Object o2)
	{
		if ( (o1 == null) && (o2 == null))	// 0,0
			return true;
		if ( (o1 == null) || (o2 == null))	// 0,1 and 1,0
			return false;
		
		if (o1.equals(o2))		// 1,1
			return true;
		else
			return false;
	}
	
	/*
	 * similar to isSame() except also tests ignoreCase if objects are strings
	 */
	public static boolean isSameIgnoreCase(Object o1, Object o2)
	{
		if ( (o1 == null) && (o2 == null))	// 0,0
			return true;
		if ( (o1 == null) || (o2 == null))	// 0,1 and 1,0
			return false;
		
		// 1,1 strings
		if (o1 instanceof String && o2 instanceof String)
		{
			String s1 = (String) o1;
			String s2 = (String) o2;
			return s1.equalsIgnoreCase(s2);
			
		}
		
		// 1,1 non strings
		if (o1.equals(o2))
			return true;
		else
			return false;
	}
	
	/**
	 * returns true if o1.equals(o2)
	 * if o1 and o2 are both null, returns false
	 */
	public static boolean isSameNonNull(Object o1, Object o2)
	{
		if ( (o1 == null) && (o2 == null))	// 0,0
			return false;
		if ( (o1 == null) || (o2 == null))	// 0,1 and 1,0
			return false;
		
		if (o1.equals(o2))		// 1,1
			return true;
		else
			return false;
	}

	public static int compareTo(Comparable o1, Comparable o2)
	{
		// 0,0
		if (o1 == null && o2 == null)
			return 0;
		
		// 0,1
		if (o1 == null)
			return +1;
		
		// 1,0
		if (o2 == null)
			return -1;
		
		// 1,1
		return o1.compareTo(o2);
	}
  
} // ObjUtil