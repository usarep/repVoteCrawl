
package common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import common.constants.CommonConstants;
/** 
 * @author amarnath
 */
public class StrUtil
{
	private static Logger logger = Logger.getLogger(StrUtil.class);
	private static final int MAX_CHARS = 256;
	private static final boolean[] WHITESPACE_LOOKUP_TABLE = new boolean[MAX_CHARS];
	
	private static final char[] WHITESPACES = { ' ', '\t', '\r', '\n' };  
	
	static {
		Arrays.fill(WHITESPACE_LOOKUP_TABLE, false);
		for(char whitepsaceChar: WHITESPACES) {
			WHITESPACE_LOOKUP_TABLE[(int) whitepsaceChar] = true;
		}
	}
	
	
	/* *****************************************************************************
	 *                         isEmpty()
	 *******************************************************************************/
    
	public static final String EMPTY_STR = "";
	
    public static boolean isEmpty(String s)
	{
	    return isNullOrWhitespace(s, false);
	}
    
    public static boolean isEmptyStrong(String s)
	{
	    return isNullOrWhitespace(s, true);
	}

    public static boolean isNullOrEmpty(String s) {
    	return s == null || s.length() == 0;
    }
    
	private static boolean isNullOrWhitespace(String str, boolean hardWorkTrim)
	{
		 if ( (str == null) || str.length() == 0)
			 return true;
		 
		String trimmedStr = null;
		
		if (hardWorkTrim) 
			trimmedStr = trim(str);
		else
			trimmedStr = str.trim();
		
		if (trimmedStr.length() <= 0)
			return true;
		else
			return false;

	}		public static String trim(String s)	{		if (s == null || s.length() == 0)			return s;				String result = removeLeadingSpaces(s);		result = removeTrailingSpaces(result);		return result;	}	
	public static int skipWhitespaces(String s, int index) {
		
		int length = s.length();
		while(index < length) {
			
			char c = s.charAt(index);
			if (!Character.isWhitespace(c))
				break;
		
			index++;
		}
		
		return index;
	}
		private static String removeLeadingSpaces(String s)	{		if (s == null || s.length() == 0)			return s;				for (int i = 0; i < s.length(); i++)		{			char c = s.charAt(i);						if (Character.isWhitespace(c) || isSpaceSeparator(c)  )				continue;			else				return s.substring(i);		}		return "";	}
	
	private static boolean isSpaceSeparator(char c)	{		if (Character.getType(c) == Character.SPACE_SEPARATOR)			return true;		else			return false;	}	private static String removeTrailingSpaces(String s)	{		if (s == null || s.length() == 0)			return s;				for (int i = s.length() -1; i >= 0; i--)		{			char c = s.charAt(i);						if (Character.isWhitespace(c) || isSpaceSeparator(c)  )				continue;			else				return s.substring(0, i+1);		}		return "";	}		public static String charValues(String s)	{		if (s == null || s.length() == 0)			return s;				StringBuffer buf = new StringBuffer();		for (int i = 0; i < s.length(); i++) 		{			if(i > 0)				buf.append(",");			buf.append(Character.getNumericValue(s.charAt(i)));		}		return buf.toString();	}		/** Return a trimmed string with weird space separator chars removed */	public static String cleanString(String s)	{		if (s == null || s.length() == 0)			return s;				StringBuffer buf = new StringBuffer();		for (int i = 0; i < s.length(); i++)		{			char c = s.charAt(i);						if (Character.isWhitespace(c))				buf.append(c);						else if (isSpaceSeparator(c) )				buf.append(" ");			else				buf.append(c);		}		String s2 = buf.toString();		return s2.trim();	}
	
