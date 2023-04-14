package dao;

import java.util.List;

import entities.Booking;
import entities.TravelPackage;
import exceptions.BookingException;
import exceptions.TravelPackageException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import utilities.EMUtil;

public class BokkingDaoImpl implements BookingDao {

	@Override
	public Booking registerBooking(Booking booking) throws BookingException {

		EntityManager em = EMUtil.getConn();
		if (em.find(Booking.class, booking.getBookingId()) != null)
			throw new BookingException("Already availabel");
		em.getTransaction().begin();
		em.persist(booking);
		em.getTransaction().commit();
		return booking;
	}

	@Override
	public Booking getBookingFromBookingId(int bookingId) throws BookingException {

		EntityManager em = EMUtil.getConn();
		Booking b = em.find(Booking.class, bookingId);
		if (em.find(Booking.class, bookingId) == null)
			throw new BookingException("NO Aval. ");

		return b;
	}

	@Override
	public List<Booking> getAllBookings() throws BookingException {
		String s = "select b from Boooking b";
		EntityManager em = EMUtil.getConn();
		Query q = em.createQuery(s);
		List<Booking> list = q.getResultList();
		if (list.isEmpty()) {
			throw new BookingException("NOT availabel");
		}
		return list;

	}

	@Override
	public TravelPackage getTravelPackageFromBookingId(int bookingId) throws BookingException, TravelPackageException {
		EntityManager entityManager = EMUtil.getConn();
		Booking booking = entityManager.find(Booking.class, bookingId);
		if (booking == null) {
			throw new BookingException("Booking not found");
		}
		TravelPackage t = booking.getTravelPackage();
		if (t == null) {

			throw new TravelPackageException("Error retrieving travel package for booking with ID " + bookingId);
		}
		return booking.getTravelPackage();
	}

}
