package com.tennis.dao;

import static com.tennis.dao.DAOUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tennis.model.Booking;
import com.tennis.model.Court;
import com.tennis.model.Member;

/**
 * This class represents a concrete JDBC implementation of the {@link BookingDAO} interface.
 * 
 * @author Jose Monteiro
 */
public class BookingDAOJDBC implements BookingDAO
{
  private static final String SQL_FIND_BY_ID = "SELECT booking.id AS booking_id, booking.start_time AS booking_start_time, booking.type AS booking_type, booking.observations AS booking_observations, court.id AS court_id, court.name AS court_name, court.surface AS court_surface, court.opening_time AS court_opening_time, court.closing_time AS court_closing_time, court.indoor AS court_indoor, court.lighting AS court_lighting, court.observations AS court_observations, member.id AS member_id, member.name AS member_name, member.email AS member_email, member.phone AS member_phone, member.address AS member_address, member.observations AS member_observations FROM booking LEFT JOIN court ON booking.court = court.id LEFT JOIN member ON booking.member = member.id WHERE booking.id = ?";
  private static final String SQL_FIND_BY_COURT_AND_START_TIME = "SELECT booking.id AS booking_id, booking.start_time AS booking_start_time, booking.type AS booking_type, booking.observations AS booking_observations, court.id AS court_id, court.name AS court_name, court.surface AS court_surface, court.opening_time AS court_opening_time, court.closing_time AS court_closing_time, court.indoor AS court_indoor, court.lighting AS court_lighting, court.observations AS court_observations, member.id AS member_id, member.name AS member_name, member.email AS member_email, member.phone AS member_phone, member.address AS member_address, member.observations AS member_observations FROM booking LEFT JOIN court ON booking.court = court.id LEFT JOIN member ON booking.member = member.id WHERE booking.court = ? AND booking.start_time = ?";
  private static final String SQL_LIST_ORDER_BY_ID = "SELECT booking.id AS booking_id, booking.start_time AS booking_start_time, booking.type AS booking_type, booking.observations AS booking_observations, court.id AS court_id, court.name AS court_name, court.surface AS court_surface, court.opening_time AS court_opening_time, court.closing_time AS court_closing_time, court.indoor AS court_indoor, court.lighting AS court_lighting, court.observations AS court_observations, member.id AS member_id, member.name AS member_name, member.email AS member_email, member.phone AS member_phone, member.address AS member_address, member.observations AS member_observations FROM booking LEFT JOIN court ON booking.court = court.id LEFT JOIN member ON booking.member = member.id ORDER BY booking.id";
  private static final String SQL_INSERT = "INSERT INTO booking (court, start_time, type, member, observations) VALUES (?, ?, ?, ?, ?)";
  private static final String SQL_UPDATE = "UPDATE booking SET court = ?, start_time = ?, type = ?, member = ?, observations = ? WHERE id = ?";
  private static final String SQL_DELETE = "DELETE FROM booking WHERE id = ?";
  
  private DAOFactory daoFactory;
  
  /**
   * Construct a Booking DAO for the given DAOFactory. Package private so that it can be constructed
   * inside the DAO package only.
   * 
   * @param daoFactory The DAOFactory to construct this Booking DAO for.
   */
  BookingDAOJDBC(DAOFactory daoFactory)
  {
    this.daoFactory = daoFactory;
  }
  
  @Override
  public Booking find(Long id) throws DAOException
  {
    return find(SQL_FIND_BY_ID, id);
  }
  
  @Override
  public Booking find(Court court, Date startTime) throws DAOException
  {
    return find(SQL_FIND_BY_COURT_AND_START_TIME, court.getId(), startTime);
  }
  
