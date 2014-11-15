package repVote.billinfo;

import java.util.Set;

import common.util.ObjUtil;
import common.util.StrUtil;
import repVote.billinfo.HouseBillsPageCrawler.Params;

public class BillRollCall
{
	public static final String SEPARATOR = ";";
	Integer id; // database primary key
	int rollCallNum;
	String billUrl;
	String billUrlText;
	Params params;
	
	String crsHref;
	String indexTermsHref;
	
	String crsSummary;
	
	Set<Link> indexTerms;
	
	/**
	 * returns index terms as a csv String. separator is a colon because data may have commas
	 */
	public String getCsvIndexTerms()
	{
		if (ObjUtil.isEmpty(indexTerms))
			return null;
		
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Link link : indexTerms)
		{
			if (link == null || StrUtil.isEmpty(link.text))
				continue;
			
			if (!first)
				sb.append(SEPARATOR);
			sb.append(link.text.trim());
			first = false;
		}
		
		if (sb.length() == 0)
			return null;
		else
			return sb.toString();
		
	}
	
	
	
}