package common.db;

import java.sql.Connection;

/**
 * A connection that may or may not be thread local
 * @author amLaptop
 *
 */
public class ConnectionWrapper
{
	private Connection connection;
	private boolean threadLocal;
	
	public Connection getConnection() {
		return connection;
	}
	public boolean isThreadLocal() {
		return threadLocal;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public void setThreadLocal(boolean threadLocal) {
		this.threadLocal = threadLocal;
	}
}