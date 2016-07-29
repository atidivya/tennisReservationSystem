package com.tennis.dao;

import java.util.Date;
import java.util.List;

import com.tennis.model.Holiday;

/**
 * This interface represents a contract for DAO for the {@link Holiday} model.
 * Note that all methods which returns the {@link Holiday} from the DB, will not
 * fill the model with the password, due to security reasons.
 * 
 * @author Jose Monteiro
 */
public interface HolidayDAO
{
  /**
   * Returns the holiday from the database matching the given ID, otherwise null.
   * 
   * @param id The ID of the holiday to be returned.
   * 
   * @return The holiday from the database matching the given ID, otherwise null.
   * 
   * @throws DAOException If something fails at database level.
   */
  public Holiday find(Long id) throws DAOException;
  
  /**
   * Returns a list of all holidays from the database ordered by holiday ID. The list is never null and
   * is empty when the database does not contain any holiday.
   * 
   * @return A list of all holidays from the database ordered by holiday ID.
   * 
   * @throws DAOException If something fails at database level.
   */
  public List<Holiday> list() throws DAOException;
  
  /**
   * Create the given holiday in the database. The holiday ID must be null, otherwise it will throw
   * IllegalArgumentException. After creating, the DAO will set the obtained ID the given holiday.
   * 
   * @param holiday The holiday to be updated in the database.
   * 
   * @throws IllegalArgumentException If the holiday ID is not null.
   * @throws DAOException If something fails at database level.
   */
  public void create(Holiday holiday) throws IllegalArgumentException, DAOException;
  
  /**
   * Update the given holiday in the database. The holiday ID must not be null, otherwise it will throw
   * IllegalArgumentException.
   * 
   * @param holiday The holiday to be updated in the database.
   * 
   * @throws IllegalArgumentException If the holiday ID is null.
   * @throws DAOException If something fails at database level.
   */
  public void update(Holiday holiday) throws IllegalArgumentException, DAOException;
  
  /**
   * Delete the given holiday in the database. After deleting, the DAO will set the ID of the given
   * holiday to null.
   * 
   * @param holiday The holiday to be deleted from the database.
   * 
   * @throws DAOException If something fails at database level.
   */
  public void delete(Holiday holiday) throws DAOException;
  
  /**
   * Returns true if the given date exist in the database.
   * 
   * @param date The date which is to be checked in the database.
   * 
   * @return True if the given date exist in the database.
   * 
   * @throws DAOException If something fails at database level.
   */
  public boolean existDate(Date date) throws DAOException;
}
