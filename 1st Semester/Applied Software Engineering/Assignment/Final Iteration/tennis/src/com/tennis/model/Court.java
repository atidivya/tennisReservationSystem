package com.tennis.model;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * This class represents the Court model. This model class can be used throughout all
 * layers, the data layer, the controller layer and the view layer.
 * 
 * @author Jose Monteiro
 */
public class Court implements Serializable
{
  private static final long serialVersionUID = 1L;
  
  private Long id;
  private String name;
  private String surface;
  private LocalTime openingTime;
  private LocalTime closingTime;
  private Boolean indoor;
  private Boolean lighting;
  private String observations;
  
  public Long getId()
  {
    return id;
  }
  
  public void setId(Long id)
  {
    this.id = id;
  }
  
  public String getName()
  {
    return name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public String getSurface()
  {
    return surface;
  }
  
  public void setSurface(String surface)
  {
    this.surface = surface;
  }
  
  public LocalTime getOpeningTime()
  {
    return openingTime;
  }
  
  public void setOpeningTime(LocalTime openingTime)
  {
    this.openingTime = openingTime;
  }
  
  public LocalTime getClosingTime()
  {
    return closingTime;
  }
  
  public void setClosingTime(LocalTime closingTime)
  {
    this.closingTime = closingTime;
  }
  
  public Boolean isIndoor()
  {
    return indoor;
  }
  
  public void setIndoor(Boolean indoor)
  {
    this.indoor = indoor;
  }
  
  public Boolean isLighting()
  {
    return lighting;
  }
  
  public void setLighting(Boolean lighting)
  {
    this.lighting = lighting;
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
   * The court ID is unique for each Court. So this should compare Court by ID only.
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object other)
  {
    return (other instanceof Court) && (id != null) ? id.equals(((Court) other).id) : (other == this);
  }
  
  /**
   * The court ID is unique for each Court. So Court with same ID should return same hashcode.
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return (id != null) ? (this.getClass().hashCode() + id.hashCode()) : super.hashCode();
  }
  
  /**
   * Returns the String representation of this Court. Not required, it just pleases reading logs.
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return String.format("Court[id=%d, name=%s, surface=%s, openingTime=%s, closingTime=%s, indoor=%b, lighting=%b, observations=%s]",
            id, name, surface, openingTime, closingTime, indoor, lighting, observations);
  }
}
