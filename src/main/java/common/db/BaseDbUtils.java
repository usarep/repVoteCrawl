/**
 * 
 */
package common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;

import common.config.DbConfig;
import common.constants.CommonErrorConstants;
import common.util.AssertUtil;
import common.util.ObjUtil;
import common.util.StrUtil;

/**
 * Common db functions. 
 * since DomainUtil is not in commonOne, there is no thread local support in this class.
 * for thread-local support, use DbUtils directly.
 * 
 * DbUtils extends this class, so in general, best to call via DbUtils.
 * 
 *  @author tomi/amLapTop
 *
 */
public class BaseDbUtils
{
	private static Logger logger = Logger.getLogger(BaseDbUtils.class);
	public static BaseDbUtils o = new BaseDbUtils();
	protected BaseDbUtils() {}
	
	public void close(Statement stmt) {
        try {
            if (stmt != null)
                stmt.close();
        } catch (SQLException e) {}
    }
    
    public void close(Connection con) {
        try {
            if (con != null)
                con.close();
        } catch (SQLException e) {}
    }
    
    /**
     * close the connection if it's not a thread local
     * @param connectionWrapper
     */
    public void closeIfNonTL(ConnectionWrapper connectionWrapper)
    {
        try 
        {
        	if (connectionWrapper == null || connectionWrapper.isThreadLocal())
        		return;
        	
        	// non thread local connection. may be null
        	Connection con = connectionWrapper.getConnection();
            if (con != null)
                con.close();
            
        } catch (SQLException e) {}
    }

