package com.tennis.model;

import java.io.Serializable;
import java.util.Date;

/**
 * This class represents the Booking model. This model class can be used throughout all
 * layers, the data layer, the controller layer and the view layer.
 * 
 * @author Jose Monteiro
 */
public class Booking implements Serializable
{
  private static final long serialVersionUID = 1L;
  
  private Long id;
  private Court court;
  private Date startTime;
  private String type;
  private Member member;
  private String observations;
  
  public Long getId()
  {
    return id;
  }
  
  public void setId(Long id)
  {
    this.id = id;
  }
  
  public Court getCourt()
  {
    return court;
  }
  
  public void setCourt(Court court)
  {
    this.court = court;
  }
  
  public Date getStartTime()
  {
    return startTime;
  }
  
  public void setStartTime(Date startTime)
  {
    this.startTime = startTime;
  }
  
  public String getType()
  {
    return type;
  }
  
  public void setType(String type)
  {
    this.type = type;
  }
  
  public Member getMember()
  {
    return member;
  }
  
  public void setMember(Member member)
  {
    this.member = member;
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
   * The booking ID is unique for each Booking. So this should compare Booking by ID only.
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object other)
  {
    return (other instanceof Booking) && (id != null) ? id.equals(((Booking) other).id) : (other == this);
  }
  
  /**
   * The booking ID is unique for each Booking. So Booking with same ID should return same hashcode.
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return (id != null) ? (this.getClass().hashCode() + id.hashCode()) : super.hashCode();
  }
  
  /**
   * Returns the String representation of this Booking. Not required, it just pleases reading logs.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return String.format("Booking[id=%d, court=%s, startTime=%s, type=%s, member=%s, observations=%s]",
            id, court, startTime, type, member, observations);
  }
}
