package com.tennis.dao;

import static com.tennis.dao.DAOUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tennis.model.Court;

/**
 * This class represents a concrete JDBC implementation of the {@link CourtDAO} interface.
 * 
 * @author Jose Monteiro
 */
public class CourtDAOJDBC implements CourtDAO
{
  private static final String SQL_FIND_BY_ID = "SELECT id, name, surface, opening_time, closing_time, indoor, lighting, observations FROM court WHERE id = ?";
  private static final String SQL_LIST_ORDER_BY_ID = "SELECT id, name, surface, opening_time, closing_time, indoor, lighting, observations FROM court ORDER BY id";
  private static final String SQL_INSERT = "INSERT INTO court (name, surface, opening_time, closing_time, indoor, lighting, observations) VALUES (?, ?, ?, ?, ?, ?, ?)";
  private static final String SQL_UPDATE = "UPDATE court SET name = ?, surface = ?, opening_time = ?, closing_time = ?, indoor = ?, lighting = ?, observations = ? WHERE id = ?";
  private static final String SQL_DELETE = "DELETE FROM court WHERE id = ?";
  private static final String SQL_EXIST_NAME = "SELECT id FROM court WHERE name = ?";
  
  private DAOFactory daoFactory;
  
  /**
   * Construct a Court DAO for the given DAOFactory. Package private so that it can be constructed
   * inside the DAO package only.
   * 
   * @param daoFactory The DAOFactory to construct this Court DAO for.
   */
  CourtDAOJDBC(DAOFactory daoFactory)
  {
    this.daoFactory = daoFactory;
  }
  
  @Override
  public Court find(Long id) throws DAOException
  {
    return find(SQL_FIND_BY_ID, id);
  }
  
  /**
   * Returns the court from the database matching the given SQL query with the given values.
   * 
   * @param sql The SQL query to be executed in the database.
   * @param values The PreparedStatement values to be set.
   * 
   * @return The court from the database matching the given SQL query with the given values.
   * 
   * @throws DAOException If the something fails at database level.
   */
  private Court find(String sql, Object... values) throws DAOException
  {
    Court court = null;
    
    try
    (
      Connection connection = daoFactory.getConnection();
      PreparedStatement statement = prepareStatement(connection, sql, false, values);
      ResultSet resultSet = statement.executeQuery();
    )
    {
      if (resultSet.next())
      {
        court = map(resultSet);
      }
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
    
    return court;
  }
  
  @Override
  public List<Court> list() throws DAOException
  {
    List<Court> courts = new ArrayList<>();
    
    try
    (
      Connection connection = daoFactory.getConnection();
      PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
      ResultSet resultSet = statement.executeQuery();
    )
    {
      while (resultSet.next())
      {
        courts.add(map(resultSet));
      }
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
    
    return courts;
  }
  
  @Override
  public void create(Court court) throws IllegalArgumentException, DAOException
  {
    if (court.getId() != null)
    {
      throw new IllegalArgumentException("Court is already created, the court ID is not null.");
    }
    
    Object[] values = {
      court.getName(),
      court.getSurface(),
      toSqlTime(court.getOpeningTime()),
      toSqlTime(court.getClosingTime()),
      court.isIndoor(),
      court.isLighting(),
      court.getObservations()
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
        throw new DAOException("Creating user failed, no rows affected.");
      }
      
      try (ResultSet generatedKeys = statement.getGeneratedKeys())
      {
        if (generatedKeys.next())
        {
          court.setId(generatedKeys.getLong(1));
        }
        else
        {
          throw new DAOException("Creating court failed, no generated key obtained.");
        }
      }
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
  }
  
  @Override
  public void update(Court court) throws DAOException
  {
    if (court.getId() == null)
    {
      throw new IllegalArgumentException("Court is not created yet, the court ID is null.");
    }
    
    Object[] values = {
      court.getName(),
      court.getSurface(),
      toSqlTime(court.getOpeningTime()),
      toSqlTime(court.getClosingTime()),
      court.isIndoor(),
      court.isLighting(),
      court.getObservations(),
      court.getId()
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
        throw new DAOException("Updating court failed, no rows affected.");
      }
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
  }
  
  @Override
  public void delete(Court court) throws DAOException
  {
    Object[] values = {
      court.getId()
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
        throw new DAOException("Deleting court failed, no rows affected.");
      }
      else
      {
        court.setId(null);
      }
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
  }
  
  @Override
  public boolean existName(String name) throws DAOException
  {
    Object[] values = {
      name
    };
    
    boolean exist = false;
    
    try
    (
      Connection connection = daoFactory.getConnection();
      PreparedStatement statement = prepareStatement(connection, SQL_EXIST_NAME, false, values);
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
   * Map the current row of the given ResultSet to a Court.
   * 
   * @param resultSet The ResultSet of which the current row is to be mapped to a Court.
   * 
   * @return The mapped Court from the current row of the given ResultSet.
   * 
   * @throws SQLException If something fails at database level.
   */
  private static Court map(ResultSet resultSet) throws SQLException
  {
    Court court = new Court();
    
    court.setId(resultSet.getLong("id"));
    court.setName(resultSet.getString("name"));
    court.setSurface(resultSet.getString("surface"));
    court.setOpeningTime(toLocalTime(resultSet.getTime("opening_time")));
    court.setClosingTime(toLocalTime(resultSet.getTime("closing_time")));
    court.setIndoor(resultSet.getBoolean("indoor"));
    court.setLighting(resultSet.getBoolean("lighting"));
    court.setObservations(resultSet.getString("observations"));
    
    return court;
  }
}
