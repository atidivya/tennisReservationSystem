package com.tennis;

import java.text.SimpleDateFormat;
import java.util.List;
import java.time.LocalTime;

import com.tennis.dao.CourtDAO;
import com.tennis.dao.DAOFactory;
import com.tennis.dao.HolidayDAO;
import com.tennis.dao.MemberDAO;
import com.tennis.model.Court;
import com.tennis.model.Holiday;
import com.tennis.model.Member;

public class DAOTest
{
  public static void main(String[] args) throws Exception
  {
    // Obtain DAOFactory
    DAOFactory tennis = DAOFactory.getInstance("tennis.jdbc");
    System.out.println("DAOFactory successfully obtained: " + tennis);
    
    // Obtain MemberDAO
    MemberDAO memberDAO = tennis.getMemberDAO();
    System.out.println("MemberDAO successfully obtained: " + memberDAO);
    
    // Obtain CourtDAO
    CourtDAO courtDAO = tennis.getCourtDAO();
    System.out.println("CourtDAO successfully obtained: " + courtDAO);
    
    // Obtain HolidayDAO
    HolidayDAO holidayDAO = tennis.getHolidayDAO();
    System.out.println("HolidayDAO successfully obtained: " + holidayDAO);
    
    // Create member
    Member member = new Member();
    member.setName("Jose Monteiro");
    member.setEmail("jasfmonteiro.percelada@gmail.com");
    memberDAO.create(member);
    System.out.println("Member successfully created: " + member);
    
    // Create another member
    Member anotherMember = new Member();
    anotherMember.setName("Vanda Sardinha");
    anotherMember.setEmail("vandafransico-00@gmail.com");
    anotherMember.setPhone("963953047");
    anotherMember.setAddress("Calheta");
    memberDAO.create(anotherMember);
    System.out.println("Another member successfully created: " + anotherMember);
    
    // Update member
    member.setPhone("963532019");
    member.setAddress("Tabua");
    memberDAO.update(member);
    System.out.println("Member successfully updated: " + member);
    
    // List all members
    List<Member> members = memberDAO.list();
    System.out.println("List of members successfully queried: " + members);
    System.out.println("Thus, amount of members in database is: " + members.size());
    
    // Create court
    Court court = new Court();
    court.setName("Court0001");
    court.setSurface("Clay");
    court.setOpeningTime(LocalTime.of(9, 0));
    court.setClosingTime(LocalTime.of(17, 0));
    court.setIndoor(false);
    court.setLighting(false);
    courtDAO.create(court);
    System.out.println("Court successfully created: " + court);
    
    // Create another court
    Court anotherCourt = new Court();
    anotherCourt.setName("Court0002");
    anotherCourt.setSurface("Grass");
    anotherCourt.setOpeningTime(LocalTime.of(9, 0));
    anotherCourt.setClosingTime(LocalTime.of(21, 0));
    anotherCourt.setIndoor(false);
    anotherCourt.setLighting(true);
    anotherCourt.setObservations("Grass courts are the fastest type of courts in common use.");
    courtDAO.create(anotherCourt);
    System.out.println("Another court successfully created: " + anotherCourt);
    
    // Update court
    court.setClosingTime(LocalTime.of(20, 0));
    court.setLighting(true);
    courtDAO.update(court);
    System.out.println("Court successfully updated: " + court);
    
    // List all courts
    List<Court> courts = courtDAO.list();
    System.out.println("List of courts successfully queried: " + courts);
    System.out.println("Thus, amount of courts in database is: " + courts.size());
    
    // Create holiday
    Holiday holiday = new Holiday();
    holiday.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2016-09-11"));
    holidayDAO.create(holiday);
    
    // Create another holiday
    Holiday anotherHoliday = new Holiday();
    anotherHoliday.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2016-12-25"));
    anotherHoliday.setName("Christmas Day");
    holidayDAO.create(anotherHoliday);
    
    // List all holidays
    List<Holiday> holidays = holidayDAO.list();
    System.out.println("List of holidays successfully queried: " + holidays);
    System.out.println("Thus, amount of holidays in database is: " + holidays.size());
    
    // Delete member
    memberDAO.delete(member);
    System.out.println("Member successfully deleted: " + member);
    
    // Get another member by id
    Member foundAnotherMember = memberDAO.find(anotherMember.getId());
    System.out.println("Another user queried: " + foundAnotherMember);
    
    // Delete another member
    memberDAO.delete(foundAnotherMember);
    System.out.println("Another user sucessfully deleted: " + foundAnotherMember);
    
    // List all members again
    members = memberDAO.list();
    System.out.println("List of members successfully queried: " + members);
    System.out.println("Thus, amount of members in database is: " + members.size());
    
    // Delete court
    courtDAO.delete(court);
    System.out.println("Court successfully deleted: " + court);
    
    // Check if name of court exists
    boolean exist = courtDAO.existName("Court0001");
    System.out.println("This name should not exist anymore, so this should print false: " + exist);
    
    // Delete another court
    courtDAO.delete(anotherCourt);
    System.out.println("Another court successfully deleted: " + anotherCourt);
    
    // List all courts again
    courts = courtDAO.list();
    System.out.println("List of courts successfully queried: " + courts);
    System.out.println("Thus, amount of courts in database is: " + courts.size());
    
    // Delete holiday
    holidayDAO.delete(holiday);
    System.out.println("Holiday successfully deleted: " + holiday);
    
    // Delete another holiday
    holidayDAO.delete(anotherHoliday);
    System.out.println("Another holiday successfully deleted: " + anotherHoliday);
    
    // List all holidays again
    holidays = holidayDAO.list();
    System.out.println("List of holidays successfully queried: " + holidays);
    System.out.println("Thus, amount of holidays in database is: " + holidays.size());
  }
}
