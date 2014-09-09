/**
 * 
 */
package repVote.db;

/**
 * @author amLaptop
 *
 */
public class RepVoteDbConstants
{
	public static final String PARAM_DB_HOST = "db-host";
	public static final String PARAM_DB_URL = "db-url";
	public static final String PARAM_DB_NAME = "db-name";
	public static final String PARAM_DB_USER = "db-user";
	public static final String PARAM_DB_PASSWORD = "db-password";
	
	public static final String RE_INITIALIZE = "reinitialize";
    public static final String DEFAULT_DB_NAME = "repvote";
    public static final String DEFAULT_DB_USER = "repvote";
    public static final String DEFAULT_DB_PASSWORD = "repvote123";
    public static final String DEFAULT_DB_USER_PART = "repvote";
    public static final String DEFAULT_DB_HOST = "localhost";
    public static final String DEFAULT_DB_URL = "jdbc:mysql://localhost/repvote?zeroDateTimeBehavior=convertToNull";
    
    // research specific
    
    /*
     * mysql max index length == 1000 characters for myisam, ~750 for innodb.
     * for unique index on urls, need to split urls into two tables.
     * see Version1.java
     */
    public static final int URL_SMALL_MAX_LEN = 10;

}
