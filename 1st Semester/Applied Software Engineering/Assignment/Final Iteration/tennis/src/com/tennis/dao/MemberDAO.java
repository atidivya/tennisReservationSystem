package com.tennis.dao;

import java.util.List;

import com.tennis.model.Member;

/**
 * This interface represent a contract for a DAO for the {@link Member} model.
 * Note that all methods which returns the {@link Member} from the DB, will not
 * fill the model with the password, due to security reasons.
 * 
 * @author Jose Monteiro
 */
public interface MemberDAO
{
  /**
   * Returns the member from the database matching the given ID, otherwise null.
   * 
   * @param id The ID of the member to be returned.
   * 
   * @return The member from the database matching the given ID, otherwise null.
   * 
   * @throws DAOException If something fails at database level.
   */
  public Member find(Long id) throws DAOException;
  
  /**
   * Returns a list of all members from the database ordered by member ID. The list
   * is never null and is empty when the database does not contain any member. 
   * 
   * @return A list of all members from the database ordered by member ID.
   * 
   * @throws DAOException If something fails at database level.
   */
  public List<Member> list() throws DAOException;
  
  /**
   * Create the given member in the database. The member ID must be null, otherwise
   * it will throw IllegalArgumentException. After creating, the DAO will set the
   * obtained ID in the given member.
   * 
   * @param member The member to be created in the database.
   * 
   * @throws IllegalArgumentException If the member ID is not null.
   * @throws DAOException If something fails at database level.
   */
  public void create(Member member) throws IllegalArgumentException, DAOException;
  
  /**
   * Update the given member in the database. The member ID must not be null,
   * otherwise it will throw IllegalArgumentException.
   * 
   * @param member The member to be updated in the database.
   * 
   * @throws IllegalArgumentException If the member ID is null.
   * @throws DAOException If something fails at database level.
   */
  public void update(Member member) throws IllegalArgumentException, DAOException;
  
  /**
   * Delete the given member in the database. After deleting, the DAO will set the
   * ID of the given member to null.
   * 
   * @param member The member to be deleted from the database.
   * 
   * @throws DAOException If something fails at database level.
   */
  public void delete(Member member) throws DAOException;
}