  /**
   * Returns the booking from the database matching the given SQL query with the given values.
   * 
   * @param sql The SQL query to be executed in the database.
   * @param values The PreparedStatement values to be set.
   * 
   * @return The booking from the database matching the given SQL query with the given values.
   * 
   * @throws DAOException If something fails at database level.
   */
  private Booking find(String sql, Object... values) throws DAOException
  {
    Booking booking = null;
    
    try
    (
      Connection connection = daoFactory.getConnection();
      PreparedStatement statement = prepareStatement(connection, sql, false, values);
      ResultSet resultSet = statement.executeQuery();
    )
    {
      if (resultSet.next())
      {
        booking = map(resultSet);
      }
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
    
    return booking;
  }
  
  @Override
  public List<Booking> list() throws DAOException
  {
    List<Booking> bookings = new ArrayList<>();
    
    try
    (
      Connection connection = daoFactory.getConnection();
      PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
      ResultSet resultSet = statement.executeQuery();
    )
    {
      while (resultSet.next())
      {
        bookings.add(map(resultSet));
      }
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
    
    return bookings;
  }
  
  @Override
  public void create(Booking booking) throws IllegalArgumentException, DAOException
  {
    if (booking.getId() != null)
    {
      throw new IllegalArgumentException("Booking is already created, the booking ID is not null.");
    }
    
    Object[] values = {
      booking.getCourt().getId(),
      toSqlDate(booking.getStartTime()),
      booking.getType(),
      booking.getMember().getId(),
      booking.getObservations()
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
        throw new DAOException("Creating booking failed, no rows affected.");
      }
      
      try (ResultSet generatedKeys = statement.getGeneratedKeys())
      {
        if (generatedKeys.next())
        {
          booking.setId(generatedKeys.getLong(1));
        }
        else
        {
          throw new DAOException("Creating booking failed, no generated key obtained");
        }
      }
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
  }
  
  @Override
  public void update(Booking booking) throws DAOException
  {
    if (booking.getId() == null)
    {
      throw new IllegalArgumentException("Booking is not created yet, the booking ID is null");
    }
    
    Object[] values = {
      booking.getCourt().getId(),
      toSqlDate(booking.getStartTime()),
      booking.getType(),
      booking.getMember().getId(),
      booking.getObservations(),
      booking.getId()
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
        throw new DAOException("Updating booking failed, no rows affected");
      }
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
  }
  
  @Override
  public void delete(Booking booking) throws DAOException
  {
    Object[] values = {
      booking.getId()
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
        throw new DAOException("Deleting booking failed, no rows affected.");
      }
      else
      {
        booking.setId(null);
      }
    }
    catch (SQLException e)
    {
      throw new DAOException(e);
    }
  }
  
  /**
   * Map the current row of the given ResultSet to a Booking.
   * 
   * @param resultSet The ResultSet of which the current row is to be mapped to a Booking.
   * 
   * @return The mapped Booking from the current row of the given ResultSet.
   * 
   * @throws SQLException If something fails at database level.
   */
  private static Booking map(ResultSet resultSet) throws SQLException
  {
    Booking booking = new Booking();
    Court court = new Court();
    Member member = new Member();
    
    court.setId(resultSet.getLong("court_id"));
    court.setName(resultSet.getString("court_name"));
    court.setSurface(resultSet.getString("court_surface"));
    court.setOpeningTime(toLocalTime(resultSet.getTime("court_opening_time")));
    court.setClosingTime(toLocalTime(resultSet.getTime("court_closing_time")));
    court.setIndoor(resultSet.getBoolean("court_indoor"));
    court.setLighting(resultSet.getBoolean("court_lighting"));
    court.setObservations(resultSet.getString("court_observations"));
           
    member.setId(resultSet.getLong("member_id"));
    member.setName(resultSet.getString("member_name"));
    member.setEmail(resultSet.getString("member_email"));
    member.setPhone(resultSet.getString("member_phone"));
    member.setAddress(resultSet.getString("member_address"));
    member.setObservations(resultSet.getString("member_observations"));
    
    booking.setId(resultSet.getLong("booking_id"));
    booking.setCourt(court);
    booking.setStartTime(resultSet.getDate("booking_start_time"));
    booking.setType(resultSet.getString("booking_type"));
    booking.setMember(member);
    booking.setObservations(resultSet.getString("booking_observations"));
    
    return booking;
  }
}
