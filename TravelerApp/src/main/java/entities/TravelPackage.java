package entities;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class TravelPackage {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int packageid;
	private String packageName;
	private double price;
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Booking> bookings;
	public int getPackageid() {
		return packageid;
	}
	public void setPackageid(int packageid) {
		this.packageid = packageid;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Set<Booking> getBookings() {
		return bookings;
	}
	public void setBookings(Set<Booking> bookings) {
		this.bookings = bookings;
	}
	public TravelPackage(String packageName, double price, Set<Booking> bookings) {
		super();
		this.packageName = packageName;
		this.price = price;
		this.bookings = bookings;
	}
	@Override
	public String toString() {
		return "TravelPackage [packageid=" + packageid + ", packageName=" + packageName + ", price=" + price
				+ ", bookings=" + bookings + "]";
	}
	
	
	

}
