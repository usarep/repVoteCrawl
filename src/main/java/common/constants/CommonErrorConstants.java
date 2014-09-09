package common.constants;

public class CommonErrorConstants 
{
	/*
	 * these are for mysql 5.0
	 * 
	 * http://dev.mysql.com/doc/refman/5.0/en/error-messages-server.html
	 */
	public static class MySQLConstants
	{
		public static final int EMPTY_DATA = 1329;
		
		/*
		 * if a group concat leads to truncation, see
		 * 		http://bugs.mysql.com/bug.php?id=8681
		 * 		http://bugs.mysql.com/bug.php?id=27689
		 */
		public static final int GROUP_CONCAT_TRUNCATION = 1260;
		
		/*
		 * http://dev.mysql.com/doc/refman/5.0/en/error-messages-server.html
		 * Error: 1205 SQLSTATE: HY000 (ER_LOCK_WAIT_TIMEOUT)
		 * Message: Lock wait timeout exceeded; try restarting transaction 
		 */
		public static final int LOCK_WAIT_TIMEOUT = 1205;
		
		public static final int EMPTY_UPDATE = 1000;
	}

}
