package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HotelRepo {
    Map<String, Hotel> hotelDB=new HashMap<>();
    Map<Integer, User> userDB=new HashMap<>();
    Map<String, Integer> countOfFacilityDB=new HashMap<>();
    Map<String, Booking> bookingDB=new HashMap<>();
    Map<Integer,Integer> noOfBookingDoneByUser=new HashMap<>();
    public String addHotel(Hotel hotel) {
        if(hotel.getHotelName() == null || hotel == null){
            return "FAILURE";
        }
        if(hotelDB.containsKey(hotel.getHotelName()) == true){
            return "FAILURE";
        }

        hotelDB.put(hotel.getHotelName(),hotel);
        int cnt=hotel.getFacilities().size();

        countOfFacilityDB.put(hotel.getHotelName(),cnt);

        return "SUCCESS";
    }

    public Integer addUser(User user) {
        Integer aadharNo=user.getaadharCardNo();
        userDB.put(aadharNo,user);
        noOfBookingDoneByUser.put(aadharNo,0);
        return aadharNo;
    }

    public String getHotelWithMostFacilities() {
      int mostFacilityCnt=0;

      for(Map.Entry<String,Integer> entry : countOfFacilityDB.entrySet()){
          if(mostFacilityCnt < entry.getValue()){
              mostFacilityCnt= entry.getValue();
          }
      }
        if(mostFacilityCnt == 0){
            return "";
        }
      String mostFacilityHotelName="";

        for(Map.Entry<String,Integer> entry : countOfFacilityDB.entrySet()){
            if(mostFacilityCnt == entry.getValue()){
                  if(mostFacilityHotelName.length() == 0){
                      mostFacilityHotelName= entry.getKey();
                  }
                  else{
                      if(entry.getKey().toLowerCase().charAt(0) < mostFacilityHotelName.toLowerCase().charAt(0) ){
                          mostFacilityHotelName= entry.getKey();
                      }
                  }
            }
        }

      return mostFacilityHotelName;
    }

    public int bookARoom(Booking booking) {
       String uniqueID= String.valueOf(UUID.randomUUID());

       bookingDB.put(uniqueID,booking);

       int totalAmount=0;

       int noOfRooms=booking.getNoOfRooms();

       String hotelName=booking.getHotelName();

       if(noOfRooms > hotelDB.get(hotelName).getAvailableRooms()){
           return -1;
       }

       int costPerNight=hotelDB.get(hotelName).getPricePerNight();

       totalAmount+=(costPerNight*noOfRooms);

       noOfBookingDoneByUser.put(booking.getBookingAadharCard(),noOfBookingDoneByUser.getOrDefault(booking.getBookingAadharCard(),0) + 1);

       return totalAmount;
    }

    public int getBookings(Integer aadharCard) {
        return noOfBookingDoneByUser.get(aadharCard);
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
       Hotel hotel=hotelDB.get(hotelName);
       List<Facility> facilities=hotel.getFacilities();
       boolean flag=false;

       for(Facility f : newFacilities){
          String a= f.name();
           for(Facility ef : facilities){
               String b=ef.name();
               if(b.equals(a)){
                   continue;
               }
               else{
                   flag=true;
                   facilities.add(f);
               }
           }
       }

       if(flag){
           hotel.setFacilities(facilities);
       }

       return hotel;
    }
}
