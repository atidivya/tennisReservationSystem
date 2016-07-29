package com.tennis.dao;

import java.util.List;

import com.tennis.model.Court;

/**
 * This interface represents a contract for a DAO for the {@link Court} model.
 * Note that all methods which returns the {@link Court} from the DB, will not
 * fill the model with the password, due to security reasons.
 * 
 * @author Jose Monteiro
 */
public interface CourtDAO
{
  /**
   * Returns the court from the database matching the given ID, otherwise null.
   * 
   * @param id The ID of the court to be returned.
   * 
   * @return The court from the database matching the given ID, otherwise null.
   * 
   * @throws DAOException If something fails at database level.
   */
  public Court find(Long id) throws DAOException;
  
  /**
   * Returns a list of all courts from the database ordered by court ID. The list is never null and
   * is empty when the database does not contain any court.
   * 
   * @return A list of all courts from the database ordered by court ID.
   * 
   * @throws DAOException If something fails at database level.
   */
  public List<Court> list() throws DAOException;
  
  /**
   * Create the given court in the database. The court ID must be null, otherwise it will throw
   * IllegalArgumentException. After creating, the DAO will set the obtained ID in the given court.
   * 
   * @param court The court to be created in the database.
   * 
   * @throws IllegalArgumentException If the court ID is not null.
   * @throws DAOException If something fails at database level.
   */
  public void create(Court court) throws IllegalArgumentException, DAOException;
  
  /**
   * Update the given court in the database. The court ID must not be null, otherwise it will throw
   * IllegalArgumentException.
   * 
   * @param court The court to be updated in the database.
   * 
   * @throws IllegalArgumentException If the court ID is null.
   * @throws DAOException If something fails at database level.
   */
  public void update(Court court) throws IllegalArgumentException, DAOException;
  
  /**
   * Delete the given court in the database. After deleting, the DAO will set the ID of the given
   * court to null.
   * 
   * @param court The court to be deleted from the database.
   * 
   * @throws DAOException If something fails at database level.
   */
  public void delete(Court court) throws DAOException;
  
  /**
   * Returns true if the given name exist in the database.
   * 
   * @param name The name which is to be checked in the database.
   * 
   * @return True if the given name exist in the database.
   * 
   * @throws DAOException If something fails at database level.
   */
  public boolean existName(String name) throws DAOException;
}
