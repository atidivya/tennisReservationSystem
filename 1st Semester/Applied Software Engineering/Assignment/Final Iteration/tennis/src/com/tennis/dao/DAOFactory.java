package com.tennis.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * This class represents a DAO factory for a SQL database. You can use {@link #getInstance(String)}
 * to obtain a new instance for the given database name. The specific instance returned depends on
 * the properties file configuration. You can obtains DAO's for the DAO factory instance using the
 * DAO getters.
 * <p>
 * This class requires a properties file named 'dao.properties' in the classpath with among others
 * the following properties:
 * <pre>
 * name.url *
 * name.driver
 * name.username
 * name.password
 * </pre>
 * Those marked with * are required, others are optional and can be left away or empty. Only the
 * username is required when any password is specified.
 * <ul>
 * <li>The 'name' must represent the database name in {@link #getInstance(String)}.</li>
 * <li>The 'name.url' must represent either the JDBC URL or JNDI name of the database.</li>
 * <li>The 'name.driver' must represent the full qualified class name of the JDBC driver.</li>
 * <li>The 'name.username' must represent the username of the database login.</li>
 * <li>The 'name.password' must represent the password of the database login.</li>
 * </ul>
 * If you specify the driver property, then the url property will be assumed as JDBC URL. If you
 * omit the driver property, the the url property will be assumed as JNDI name. When using JNDI
 * with username/password preconfigured, you can omit the username and password properties as well.
 * <p>
 * Here as basic examples of valid properties for database with the name 'tennis':
 * <pre>
 * tennis.jdbc.url = jdbc:mysql://localhost:33006/tennis
 * tennis.jdbc.driver = com.mysql.jdbc.Driver
 * tennis.jdbc.username = root
 * tennis.jdbc.password = root
 * </pre>
 * <pre>
 * tennis.jndi.url = jdbc/tennis
 * </pre>
 * Here is a basic use example:
 * <pre>
 * DAOFactory tennis = DAOFactory.getInstance("tennis.jdbc");
 * MemberDAO memberDAO = tennis.getMemberDAO();
 * </pre>
 * 
 * @author Jose Monteiro
 */
public abstract class DAOFactory
{
  private static final String PROPERTY_URL = "url";
  private static final String PROPERTY_DRIVER = "driver";
  private static final String PROPERTY_USERNAME = "username";
  private static final String PROPERTY_PASSWORD = "password";
  
  /**
   * Returns a new DAOFactory instance for the given database name.
   * 
   * @param name The database name to return a new DAOFactory instance for.
   * 
   * @return A new DAOFactory instance for the given database name.
   * 
   * @throws DAOConfigurationException If the database name is null, or if the properties file is
   * missing in the classpath or cannot be loaded, or if a required property is missing in the
   * properties file, or if either the driver cannot be loaded or the datasource cannot be found.
   */
  public static DAOFactory getInstance(String name) throws DAOConfigurationException
  {
    if (name == null)
    {
      throw new DAOConfigurationException("Database name is null.");
    }
    
    DAOProperties properties = new DAOProperties(name);
    
    String url = properties.getProperty(PROPERTY_URL, true);
    String driverClassName = properties.getProperty(PROPERTY_DRIVER, false);
    String password = properties.getProperty(PROPERTY_PASSWORD, false);
    String username = properties.getProperty(PROPERTY_USERNAME, password != null);
    
    DAOFactory instance;
    
    // If driver is specified, then load it to let it register itself with DriverManager
    if (driverClassName != null)
    {
      try
      {
        Class.forName(driverClassName);
      }
      catch (ClassNotFoundException e)
      {
        throw new DAOConfigurationException("Driver class '" + driverClassName + "' is missing in the classpath.", e);
      }
      
      instance = new DriverManagerDAOFactory(url, username, password);
    }
    // Else assume URL as DataSource URL and lookup it in the JNDI.
    else
    {
      DataSource dataSource;
      
      try
      {
        dataSource = (DataSource) new InitialContext().lookup(url);
      }
      catch (NamingException e)
      {
        throw new DAOConfigurationException("DataSource '" + url + "' is missing in JNDI.", e);
      }
      
      if (username != null)
      {
        instance = new DataSourceWithLoginDAOFactory(dataSource, username, password);
      }
      else
      {
        instance = new DataSourceDAOFactory(dataSource);
      }
    }
    
    return instance;
  }
  
  /**
   * Returns a connection to the database. Package private so that it can be used inside the DAO
   * package only.
   * 
   * @return A connection to the database.
   * 
   * @throws SQLException If acquiring the connection fails.
   */
  abstract Connection getConnection() throws SQLException;
  
  /**
   * Returns the Member DAO associated with the current DAOFactory.
   * 
   * @return The Member DAO associated with the current DAOFactory.
   */
  public MemberDAO getMemberDAO()
  {
    return new MemberDAOJDBC(this);
  }
  
  /**
   * Returns the Court DAO associated with the current DAOFactory.
   * 
   * @return The Court DAO associated with the current DAOFactory.
   */
  public CourtDAO getCourtDAO()
  {
    return new CourtDAOJDBC(this);
  }
  
  /**
   * Returns the Holiday DAO associated with the current DAOFactory.
   * 
   * @return The Holiday DAO associated with the current DAOFactory.
   */
  public HolidayDAO getHolidayDAO()
  {
    return new HolidayDAOJDBC(this);
  }
  
  /**
   * Returns the Booking DAO associated with the current DAOFactory.
   * 
   * @return The Booking DAO associated with the current DAOFactory.
   */
  public BookingDAO getBookingDAO()
  {
    return new BookingDAOJDBC(this);
  }
}

/**
 * The DriverManager based DAOFactory.
 * 
 * @author Jose Monteiro
 */
class DriverManagerDAOFactory extends DAOFactory
{
  private String url;
  private String username;
  private String password;
  
  DriverManagerDAOFactory(String url, String username, String password)
  {
    this.url = url;
    this.username = username;
    this.password = password;
  }
  
  @Override
  Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(url, username, password);
  }
}

/**
 * The DataSource based DAOFactory.
 * 
 * @author Jose Monteiro
 */
class DataSourceDAOFactory extends DAOFactory
{
  private DataSource dataSource;
  
  DataSourceDAOFactory(DataSource dataSource)
  {
    this.dataSource = dataSource;
  }
  
  @Override
  Connection getConnection() throws SQLException
  {
    return dataSource.getConnection();
  }
}

/**
 * The DataSource-with-Login based DAOFactory.
 * 
 * @author Jose Monteiro
 */
class DataSourceWithLoginDAOFactory extends DAOFactory
{
  private DataSource dataSource;
  private String username;
  private String password;
  
  DataSourceWithLoginDAOFactory(DataSource dataSource, String username, String password)
  {
    this.dataSource = dataSource;
    this.username = username;
    this.password = password;
  }
  
  @Override
  Connection getConnection() throws SQLException
  {
    return dataSource.getConnection(username, password);
  }
}