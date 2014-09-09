/**
 * 
 */
package common.config;

import common.util.StrUtil;

/**
 * 
 * @author amLaptop
 *
 */
public class DbConfig
{
	public static final String DB_URL_KEY = "db.url";
	
    private String dbUrl = null; // e.g., "jdbc:mysql://localhost/joke"
    private String dbName; // e.g. joke
    private String dbUserName;
    private String dbPassword;
    private String dbHost; // e.g., localhost -- include port in necessary
    
    private String dbRootPassword; // set this only if it's necessary to pass root password
    
	public String getDbHost() {
		return dbHost;
	}
	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	
	public String getDbUserName() {
		return dbUserName;
	}
	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}
	
//	 e.g., "jdbc:mysql://localhost/joke"
	private static final String JDBC_MYSQL = "jdbc:mysql://";
	public String getDbUrl() {
		if (!StrUtil.isEmpty(dbUrl))
			return dbUrl;
		
		if (StrUtil.isEmpty(dbHost) || StrUtil.isEmpty(dbName))
			return null;
		
		// http://forums.mysql.com/read.php?39,44913,44913 -- change zero valued timestamps to null instead of throwing exception
		return JDBC_MYSQL + dbHost + "/" + dbName + "?zeroDateTimeBehavior=convertToNull";
	}
	public String getDbRootPassword() {
		return dbRootPassword;
	}
	public void setDbRootPassword(String dbRootPassword) {
		this.dbRootPassword = dbRootPassword;
	}
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	
	/**
	 * returns true if any one of the following is empty
	 *  dbName
	 *  dbUserName
	 *  dbPassword
	 *  dbHost
	 *  
	 *  dbPassword can be "" but not null.
	 */
	public boolean isEmpty()
	{
		if (StrUtil.isEmpty(dbName) || 
				StrUtil.isEmpty(dbUserName) ||
				dbPassword == null ||
				StrUtil.isEmpty(dbHost) )
			
			return true;
		else
			return false;
	}
    
}