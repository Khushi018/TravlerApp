package dao;

import entities.Booking;
import entities.TravelPackage;
import exceptions.BookingException;
import exceptions.TravelPackageException;

import java.util.List;

public interface BookingDao {

    public Booking registerBooking(Booking booking) throws BookingException;

    public Booking getBookingFromBookingId(int bookingId) throws BookingException;

    public List<Booking> getAllBookings() throws BookingException;

    public TravelPackage getTravelPackageFromBookingId(int bookingId) throws BookingException, TravelPackageException;

}