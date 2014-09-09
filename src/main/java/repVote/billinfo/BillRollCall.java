package repVote.billinfo;

import java.util.Set;

import repVote.billinfo.HouseBillsPageCrawler.Params;

public class BillRollCall
{
	Integer id; // database primary key
	int rollCallNum;
	String billUrl;
	String billUrlText;
	Params params;
	
	String crsHref;
	String indexTermsHref;
	
	String crsSummary;
	
	Set<Link> indexTerms;
	
	
	
}