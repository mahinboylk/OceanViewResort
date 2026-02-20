package com.oceanview.model;

import java.sql.Date;

public class Reservation {

    private int    reservationId;
    private String guestName;
    private String address;
    private String contactNumber;
    private String roomType;
    private Date   checkIn;
    private Date   checkOut;
    private double totalAmount;

    public int    getReservationId()  { return reservationId; }
    public String getGuestName()      { return guestName; }
    public String getAddress()        { return address; }
    public String getContactNumber()  { return contactNumber; }
    public String getRoomType()       { return roomType; }
    public Date   getCheckIn()        { return checkIn; }
    public Date   getCheckOut()       { return checkOut; }
    public double getTotalAmount()    { return totalAmount; }

    public void setReservationId(int reservationId)    { this.reservationId = reservationId; }
    public void setGuestName(String guestName)         { this.guestName = guestName; }
    public void setAddress(String address)             { this.address = address; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setRoomType(String roomType)           { this.roomType = roomType; }
    public void setCheckIn(Date checkIn)               { this.checkIn = checkIn; }
    public void setCheckOut(Date checkOut)             { this.checkOut = checkOut; }
    public void setTotalAmount(double totalAmount)     { this.totalAmount = totalAmount; }
}