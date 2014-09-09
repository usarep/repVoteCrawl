/*
 * This class is originally Clarifyre code,
 * transferred to Vugle in 2006. It's currently 
 * owned by Vugle.
 * 
 * It's made available to UrlTrails as part of the
 * Shared Framework Agreement which states that 
 * it's owned by the original owner, but UrlTrails
 * has a perpetual royalty free license to use it
 * in all of its products, and modify it as
 * necessary. 
 * 
 * Created: 2005.
 * Copyright Vugle 2005-2007.
 * 
 */
package common.db;

import java.sql.Connection;

/**
 * @author amLaptop
 */
public interface IConnectionServer 
{
    Connection getConnection();
}