    public void close(ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException e) {}
    }

    public void close(Statement stmt, Connection con) {
        close(stmt);
        close(con);
    }

    public void close(ResultSet rs, Statement stmt, Connection con) {
        close(rs);
        close(stmt);
        close(con);
    }
    
    public void close(ResultSet rs, Statement stmt) {
        close(rs);
        close(stmt);
    }
    
    public void rollback(Connection con) {
        try {
            if (con != null) {
            	con.rollback();
            }
        } catch (SQLException e) {}
    }
    
    /**
     * Returns a new non-thread-local connection
     * @param url
     * @param userName
     * @param password
     * @return
     */
    
    public Connection getConnectionNonTL(String url, String userName, String password)
	{
    	int xyz=1;
		try {
			Connection c = 
			    DriverManager.getConnection(url, userName, password);
			c.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			return c;
		} catch (SQLException e) {
			// logger.error(e.getMessage(), e);
			e.printStackTrace();
			return null;
		}
	}
    
    public Connection getConnectionNonTL(DbConfig dbConfig)
    {
    	AssertUtil.assertArg(dbConfig != null && !dbConfig.isEmpty(), "dbConfig is empty");
    	
    	// 9/2010: http://forums.mysql.com/read.php?39,44913,44913 -- zero timestamp to be returned as null instead of throwing exception
    	String url = "jdbc:mysql://" + dbConfig.getDbHost() + "/" + dbConfig.getDbName() + "?zeroDateTimeBehavior=convertToNull";
    	return getConnectionNonTL(url, dbConfig.getDbUserName(), dbConfig.getDbPassword());
    }
    
    /*
	 * set a Long value, checking for null.
	 */
	public void setLong(PreparedStatement pstmt, Long value, int pos) throws SQLException
	{
		if (pstmt == null) {
			AssertUtil.ourAssertDebug(false, "pstmt is null", logger);
			return;
		}
		if (value == null)
			pstmt.setNull(pos, Types.BIGINT);
		else
			pstmt.setLong(pos, value.longValue());
	}
	
	/*
	 * set an Integer value, checking for null.
	 */
	public void setInt(PreparedStatement pstmt, Integer value, int pos) throws SQLException
	{
		if (pstmt == null) {
			AssertUtil.ourAssertDebug(false, "pstmt is null", logger);
			return;
		}
		if (value == null)
			pstmt.setNull(pos, Types.NUMERIC);
		else
			pstmt.setInt(pos, value.intValue());
	}
	
	/*
	 * will set the value to ordinal + 1 -- because 0 is non-distinguishable from null on read.
	 */
	public void setInt(PreparedStatement pstmt, IOrdinal value, int pos) throws SQLException
	{
		if (pstmt == null) {
			AssertUtil.ourAssertDebug(false, "pstmt is null", logger);
			return;
		}
		if (value == null)
			pstmt.setNull(pos, Types.NUMERIC);
		else
			pstmt.setInt(pos, value.ordinal());
	}
	
	public void setDouble(PreparedStatement pstmt, Double value, int pos) throws SQLException
	{
		if (pstmt == null) {
			AssertUtil.ourAssertDebug(false, "pstmt is null", logger);
			return;
		}
		if (value == null)
			pstmt.setNull(pos, Types.DOUBLE);
		else
			pstmt.setDouble(pos, value.doubleValue());
	}
	
	/*
	 * set a String value, checking for null.
	 */
	public void setString(PreparedStatement pstmt, String value, int pos) throws SQLException
	{
		if (pstmt == null) {
			AssertUtil.ourAssertDebug(false, "pstmt is null", logger);
			return;
		}
		if (StrUtil.isEmpty(value))
			pstmt.setNull(pos, Types.VARCHAR);
		else
			pstmt.setString(pos, value.trim());
	}
	
	public void setString(PreparedStatement pstmt, String value, int pos, int maxStringLen) throws SQLException
	{
		if (pstmt == null) {
			AssertUtil.ourAssertDebug(false, "pstmt is null", logger);
			return;
		}
		if (StrUtil.isEmpty(value))
			pstmt.setNull(pos, Types.VARCHAR);
		else
		{
			String s = value.trim();
			if (s.length() > maxStringLen)
				s = s.substring(0, maxStringLen);
			pstmt.setString(pos, s);
		}
			
	}
	
	// if value is null, set it to ""
	public void setStringNonNull(PreparedStatement pstmt, String value, int pos) throws SQLException
	{
		if (pstmt == null) {
			AssertUtil.ourAssertDebug(false, "pstmt is null", logger);
			return;
		}
		if (StrUtil.isEmpty(value))
			pstmt.setString(pos, "");
		else
			pstmt.setString(pos, value.trim());
	}
	
	public void setBoolean(PreparedStatement pstmt, Boolean value, int pos) throws SQLException
	{
		if (pstmt == null) {
			AssertUtil.ourAssertDebug(false, "pstmt is null", logger);
			return;
		}
		if (value == null)
			pstmt.setNull(pos, Types.BOOLEAN);
		else
			pstmt.setBoolean(pos, value.booleanValue());
	}
	
	public void setBooleanNonNull(PreparedStatement pstmt, Boolean value, int pos) throws SQLException
	{
		if (pstmt == null) {
			AssertUtil.ourAssertDebug(false, "pstmt is null", logger);
			return;
		}
		if (value == null)
			pstmt.setBoolean(pos, false);
		else
			pstmt.setBoolean(pos, value.booleanValue());
	}
	
	/*
	 * set a Short value, checking for null.
	 */
	public void setShort(PreparedStatement pstmt, Short value, int pos) throws SQLException
	{
		if (pstmt == null) {
			AssertUtil.ourAssertDebug(false, "pstmt is null", logger);
			return;
		}
		if (value == null)
			pstmt.setNull(pos, Types.NUMERIC);
		else
			pstmt.setShort(pos, value.shortValue());
	}
	
	/*
	 * set a Timestamp value, checking for null.
	 */
	public void setTimestamp(PreparedStatement pstmt, Timestamp value, int pos) throws SQLException
	{
		if (pstmt == null) {
			AssertUtil.ourAssertDebug(false, "pstmt is null", logger);
			return;
		}
		if (value == null)
			pstmt.setNull(pos, Types.TIMESTAMP);
		else
			pstmt.setTimestamp(pos, value);
	}
	
	/*
	 * set an Integer value; if null, set it to default
	 */
	public void setIntWithDefault(PreparedStatement pstmt, Integer value, int pos, int defaultValue) throws SQLException
	{
		if (pstmt == null) {
			AssertUtil.ourAssertDebug(false, "pstmt is null", logger);
			return;
		}
		if (value == null)
			pstmt.setInt(pos, defaultValue);
		else
			pstmt.setInt(pos, value.intValue());
	}
	
	/*
	 * set a Long value; if null, set it to default
	 */
	public void setLongWithDefault(PreparedStatement pstmt, Long value, int pos, long defaultValue) throws SQLException
	{
		if (pstmt == null) {
			AssertUtil.ourAssertDebug(false, "pstmt is null", logger);
			return;
		}
		if (value == null)
			pstmt.setLong(pos, defaultValue);
		else
			pstmt.setLong(pos, value.longValue());
	}
	
	public void setBooleanWithDefault(PreparedStatement pstmt, Boolean value, int pos, boolean defaultValue) throws SQLException
	{
		if (pstmt == null) {
			AssertUtil.ourAssertDebug(false, "pstmt is null", logger);
			return;
		}
		if (value == null)
			pstmt.setBoolean(pos, defaultValue);
		else
			pstmt.setBoolean(pos, value.booleanValue());
	}
	
	public Long getLong(ResultSet rs, String colName)
		throws SQLException
	{
		return getLongWithDefault(rs, colName, null);
	}
	public Long getLongWithDefault(ResultSet rs, String colName, Long defaultValue)
		throws SQLException
	{
		long longVal = rs.getLong(colName);
		if (longVal != 0)
			return new Long(longVal);
		else
			return defaultValue;
	}
	
	public Integer getInt(ResultSet rs, String colName)
		throws SQLException
	{
		return getIntWithDefault(rs, colName, null);
	}
	public Integer getIntWithDefault(ResultSet rs, String colName, Integer defaultValue)
		throws SQLException
	{
		int intVal = rs.getInt(colName);
		if (intVal != 0)
			return new Integer(intVal);
		else
			return defaultValue;
	}
	
	/**
	 * Handles null/zero value for timestamp
	 *
	 */
	public Timestamp getTimestampWithDefault(ResultSet rs, String colName, Timestamp defaultValue)
	{
		try {
			Timestamp t = rs.getTimestamp(colName);
			return t;
		} catch (Exception e) {
			return defaultValue;
		}
		
	}
	
	
	
	public boolean execSql(Connection con, List<String> sqlList) 
	{
		AssertUtil.assertArg(!ObjUtil.isEmpty(sqlList), "sqlList is empty");
		boolean status = true;
		for (String sql: sqlList)
    	{
    		status &= execSql(con, sql);
    	}
		return status;
	}
	

	/**
	 * Similar to DbUpdate.execSql(...), 
	 * except that (a) we supply the connection as a parameter, and (b) there is no
	 * concept of incrementing a db-step.
	 * 
	 * @param con
	 * @param sql
	 * @param showStackTrace
	 * @return
	 */
	public boolean execSql(Connection con, String sql) 
	{
		AssertUtil.assertArg(!StrUtil.isEmpty(sql), "sql is empty");
		
		boolean status = false;
		Statement stmt = null;
		try 
		{
			con.setAutoCommit(false);
			stmt = con.createStatement();
			logger.info(sql);
			stmt.executeUpdate(sql);		
			con.commit();
			status = true;
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			rollback(con);
			status = false;
		} finally {
			close(stmt);
		}
		
		return status;
	}
	
	/**
	 * if an statement.executeQuery() generated empty data, an SQLException is generated.
	 * The error code in the exception indicates if the error was due to empty data.
	 * @param e
	 * @return
	 */
	public boolean isEmptyData(SQLException e)
	{
		AssertUtil.assertArg(e != null, "e is null");
		if (e.getErrorCode() == CommonErrorConstants.MySQLConstants.EMPTY_DATA)
			return true;
		else
			return false;
	}
	
	public boolean isGroupConcatTruncation(SQLException e)
	{
		AssertUtil.assertArg(e != null, "e is null");
		if (e.getErrorCode() == CommonErrorConstants.MySQLConstants.GROUP_CONCAT_TRUNCATION)
			return true;
		else
			return false;
	}
	
	public boolean isLockWaitTimeout(SQLException e)
	{
		AssertUtil.assertArg(e != null, "e is null");
		if (e.getErrorCode() == CommonErrorConstants.MySQLConstants.LOCK_WAIT_TIMEOUT)
			return true;
		else
			return false;
	}
	
	public boolean isEmptyUpdate(SQLException e)
	{
		AssertUtil.assertArg(e != null, "e is null");
		if (e.getErrorCode() == CommonErrorConstants.MySQLConstants.EMPTY_UPDATE)
			return true;
		else
			return false;
	}

}
