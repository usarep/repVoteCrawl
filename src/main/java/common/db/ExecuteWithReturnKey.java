/*
 * This class is originally 1255 Patterns code,
 * transferred to Clarifyre in 2005, and then
 * to Vugle in 2006. It's currently owned by Vugle.
 * 
 * It's made available to UrlTrails as part of the
 * Shared Framework Agreement which states that 
 * it's owned by the original owner, but UrlTrails
 * has a perpetual royalty free license to use it
 * in all of its products, and modify it as
 * necessary. 
 * 
 * Created: Jan 2005.
 * Copyright Vugle 2005-2007.
 * 
 */

package common.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



/**
 * @author amLaptop
 */
public class ExecuteWithReturnKey
{

    public static ExecuteWithReturnKey o = new ExecuteWithReturnKey();
    public static ExecuteWithReturnKey getInstance() { return o;}
    
    private ExecuteWithReturnKey() {}
    
    public static class ResultWithKey
    {
        private int returnValue; // return value from executeUpdate()
        private Long generatedKey; // generated key if any
        
        public String toString() {
            return "(returnValue, generatedKey)= " + "(" + returnValue + ", " + generatedKey + ")";
        }

		public Long getGeneratedKey() {
			return generatedKey;
		}

		public int getReturnValue() {
			return returnValue;
		}
    }
    public ResultWithKey executeUpdate(PreparedStatement statement)
    	throws SQLException
    {
    	ResultSet rs  = null;
    	ResultWithKey result = new ResultWithKey();
    	
    	try {
    
	        result.returnValue = statement.executeUpdate();
	        rs = statement.getGeneratedKeys();
	        
	        // max one key for our app
	        while(rs.next())
	        {
	            long longValue =  rs.getLong(1);
	            result.generatedKey = new Long(longValue);
	            break;
	        }
    	} finally {
    		BaseDbUtils.o.close(rs);
    	}
        return result;
    }
    
    public ResultWithKey executeUpdate(Statement statement, String sql)
	throws SQLException
	{
		ResultSet rs  = null;
		ResultWithKey result = new ResultWithKey();
		
		try {
	
	        result.returnValue = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
	        rs = statement.getGeneratedKeys();
	        
	        // max one key for our app
	        while(rs.next())
	        {
	            long longValue =  rs.getLong(1);
	            result.generatedKey = new Long(longValue);
	            break;
	        }
		} finally {
			BaseDbUtils.o.close(rs);
		}
	    return result;
	}
    
    
    public PreparedStatement createPrepared(String sql, IConnectionServer connectionServer)
	{
	    Connection connection = null;
		try {
		    connection = connectionServer.getConnection();
		    PreparedStatement statement = connection.prepareStatement(sql);
		    return statement;
		    
		} catch (SQLException e) {
		    e.printStackTrace();
		    return null;
		}
	}
}
