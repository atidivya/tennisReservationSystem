package com.tennis.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;

/**
 * Utility class for DAO's. This class contains commonly used DAO logic which is been refactored in
 * single static methods. As far it contains PreparedStatement values setter and a
 * <code>java.util.Date</code> to <code>java.sql.Date</code> converter.
 * 
 * @author Atidivya
 */
public final class DAOUtil
{
  private DAOUtil()
  {
    // Utility class, hide constructor.
  }
  
  /**
   * Returns a PreparedStatement of the given connection, set with the given SQL query and the
   * given parameter values.
   * 
   * @param connection The Connection to create the PreparedStatement from.
   * @param sql The SQL query to construct the PreparedStatement with.
   * @param returnGeneratedKeys Set whether to return generated keys or not.
   * @param values The parameter values to be set in the created PreparedStatement.
   * 
   * @throws SQLException If something fails during creating the PreparedStatement.
   */
  public static PreparedStatement prepareStatement(Connection connection, String sql, boolean returnGeneratedKeys, Object... values) throws SQLException
  {
    PreparedStatement statement = connection.prepareStatement(sql, returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
    
    setValues(statement, values);
    
    return statement;
  }
  
  /**
   * Set the given parameter values in the given PreparedStatement.
   * 
   * @param statement The PreparedStatement to set the given parameter values in.
   * @param values The parameter values to be set in the created PreparedStatement.
   * 
   * @throws SQLException If something fails during setting the PreparedStatement values.
   */
  public static void setValues(PreparedStatement statement, Object... values) throws SQLException
  {
    for (int i = 0; i < values.length; i++)
    {
      statement.setObject(i + 1, values[i]);
    }
  }
  
  /**
   * Converts the given java.util.Date to java.sql.Date.
   * 
   * @param date The java.util.Date to be converted to java.sql.Date.
   * 
   * @return The converted java.sql.Date.
   */
  public static Date toSqlDate(java.util.Date date)
  {
    return (date != null) ? new Date(date.getTime()) : null;
  }
  
  /**
   * Converts the given java.time.LocalTime to java.sql.Time.
   * 
   * @param localTime The java.time.LocalTime to be converted to java.sql.Time.
   * 
   * @return The converted java.sql.Time.
   */
  public static Time toSqlTime(LocalTime localTime)
  {
    return (localTime != null) ? Time.valueOf(localTime) : null;
  }
  
  /**
   * Converts the given java.sql.Time to java.time.LocalTime.
   * 
   * @param sqlTime The java.sql.Time to be converted to java.time.LocalTime.
   * 
   * @return The converted java.time.LocalTime.
   */
  public static LocalTime toLocalTime(Time sqlTime)
  {
    return (sqlTime != null) ? sqlTime.toLocalTime() : null;
  }
}
