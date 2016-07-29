package com.tennis.dao;

import static com.tennis.dao.DAOUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tennis.model.Holiday;

/**
 * This class represents a concrete JDBC implementation of the {@link Holiday} interface.
 * 
 * @author Atidivya
 */
public class HolidayDAOJDBC implements HolidayDAO
{
  private static final String SQL_FIND_BY_ID = "SELECT id, date, name, observations FROM holiday WHERE id = ?";
  private static final String SQL_LIST_ORDER_BY_ID = "SELECT id, date, name, observations FROM holiday ORDER BY id";
  private static final String SQL_INSERT = "INSERT INTO holiday (date, name, observations) VALUES(?, ?, ?)";
  private static final String SQL_UPDATE = "UPDATE holiday SET date = ?, name = ?, observations = ? WHERE id = ?";
  private static final String SQL_DELETE = "DELETE FROM holiday WHERE id = ?";
  private static final String SQL_EXIST_DATE = "SELECT id FROM holiday WHERE date = ?";
  
  private DAOFactory daoFactory;
  
  /**
   * Construct a Holiday DAO for the given DAOFactory. Package private so that it can be constructed
   * inside the DAO package only.
   * 
   * @param daoFactory The DAOFactory to construct this Holiday DAO for.
   */
  HolidayDAOJDBC(DAOFactory daoFactory)
  {
    this.daoFactory = daoFactory;
  }
  
  @Override
  public Holiday find(Long id) throws DAOException
  {
    return find(SQL_FIND_BY_ID, id);
  }
  
  /**
   * Returns the holiday from the database matching the given SQL query with the given values.
   * 
   * @param sql The SQL query to be executed in the database.
   * @param values The PreparedStatement values to be set.
   * 
   * @return The holiday from the database matching the given SQL query with the given values.
   * 
   * @throws DAOException If something fails at database level.
   */
  private Holiday find(String sql, Object... values) throws DAOException
  {
    Holiday holiday = null;
    
    try
    (
      Connection connection = daoFactory.getConnection();
      PreparedStatement statement = prepareStatement(connection, sql, false, values);
      ResultSet resultSet = statement.executeQuery();
    )
    {
      if (resultSet.next())
      {
        holiday = map(resultSet);
      }
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
    
    return holiday;
  }
  
  @Override
  public List<Holiday> list() throws DAOException
  {
    List<Holiday> holidays = new ArrayList<>();
    
    try
    (
      Connection connection = daoFactory.getConnection();
      PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
      ResultSet resultSet = statement.executeQuery();
    )
    {
      while (resultSet.next())
      {
        holidays.add(map(resultSet));
      }
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
    
    return holidays;
  }
  
  @Override
  public void create(Holiday holiday) throws IllegalArgumentException, DAOException
  {
    if (holiday.getId() != null)
    {
      throw new IllegalArgumentException("Holiday is already created, the holiday ID is not null.");
    }
    
    Object[] values = {
      toSqlDate(holiday.getDate()),
      holiday.getName(),
      holiday.getObservations()
    };
    
    try
    (
      Connection connection = daoFactory.getConnection();
      PreparedStatement statement = prepareStatement(connection, SQL_INSERT, true, values);
    )
    {
      int affectedRows = statement.executeUpdate();
      
      if (affectedRows == 0)
      {
        throw new DAOException("Creating holiday failed, no rows affected.");
      }
      
      try (ResultSet generatedKeys = statement.getGeneratedKeys())
      {
        if (generatedKeys.next())
        {
          holiday.setId(generatedKeys.getLong(1));
        }
        else
        {
          throw new DAOException("Creating user failed, no generated key obtained.");
        }
      }
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
  }
  
  @Override
  public void update(Holiday holiday) throws DAOException
  {
    if (holiday.getId() == null)
    {
      throw new IllegalArgumentException("Holiday is not created yet, the holiday ID is null.");
    }
    
    Object[] values = {
      toSqlDate(holiday.getDate()),
      holiday.getName(),
      holiday.getObservations(),
      holiday.getId()
    };
    
    try
    (
      Connection connection = daoFactory.getConnection();
      PreparedStatement statement = prepareStatement(connection, SQL_UPDATE, false, values);
    )
    {
      int affectedRows = statement.executeUpdate();
      
      if (affectedRows == 0)
      {
        throw new DAOException("Updating holiday failed, no rows affected.");
      }
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
  }
  
  @Override
  public void delete(Holiday holiday) throws DAOException
  {
    Object[] values = {
      holiday.getId()
    };
    
    try
    (
      Connection connection = daoFactory.getConnection();
      PreparedStatement statement = prepareStatement(connection, SQL_DELETE, false, values);
    )
    {
      int affectedRows = statement.executeUpdate();
      
      if (affectedRows == 0)
      {
        throw new DAOException("Deleting holiday failed, no rows affected.");
      }
      else
      {
        holiday.setId(null);
      }
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
  }
  
  @Override
  public boolean existDate(Date date) throws DAOException
  {
    Object[] values = {
      date
    };
    
    boolean exist = false;
    
    try
    (
      Connection connection = daoFactory.getConnection();
      PreparedStatement statement = prepareStatement(connection, SQL_EXIST_DATE, false, values);
      ResultSet resultSet = statement.executeQuery();
    )
    {
      exist = resultSet.next();
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
      
    return exist;
  }
  
  /**
   * Map the current row of the given ResultSet to a Holiday.
   * 
   * @param resultSet The ResultSet of which the current row is to be mapped to a Holiday.
   * 
   * @return The mapped Holiday from the current row of given ResultSet.
   * 
   * @throws SQLException If something fails at database level.
   */
  private static Holiday map(ResultSet resultSet) throws SQLException
  {
    Holiday holiday = new Holiday();
    
    holiday.setId(resultSet.getLong("id"));
    holiday.setDate(resultSet.getDate("date"));
    holiday.setName(resultSet.getString("name"));
    holiday.setObservations(resultSet.getString("observations"));
    
    return holiday;
  }
}
