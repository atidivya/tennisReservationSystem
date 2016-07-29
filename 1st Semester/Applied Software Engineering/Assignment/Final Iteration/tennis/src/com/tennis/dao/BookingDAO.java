package com.tennis.dao;

import java.util.Date;
import java.util.List;

import com.tennis.model.Booking;
import com.tennis.model.Court;


/**
 * This interface represents a contract for DAO for the {@link Booking} model.
 * Note that all methods which returns the {@link Booking} from the DB, will not
 * fill the model with the password, due to security reasons.
 * 
 * @author Atidivya
 */
public interface BookingDAO
{
  /**
   * Returns the booking from the database matching the given ID, otherwise null.
   * 
   * @param id The ID of the booking to be returned.
   * 
   * @return The booking from the database matching the given ID, otherwise null.
   * 
   * @throws DAOException If something fails at database level.
   */
  public Booking find(Long id) throws DAOException;
  
  /**
   * Returns the booking from the database matching the given court and start time, otherwise null.
   * 
   * @param court The court of the booking to be returned.
   * @param startTime The start time of the booking to be returned.
   * 
   * @return The booking from the database matching the given court and start time, otherwise null.
   * 
   * @throws DAOException If something fails at database level.
   */
  public Booking find(Court court, Date startTime) throws DAOException;
  
  /**
   * Returns a list of all users from the database ordered by booking ID. The list is never null and
   * is empty when the database does not contain any booking.
   * 
   * @return A list of all bookings from the database ordered by booking ID.
   * 
   * @throws DAOException If something fails at database level.
   */
  public List<Booking> list() throws DAOException;
  
  /**
   * Create the given booking in the database. The booking ID must be null, otherwise it will throw
   * IllegalArgumentException. After creating, the DAO will set the obtained ID in the given booking.
   * 
   * @param booking The booking to be created in the database.
   * 
   * @throws IllegalArgumentException If the booking ID is not null.
   * @throws DAOException If something fails at database level.
   */
  public void create(Booking booking) throws IllegalArgumentException, DAOException;
  
  /**
   * Update the given booking in the database. The booking ID must not be null, otherwise it will throw
   * IllegalArgumentException.
   * 
   * @param booking The booking to be updated in the database.
   * 
   * @throws IllegalArgumentException If the booking ID is null.
   * @throws DAOException If something fails at database level.
   */
  public void update(Booking booking) throws IllegalArgumentException, DAOException;
  
  /**
   * Delete the given booking from the database. After deleting, the DAO will set the ID of the given
   * booking to null.
   * 
   * @param booking The booking to be deleted from the database.
   * 
   * @throws DAOException If something fails at database level.
   */
  public void delete(Booking booking) throws DAOException;
}
