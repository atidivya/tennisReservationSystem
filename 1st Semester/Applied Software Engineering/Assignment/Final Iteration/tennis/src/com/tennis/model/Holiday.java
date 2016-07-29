package com.tennis.model;

import java.io.Serializable;
import java.util.Date;

/**
 * This class represents the Holiday model. This model class can be used throughout all
 * layers, the data layer, the controller layer and the view layer.
 * 
 * @author Atidivya
 */
public class Holiday implements Serializable
{
  private static final long serialVersionUID = 1L;
  
  private Long id;
  private Date date;
  private String name;
  private String observations;
  
  public Long getId()
  {
    return id;
  }
  
  public void setId(Long id)
  {
    this.id = id;
  }
  
  public Date getDate()
  {
    return date;
  }
  
  public void setDate(Date date)
  {
    this.date = date;
  }
  
  public String getName()
  {
    return name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public String getObservations()
  {
    return observations;
  }
  
  public void setObservations(String observations)
  {
    this.observations = observations;
  }
  
  /**
   * The holiday ID is unique for each Holiday. So this should compare Holiday by ID only.
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object other)
  {
    return (other instanceof Holiday) && (id != null) ? id.equals(((Holiday) other).id) : (other == this);
  }
  
  /**
   * The holiday ID is unique for each Holiday. So Holiday with same ID should return same hashcode.
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return (id != null) ? (this.getClass().hashCode() + id.hashCode()) : super.hashCode();
  }
  
  /**
   * Returns the String representation of this Holiday. Not required, it just pleases reading logs.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return String.format("Holiday[id=%d, date=%s, name=%s, observations=%s]",
            id, date, name, observations);
  }
}
