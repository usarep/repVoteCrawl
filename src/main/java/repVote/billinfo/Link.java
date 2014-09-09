package repVote.billinfo;

import common.util.StrUtil;

public class Link implements Comparable
{
	Integer id; // primary key
	String url;
	String text;
	
	public boolean isEqual(Object other)
	{
		Link o = (Link) other;
		
		// 0,0
		if (StrUtil.isEmpty(url) && StrUtil.isEmpty(o.url))
			return true;
		
		// 0,1 and 1,0
		else if (StrUtil.isEmpty(url) || StrUtil.isEmpty(o.url))
			return false;
		
		else
			return url.trim().equalsIgnoreCase(o.url.trim());
	}
	
	public int hashCode()
	{
		if (url != null)
			return url.trim().toLowerCase().hashCode();
		
		else
			return 0;
	}
	
	public int compareTo(Object other) 
	{
		Link o = (Link) other;
		
		// 0,0
		if (StrUtil.isEmpty(url) && StrUtil.isEmpty(o.url))
			return 0;
		
		// 0,1
		else if (StrUtil.isEmpty(url))
			return +1;
		
		// 1,0
		else if (StrUtil.isEmpty(o.url))
			return -1;
		
		else
			return url.trim().compareToIgnoreCase(o.url.trim());
		
	}
}