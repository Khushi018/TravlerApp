package entities;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/*
 *
 */
@Entity
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int bookingId;

	private String travelerName;
	
	private String destination;
	
	private LocalDate bookingDate;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private TravelPackage travelPackage;

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public String getTravelerName() {
		return travelerName;
	}

	public void setTravelerName(String travelerName) {
		this.travelerName = travelerName;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public LocalDate getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(LocalDate bookingDate) {
		this.bookingDate = bookingDate;
	}

	public TravelPackage getTravelPackage() {
		return travelPackage;
	}

	public void setTravelPackage(TravelPackage travelPackage) {
		this.travelPackage = travelPackage;
	}

	public Booking(String travelerName, String destination, LocalDate bookingDate, TravelPackage travelPackage) {
		super();
		this.travelerName = travelerName;
		this.destination = destination;
		this.bookingDate = bookingDate;
		this.travelPackage = travelPackage;
	}

	@Override
	public String toString() {
		return "Booking [bookingId=" + bookingId + ", travelerName=" + travelerName + ", destination=" + destination
				+ ", bookingDate=" + bookingDate + ", travelPackage=" + travelPackage + "]";
	}
	
	
	
}
