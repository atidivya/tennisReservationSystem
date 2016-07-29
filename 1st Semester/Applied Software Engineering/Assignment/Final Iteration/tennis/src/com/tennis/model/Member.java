package com.tennis.model;

import java.io.Serializable;

/**
 * This class represents the Member model. This model class can be used throughout all
 * layers, the data layer, the controller layer and the view layer.
 * 
 * @author Jose Monteiro
 */
public class Member implements Serializable
{
  private static final long serialVersionUID = 1L;
  
  private Long id;
  private String name;
  private String email;
  private String phone;
  private String address;
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
  
  public String getEmail()
  {
    return email;
  }
  
  public void setEmail(String email)
  {
    this.email = email;
  }
  
  public String getPhone()
  {
    return phone;
  }
  
  public void setPhone(String phone)
  {
    this.phone = phone;
  }
  
  public String getAddress()
  {
    return address;
  }
  
  public void setAddress(String address)
  {
    this.address = address;
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
   * The member ID is unique for each Member. So this should compare Member by ID only.
   * 
   * @see java.lang.Object#equals(java.lang.Object) 
   */
  @Override
  public boolean equals(Object other)
  {
    return (other instanceof Member) && (id != null) ? id.equals(((Member) other).id) : (other == this);
  }
  
  /**
   * The member ID is unique for each Member. So Member with same ID should return same hashcode.
   * 
   * @see java.lang.Object#hashCode() 
   */
  @Override
  public int hashCode()
  {
    return (id != null) ? (this.getClass().hashCode() + id.hashCode()) : super.hashCode();
  }
  
  /**
   * Returns the String representation of this Member. Not required, it just pleases reading logs.
   * 
   * @see java.lang.Object#toString() 
   */
  @Override
  public String toString()
  {
    return String.format("Member[id=%d, name=%s, email=%s, phone=%s, address=%s, observations=%s]",
            id, name, email, phone, address, observations);
  }
}
