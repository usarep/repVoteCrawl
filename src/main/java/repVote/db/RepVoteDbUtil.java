/**
 * 
 */
package repVote.db;

import common.config.DbConfig;

/**
 * @author am
 *
 */
public class RepVoteDbUtil 
{

	public static RepVoteDbUtil o = new RepVoteDbUtil();
	protected RepVoteDbUtil() {}
	
	public DbConfig createDbConfig()
	{
		DbConfig result = new DbConfig();
		result.setDbHost(RepVoteDbConstants.DEFAULT_DB_HOST);
		result.setDbName(RepVoteDbConstants.DEFAULT_DB_NAME);
		result.setDbPassword(RepVoteDbConstants.DEFAULT_DB_PASSWORD);
	
		result.setDbUserName(RepVoteDbConstants.DEFAULT_DB_USER);
	
		result.setDbUrl(RepVoteDbConstants.DEFAULT_DB_URL);
		
		return result;
	
	}
}