	// remove all occurences of char ch from s
	public static String removeAll(String s, char ch)
	{
		if (s == null || s.length() == 0)
			return s;
		
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			
			if (c == ch)
				continue;
			else
				buf.append(c);
		}
		String s2 = buf.toString();
		return s2;
	}
	
	public static String double2String(double d)
	{
		StringBuffer result = new StringBuffer();
		result.append(d);
		return result.toString();
	}
	public static String genUniqueId()
	{
		double d = java.lang.Math.random();
		String uniqueId = double2String(d);
		return uniqueId;
	}
	public static boolean isSame(String str1, String str2)
	{
		if ( (str1 == null) && (str2 == null))	// 0,0
			return true;
		if ( (str1 == null) || (str2 == null))	// 0,1 and 1,0
			return false;
				if (str1.equalsIgnoreCase(str2))		// 1,1
			return true;
		else
			return false;
	}	
	// for Comparable -- str1.compareTo(str2)
	public static int compare(String str1, String str2, boolean userLowerCase)
	{
		if ( (str1 == null) && (str2 == null))	// 0,0
			return 0;
		else if ( (str1 == null))	// 0,1
			return -1;
		
		else if ( (str2 == null))	// 1,0
			return +1;
		
		else // 1,1
		{
			if (userLowerCase)
				return str1.compareToIgnoreCase(str2);
			else
				return str1.compareTo(str2);
		}
		
	}
	
	private static final class DecodeCharMap
	{
		static char[] in = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
			      'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
			      '0','1','2','3','4','5','6','7','8','9','_','&','#','%',' ', '-'};
	
		static char[] out = {'R','4','a','7','M','A','h','e','z','g','f','U','j','o','l','B','n','3','s','k','6','v','q','t','w',
			      'm','C','1','Z','b','F','D','9','E','G','c','I','8','H','K','2','L','d','N','O','Q','i','S','Y','p','T','V','X','y','x',
			      '0','W','r','P','_','J','&','#','u','%','5','@', '-'};
	}
	public static String encrypt(String str) 
	{
		StringBuffer buf = new StringBuffer();
		for(int i=0; i< str.length(); i++) 
		{
			char ch = str.charAt(i);
			for(int j=0; j< DecodeCharMap.in.length ; j++) 
			{
				if(ch == DecodeCharMap.in[j]) {
					buf.append(DecodeCharMap.out[j]);
	                break;
	            } 
			}  
		}
	    return buf.toString();  
	  }
	  
	  public static String decrypt(String str) 
	  {
		  StringBuffer buf = new StringBuffer();
	      for(int i=0; i< str.length(); i++) 
	      {
	         char ch = str.charAt(i);
	         
	         for(int j=0; j< DecodeCharMap.out.length ; j++) 
	         {
	            if(ch == DecodeCharMap.out[j]) 
	            {
	            	buf.append(DecodeCharMap.in[j]);
	                break;
	            } 
	         }  
	      }
	    return buf.toString();  
	  }
	public static String normalizedKey(String s) {		AssertUtil.ourAssertDebug(!isEmpty(s), "s is empty");		return s.trim().toLowerCase();	}
	
	/*
	 * append a / if dir does not end with / or \
	 */
	public static String normalizedDir(String dir)
	{
		if (dir == null) {
			AssertUtil.ourAssertDebug(false, "dir is null");
			return null;
		}
		String result = dir;
		if (!result.endsWith("/") && ! result.endsWith("\\"))
			result += "/";
		return result;
	}
	
	public static String normalizedUrlDir(String urlDir)
	{
		if (urlDir == null) {
			AssertUtil.ourAssertDebug(false, "urlDir is null");
			return null;
		}
		String result = urlDir;
		if (!result.endsWith("/"))
			result += "/";
		return result;
	}        public static Long now()     {    	long msec = Calendar.getInstance().getTimeInMillis();    	return new Long(msec);    }        /**     * Return true if s has an alpha numeric char, false otherwise     * @param s     * @return     */    public static boolean hasLetterOrDigit(String s)    {        if (isEmpty(s))            return false;        for (int i = 0; i < s.length(); i++)        {            char c = s.charAt(i);            if (Character.isLetterOrDigit(c))                return true;        }        return false;    }
    
    public static final String CONCAT_ID_SEP = "|";
    public static SortedSet concatString2Objects(String concatStringIds)
	throws AssertException, SQLException
	{
    	return concatString2Objects(concatStringIds, CONCAT_ID_SEP);
	}
    
    public static SortedSet concatString2Objects(String concatStringIds, String separator)
		throws AssertException, SQLException
	{
	    SortedSet result = new TreeSet();
	
		if (isEmpty(concatStringIds))
		{
		    return result;
		}
		String[] idArr = concatStringIds.split(separator);
		
		if (idArr == null || idArr.length == 0) 
		{
		    AssertUtil.ourAssertDebug(false, "idArr is empty, concatStringIds=" + concatStringIds);
		    return result;
		}
		Set uniqueIds = new HashSet();
		for (int i=0; i < idArr.length; i++)
		{
		    String idStr = idArr[i];
		    try {
		        int id = Integer.parseInt(idStr);
		        Integer idObj = new Integer(id);
		        // remove duplicates
		        boolean isNew = uniqueIds.add(idObj);
		        if (!isNew)
		            continue;   
		        result.add(idObj);
		    } catch (NumberFormatException e) {
		        logger.error(e.getMessage(), e);
		        continue;
		    }
		}
		return result;
	}
    
    public static String strArray2concatString(String[] strArray)
	{
    	return strArray2concatString(strArray, CONCAT_ID_SEP);
	}
    
    public static String strArray2concatString(String[] strArray, String separator)
	{
    	if (strArray == null || strArray.length == 0)
    		return null;
    	
	    StringBuffer buf = new StringBuffer();
	
		for (int i=0; i < strArray.length; i++)
		{
			if (i > 0)
				buf.append(separator);
		    buf.append(strArray[i]);
		}
		return buf.toString();
	}
    
    
    /*
     * sort an input csv string, and output a csv output string where the values are sorted in ascending order
     */
    public static String sortedAscendingCsvString(String inCsvString)
	{
	   
	    if (StrUtil.isEmpty(inCsvString))
	    	return inCsvString;
	    
	    String[] arr = inCsvString.split(",");
	    if (arr.length == 1) // only one element, nothing to sort
	    	return inCsvString;
	    
	
	    Arrays.sort(arr);
	    
	    StringBuilder buf = new StringBuilder();
	    boolean first = true;
	    for (int i=0; i < arr.length; i++)
	    {
	    	String next = arr[i];
	    	if (StrUtil.isEmpty(next))
	    		continue;
	    	else
	    	{
	    		if (!first)
	    			buf.append(",");
	    		buf.append(next);
	    		first = false;
	    	}
	    }
	    
	    return buf.toString();
	    
	}
    
    //  separate text-output with separator lines
    private static String separatorStr = "============================================";
    public static void separator()
    {
        System.out.println(separatorStr);
    }
    // print str after separator
    public static void separator(String str)
    {
        System.out.println(separatorStr);
        System.out.println(str);
    }
    
    public static int skipWhitespaces(char[] content, int start, int end) {
		for(int i = start; i < end; i++) {
			char ch = content[i];
			if (ch < MAX_CHARS) {
				if (!WHITESPACE_LOOKUP_TABLE[ch]) {
					return i;
				}
			}
		}
		
		return end;
    }
    
    public static int findWhitespace(char[] content, int start, int end) {
		for(int i = start; i < end; i++) {
			char ch = content[i];
			if (ch < MAX_CHARS) {
				if (WHITESPACE_LOOKUP_TABLE[ch]) {
					return i;
				}
			}
		}
		
		return -1;
    }
    
    public static String collapseWhitespaces(String text, int start, int length) {
    	
    	StringBuilder buffer = new StringBuilder(length);
    	
    	char[] content = text.toCharArray();
    	int end = start + length;
		for(int i = start; i < end; i++) {
			char ch = content[i];
			if (ch == ' ' || ch == '\t' || ch == '\n') {
				i++;
				buffer.append(content, start, i-start);
				
				i = skipWhitespaces(content, i, end);
				start = i;
			}
		}
		
		if (start < end) {
			buffer.append(content, start, end-start);
		}
		
		return buffer.toString();
    }
    
    /*
     * create a short string, e.g., for displaying in a link or a tool tip
     */
    public static String createShortString(String text, int max, String defaultEmptyString) 
	{
		if (StrUtil.isEmpty(text)) {
			return defaultEmptyString;
		}
		String text2Use = text.trim();
		
    	if (text2Use.length() > max)
    		return text2Use.substring(0, max) + "...";
    	else
    		return text2Use;
    }
    
    public static String createShortString(String text, String defaultEmptyString)
    {
    	return createShortString(text, 50, defaultEmptyString);
    }
    
    public static String concat(String[] tokens, char ch) {
    	
    	if (tokens == null || tokens.length == 0)
    		return null;
    	
    	StringBuilder buf = new StringBuilder();    	
    	
    	int length = tokens.length;
    	for(int i = 0; i < length; i++) {
    		if (i > 0)
    			buf.append(", ");
    		buf.append(tokens[i]);
    	}
    	
    	return buf.toString();
    }
    
    public static String truncateAtWordBoundary(String s, int max) {
    	
    	int len;
    	if (s == null || (len = s.length()) == 0)
    		return null;
    	
    	if (len < max)
    		return s;
    	
    	if (Character.isWhitespace(s.charAt(max)))
    		return s.substring(0, max);
    
    	int i = max;
    	for(; i > 0; i--) {    		
    		if (Character.isWhitespace(s.charAt(i)))
    			break;
    	}
    	
    	if (i > 0)
    		return s.substring(0, i-1);
    	
    	return s.substring(0, max);
    }
    
    public static String[] split(String s) {
    	
    	if (isEmpty(s)) {
    		return null;
    	}
    	StringTokenizer st = new StringTokenizer(s, ",");
    	int count = st.countTokens();
    	String[] str = new String[count];
    	int j = 0;
    	for (int i=0; i<count ; i++) {
    		String token = st.nextToken();
    		if (!isEmpty(token)) {
    			str[j++] = token;
    		}
    	}
    	return str;
    }
    
    public static Set<String> splitIntoSet(String s) {
    	
    	if (isEmpty(s)) {
    		return null;
    	}
    	StringTokenizer st = new StringTokenizer(s, ",");
    	int count = st.countTokens();
    	
    	Set<String> hashSet = new HashSet<String>();
    	for (int i = 0; i < count ; i++) {
    		String token = st.nextToken();
    		if (!isEmpty(token)) {
    			hashSet.add(token);
    		}
    	}
    	return hashSet;
    }

    public static String concat(List<String> list) {
    	return concat(list, ",");
    }
    
    public static String concat(List<String> list, String comma) {
    	
		if (list == null || list.size() == 0)
			return null;
		
		String s = null;
		if (list.size() == 1) {
			s = list.get(0);
		} else {
		
			boolean first = true;
			StringBuilder buf = new StringBuilder(); 
			for(String e: list) {
				if (!first)
					buf.append(", ");
				
				buf.append(e);
				first = false;
			}
			
			s = buf.toString();
		}
		
		return s;
    }
    
    /* 
     * use DomainHtmlCleaner.removeAllHtml(String) or BaseSearchBo.removeAllHtml() instead
    public static String escapeHtml(String inStr)
	{
		if (inStr == null)
			return null;
		String s = inStr.replaceAll("&", " "); // not needed for indexing
		s = s.replaceAll("<", "&lt;");
		s = s.replaceAll(">", "&gt;");
		return s;
	}
	*/
    
    /**
     * concat two csv strings into a new csv string
     * @param csv1
     * @param csv2
     * @return
     */
    public static String concatCsv(String csv1, String csv2)
	{
		// 0,0
		if (StrUtil.isEmpty(csv1) && StrUtil.isEmpty(csv2))
			return "";
		
		// 0,1
		if (StrUtil.isEmpty(csv1))
			return csv2;
		
		// 1,0
		if (StrUtil.isEmpty(csv2))
			return csv1;
		
		// 1,1
		StringBuilder buf = new StringBuilder();
		buf.append(csv1).append(",").append(csv2);
		return buf.toString();
	}
    
    public static String urlDecodeNoException(String s)
    {
    	return urlDecodeNoException(s, CommonConstants.ENCODING_UTF8);
    }
    
    public static String urlDecodeNoException(String s, String encoding)
    {
    	if (isEmpty(s))
    		return s;
    	
    	String result = null;
    	try
    	{
    		result = URLDecoder.decode(s, encoding);
    	} catch (UnsupportedEncodingException e) {
    		logger.error(e.getMessage(), e);
    		result = s;
    	}
    	return result;
    }
    
    public static String urlEncodeNoException(String s, String encoding)
    {
    	if (isEmpty(s))
    		return s;
    	
    	String result = null;
    	try
    	{
    		result = URLEncoder.encode(s, encoding);
    	} catch (UnsupportedEncodingException e) {
    		logger.error(e.getMessage(), e);
    		result = s;
    	}
    	return result;
    }
    public static String urlEncodeNoException(String s)
    {
    	return urlEncodeNoException(s, CommonConstants.ENCODING_UTF8);
    }
    
    // replace + with %20
    public static String urlEncodeJSEquivalentNoException(String s, String encoding)
    {
    	if (isEmpty(s))
    		return s;
    	
    	String result = null;
    	try
    	{
    		result = URLEncoder.encode(s, encoding);
    		if (result != null)
    			result = result.replaceAll("\\+", "\\%20");
    	} catch (UnsupportedEncodingException e) {
    		logger.error(e.getMessage(), e);
    		result = s;
    	}
    	return result;
    }
    public static String urlEncodeJSEquivalentNoException(String s)
    {
    	return urlEncodeJSEquivalentNoException(s, CommonConstants.ENCODING_UTF8);
    }
    
} // Util