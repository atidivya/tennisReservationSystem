package com.tennis.dao;

import static com.tennis.dao.DAOUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tennis.model.Member;

/**
 * This class represents a concrete JDBC implementation of the {@link MemberDAO} interface.
 * 
 * @author Atidivya
 */
public class MemberDAOJDBC implements MemberDAO
{
  private static final String SQL_FIND_BY_ID = "SELECT id, name, email, phone, address, observations FROM member WHERE id = ?";
  private static final String SQL_LIST_ORDER_BY_ID = "SELECT id, name, email, phone, address, observations FROM member ORDER BY id";
  private static final String SQL_INSERT = "INSERT INTO member(name, email, phone, address, observations) VALUES(?, ?, ?, ?, ?)";
  private static final String SQL_UPDATE = "UPDATE member SET name = ?, email = ?, phone = ?, address = ?, observations = ? WHERE id = ?";
  private static final String SQL_DELETE = "DELETE FROM member WHERE id = ?";
  
  private DAOFactory daoFactory;
  
  /**
   * Construct a Member DAO for the given DAOFactory. Package private so that it can be constructed
   * inside the DAO package only.
   * 
   * @param daoFactory 
   */
  MemberDAOJDBC(DAOFactory daoFactory)
  {
    this.daoFactory = daoFactory;
  }
  
  @Override
  public Member find(Long id) throws DAOException
  {
    return find(SQL_FIND_BY_ID, id);
  }
  
  /**
   * Returns the member from the database matching the given SQL query with the given values.
   * 
   * @param sql The SQL query to be executed in the database.
   * @param values The PreparedStatement values to be set.
   * 
   * @return The member from the database matching the given SQL query with the given values.
   * 
   * @throws DAOException If something fails at database level.
   */
  private Member find(String sql, Object... values) throws DAOException
  {
    Member member = null;
    
    try
    (
      Connection connection = daoFactory.getConnection();
      PreparedStatement statement = prepareStatement(connection, sql, false, values);
      ResultSet resultSet = statement.executeQuery();
    )
    {
      if (resultSet.next())
      {
        member = map(resultSet);
      }
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
    
    return member;
  }
  
  @Override
  public List<Member> list() throws DAOException
  {
    List<Member> members = new ArrayList<>();
    
    try
    (
      Connection connection = daoFactory.getConnection();
      PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
      ResultSet resultSet = statement.executeQuery();
    )
    {
      while (resultSet.next())
      {
        members.add(map(resultSet));
      }
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
    
    return members;
  }
  
  @Override
  public void create(Member member) throws IllegalArgumentException, DAOException
  {
    if (member.getId() != null)
    {
      throw new IllegalArgumentException("Member is already created, the member ID is not null.");
    }
    
    Object[] values = {
      member.getName(),
      member.getEmail(),
      member.getPhone(),
      member.getAddress(),
      member.getObservations()
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
        throw new DAOException("Creating member failed, no rows affected.");
      }
      
      try (ResultSet generatedKeys = statement.getGeneratedKeys())
      {
        if (generatedKeys.next())
        {
          member.setId(generatedKeys.getLong(1));
        }
        else
        {
          throw new DAOException("Creating member failed, no generated key obtained.");
        }
      }
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
  }
  
  @Override
  public void update(Member member) throws DAOException
  {
    if (member.getId() == null)
    {
      throw new IllegalArgumentException("Member is not created yet, the member ID is null.");
    }
    
    Object[] values = {
      member.getName(),
      member.getEmail(),
      member.getPhone(),
      member.getAddress(),
      member.getObservations(),
      member.getId()
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
        throw new DAOException("Updating member failed, no rows affected");
      }
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
  }
  
  @Override
  public void delete(Member member) throws DAOException
  {
    Object[] values = {
      member.getId()
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
        throw new DAOException("Deleting member failed, no rows affected.");
      }
      else
      {
        member.setId(null);
      }
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
  }
  
  /**
   * Map the current row of the given ResultSet to an Member.
   * 
   * @param resultSet The ReseultSet of which the current row is to be mapped to an Member.
   * 
   * @return Member from the current row of the given ResultSet.
   * 
   * @throws SQLException If something fails at database level.
   */
  private static Member map(ResultSet resultSet) throws SQLException
  {
    Member member = new Member();
    
    member.setId(resultSet.getLong("id"));
    member.setName(resultSet.getString("name"));
    member.setEmail(resultSet.getString("email"));
    member.setPhone(resultSet.getString("phone"));
    member.setAddress(resultSet.getString("address"));
    member.setObservations(resultSet.getString("observations"));
    
    return member;
  }
}
