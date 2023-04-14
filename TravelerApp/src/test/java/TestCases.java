import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import dao.BookingDao;
import dao.TravelPackageDao;
import entities.Booking;
import entities.TravelPackage;
import exceptions.BookingException;
import exceptions.TravelPackageException;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import utilities.EMUtil;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCases {
	
	static BookingDao bDao;
	static TravelPackageDao tpDao;
	
	@Test
	@Order(1)
	void basicChecks() throws Exception {
		Assertions.assertAll(() -> {
			if(Class.forName("entities.Booking").getDeclaredFields().length >= 5 && Class.forName("entities.Booking").getDeclaredAnnotationsByType(Entity.class).length == 1)
			
			if(Class.forName("entities.TravelPackage").getDeclaredFields().length >= 4 && Class.forName("entities.TravelPackage").getDeclaredAnnotationsByType(Entity.class).length == 1)	
			
			if(BookingDao.class.isAssignableFrom(Class.forName("dao.BookingDaoImpl"))) {
				bDao = (BookingDao) Class.forName("dao.BookingDaoImpl").getDeclaredConstructor().newInstance();
			}
			
			if(TravelPackageDao.class.isAssignableFrom(Class.forName("dao.TravelPackageDaoImpl"))) {
				tpDao = (TravelPackageDao) Class.forName("dao.TravelPackageDaoImpl").getDeclaredConstructor().newInstance();
			}			
		});
	}
	
	@Test
	@Order(2)
	void registerBooking() throws Exception {
		Assertions.assertAll(() -> {
			
			EntityManager em = (EntityManager) Class.forName("utilities.EMUtil").getDeclaredMethod("provideEntityManager").invoke(EMUtil.class);
			cleanData(em);
			Booking booking = getBooking("Sahyadri tour and travels", "Kalsubai", LocalDate.now());
			
			Booking b = bDao.registerBooking(booking);
			
			if(b == null)
				Assertions.fail("registerBooking method is returning a null value object");
			
			if((int)Booking.class.getDeclaredMethod("getBookingId").invoke(b) == 0)
				Assertions.fail("registerBooking method is returning a object without Id");
			
			Booking b1 = em.find(Booking.class, Booking.class.getDeclaredMethod("getBookingId").invoke(b));
			if(b1 == null || !Booking.class.getDeclaredMethod("getTravelerName").invoke(b1).equals(Booking.class.getDeclaredMethod("getTravelerName").invoke(b)))
				Assertions.fail("registerBooking method is not registering data into the database correctly");

	        em.close();
		});
	}
	
	@Test
	@Order(3)
	void registerBookingException() throws Exception {
		Assertions.assertAll(() -> {
			Booking booking = getBooking("Kashmir travels", "Kullu", LocalDate.now());
            
			Assertions.assertDoesNotThrow(
					() -> bDao.registerBooking(booking),
					"registerBooking method is throwing exception even passing correct object"
					);
			
			Assertions.assertThrows(
					BookingException.class,
					() -> bDao.registerBooking(null),
					"registerBooking method is not throwing exception even passing incorrect object"
					);
		});
	}
	
	@Test
	@Order(4)
	void getBookingFromBookingId() throws Exception {
		Assertions.assertAll(() -> {
			EntityManager em = (EntityManager) Class.forName("utilities.EMUtil").getDeclaredMethod("provideEntityManager").invoke(EMUtil.class);
			cleanData(em);
			
			Booking registeredBooking = addBooking(em);
			Booking retrievedBooking = bDao.getBookingFromBookingId((int)Booking.class.getDeclaredMethod("getBookingId").invoke(registeredBooking));
			
			assertNotNull(retrievedBooking, "getBookingFromBookingId method is returning null object");
		    assertEquals(
		    		Booking.class.getDeclaredMethod("getBookingId").invoke(registeredBooking),
		    		Booking.class.getDeclaredMethod("getBookingId").invoke(retrievedBooking),
		    		"getAllBookings method is not returning the correct object"
		    		);
		    
		    Assertions.assertDoesNotThrow(
					() -> bDao.getBookingFromBookingId((int)Booking.class.getDeclaredMethod("getBookingId").invoke(retrievedBooking)),
					"getBookingFromBookingId method is throwing exception even object present in the database with given Id"
					);
			
			Assertions.assertThrows(
					BookingException.class,
					() -> bDao.getBookingFromBookingId(-1),
					"getBookingFromBookingId method is not throwing exception even passing incorrect Id"
					);
	        em.close();
		});
	}

	@Test
	@Order(5)
	void getAllBookings() throws Exception {
		Assertions.assertAll(() -> {
			EntityManager em = (EntityManager) Class.forName("utilities.EMUtil").getDeclaredMethod("provideEntityManager").invoke(EMUtil.class);
			cleanData(em);
			
			List<Booking> registeredBookings = addBookings(em);
			List<Booking> retrievedBookings = bDao.getAllBookings();
			
			assertNotNull(retrievedBookings, "getAllBookings method is returning null object");
		    assertEquals(
		    		registeredBookings.size(),
		    		retrievedBookings.size(),
		    		"getAllBookings method is not returning the all objects correctly"
		    		);
		    
		    Assertions.assertDoesNotThrow(
					() -> bDao.getAllBookings(),
					"getAllBookings method is throwing exception even object present in the database"
					);
			
		    em.clear();
		    cleanData(em);
			Assertions.assertThrows(
					BookingException.class,
					() -> bDao.getAllBookings(),
					"getAllBookings method is not throwing exception even for empty database"
					);
	        em.close();
		});
	}
	
	@Test
	@Order(6)
	void getTravelPackageFromBookingId() throws Exception {
		Assertions.assertAll(() -> {
			EntityManager em = (EntityManager) Class.forName("utilities.EMUtil").getDeclaredMethod("provideEntityManager").invoke(EMUtil.class);
			cleanData(em);
			
			Booking registeredBooking = addBooking(em);
			TravelPackage registeredTravelPackage = (TravelPackage)Booking.class.getDeclaredMethod("getTravelPackage").invoke(registeredBooking);
			TravelPackage retrievedTravelPackage = bDao.getTravelPackageFromBookingId((int)Booking.class.getDeclaredMethod("getBookingId").invoke(registeredBooking));
			
			assertNotNull(retrievedTravelPackage, "getTravelPackageFromBookingId method is returning null object");
		    assertEquals(
		    		TravelPackage.class.getDeclaredMethod("getPackageId").invoke(retrievedTravelPackage),
		    		TravelPackage.class.getDeclaredMethod("getPackageId").invoke(registeredTravelPackage),
		    		"getTravelPackageFromBookingId method is not returning the all objects correctly"
		    		);
		    
		    Assertions.assertDoesNotThrow(
					() -> bDao.getTravelPackageFromBookingId((int)Booking.class.getDeclaredMethod("getBookingId").invoke(registeredBooking)),
					"getTravelPackageFromBookingId method is throwing exception even object present in the database"
					);
			
		    em.clear();
		    cleanData(em);
			Assertions.assertThrows(
					BookingException.class,
					() -> bDao.getTravelPackageFromBookingId(-1),
					"getTravelPackageFromBookingId method is not throwing exception if object not found"
					);
	        em.close();
		});
	}
	
	@Test
	@Order(7)
	void addTravelPackage() throws Exception {
		Assertions.assertAll(() -> {
			EntityManager em = (EntityManager) Class.forName("utilities.EMUtil").getDeclaredMethod("provideEntityManager").invoke(EMUtil.class);
			cleanData(em);
			
			TravelPackage travelPackage = getTravelPackage("Swis curt", 70000.0);
			TravelPackage registeredTravelPackage = tpDao.addTravelPackage(travelPackage);
			
			assertNotNull(registeredTravelPackage, "addTravelPackage method is returning null object");
		    
			assertNotEquals(
		    		0,
		    		TravelPackage.class.getDeclaredMethod("getPackageId").invoke(registeredTravelPackage),
		    		"addTravelPackage method is not returning the all objects correctly"
		    		);
		    
		    Assertions.assertDoesNotThrow(
					() -> tpDao.addTravelPackage(travelPackage),
					"addTravelPackage method is throwing exception even passing correct object"
					);
			
			Assertions.assertThrows(
					TravelPackageException.class,
					() -> tpDao.addTravelPackage(null),
					"addTravelPackage method is not throwing exception while passing null object"
					);
	        em.close();
		});
	}
	
	@Test
	@Order(8)
	void updateTravelPackage() throws Exception {
		Assertions.assertAll(() -> {
			EntityManager em = (EntityManager) Class.forName("utilities.EMUtil").getDeclaredMethod("provideEntityManager").invoke(EMUtil.class);
			cleanData(em);
			
			TravelPackage travelPackage = addTravelPackage(em);
			TravelPackage.class.getDeclaredMethod("setPrice", double.class).invoke(
					travelPackage, 
					(double)TravelPackage.class.getDeclaredMethod("getPrice").invoke(travelPackage) + 2000
					);
			TravelPackage UpdatedTravelPackage = tpDao.updateTravelPackage(travelPackage);
			
			assertNotNull(UpdatedTravelPackage, "updateTravelPackage method is returning null object");
		    
			assertEquals(
					TravelPackage.class.getDeclaredMethod("getPrice").invoke(travelPackage),
		    		TravelPackage.class.getDeclaredMethod("getPrice").invoke(UpdatedTravelPackage),
		    		"updateTravelPackage method is not updating object correctly"
		    		);
		    
		    Assertions.assertDoesNotThrow(
					() -> tpDao.updateTravelPackage(travelPackage),
					"updateTravelPackage method is throwing exception even passing correct object"
					);
			
			Assertions.assertThrows(
					TravelPackageException.class,
					() -> tpDao.addTravelPackage(null),
					"updateTravelPackage method is not throwing exception while passing null object"
					);
	        em.close();
		});
	}
	
	@Test
	@Order(9)
	void deleteTravelPackage() throws Exception {
		Assertions.assertAll(() -> {
			EntityManager em = (EntityManager) Class.forName("utilities.EMUtil").getDeclaredMethod("provideEntityManager").invoke(EMUtil.class);
			cleanData(em);
			
			TravelPackage travelPackage = addTravelPackage(em);
			boolean isDeleted = tpDao.deleteTravelPackage((int)TravelPackage.class.getDeclaredMethod("getPackageId").invoke(travelPackage));
		    
			assertEquals(
					true,
		    		isDeleted,
		    		"deleteTravelPackage method is not returning value correctly"
		    		);
		    
			TravelPackage travelPackage1 = addTravelPackage(em);
		    Assertions.assertDoesNotThrow(
					() -> tpDao.deleteTravelPackage((int)TravelPackage.class.getDeclaredMethod("getPackageId").invoke(travelPackage1)),
					"deleteTravelPackage method is throwing exception even passing correct object"
					);
			
			Assertions.assertThrows(
					TravelPackageException.class,
					() -> tpDao.deleteTravelPackage(-1),
					"deleteTravelPackage method is not throwing exception while incorrect Id"
					);
	        em.close();
		});
	}
	

	@Test
	@Order(10)
	void getTravelPackageById() throws Exception {
		Assertions.assertAll(() -> {
			EntityManager em = (EntityManager) Class.forName("utilities.EMUtil").getDeclaredMethod("provideEntityManager").invoke(EMUtil.class);
			cleanData(em);
			
			TravelPackage registeredTravelPackage = addTravelPackage(em);
			TravelPackage retrievedTravelPackage = tpDao.getTravelPackageById((int)TravelPackage.class.getDeclaredMethod("getPackageId").invoke(registeredTravelPackage));
			
			assertNotNull(retrievedTravelPackage, "getTravelPackageById method is returning null object");
		    
			assertEquals(
		    		TravelPackage.class.getDeclaredMethod("getPackageId").invoke(retrievedTravelPackage),
		    		TravelPackage.class.getDeclaredMethod("getPackageId").invoke(registeredTravelPackage),
		    		"getTravelPackageById method is not returning the all objects correctly"
		    		);
		    
		    Assertions.assertDoesNotThrow(
					() -> tpDao.getTravelPackageById((int)TravelPackage.class.getDeclaredMethod("getPackageId").invoke(registeredTravelPackage)),
					"getTravelPackageById method is throwing exception even object present in the database"
					);
			
		    em.clear();
		    cleanData(em);
			Assertions.assertThrows(
					TravelPackageException.class,
					() -> tpDao.getTravelPackageById(-1),
					"getTravelPackageById method is not throwing exception if object not found"
					);
	        em.close();
		});
	}
	
	@Test
	@Order(11)
	void searchTravelPackages() throws Exception {
		Assertions.assertAll(() -> {
			EntityManager em = (EntityManager) Class.forName("utilities.EMUtil").getDeclaredMethod("provideEntityManager").invoke(EMUtil.class);
			cleanData(em);
			
			List<TravelPackage> registeredTravelPackages = addTravelPackages(em);
			List<TravelPackage> retrievedTravelPackage = tpDao.searchTravelPackages("travels");
			
			assertNotNull(retrievedTravelPackage, "searchTravelPackages method is returning null object");
		    
			assertEquals(
		    		registeredTravelPackages.size()-1,
		    		retrievedTravelPackage.size(),
		    		"searchTravelPackages method is not returning the all objects correctly"
		    		);
		    
		    Assertions.assertDoesNotThrow(
					() -> tpDao.searchTravelPackages("travels"),
					"searchTravelPackages method is throwing exception even object present in the database"
					);
		});
	}
	
	static Booking getBooking(String travelerName, String destination, LocalDate bookingDate) throws Exception {
		Booking booking = new Booking();
		Booking.class.getDeclaredMethod("setTravelerName", String.class).invoke(booking, travelerName);
		Booking.class.getDeclaredMethod("setDestination", String.class).invoke(booking, destination);
		Booking.class.getDeclaredMethod("setBookingDate", LocalDate.class).invoke(booking, bookingDate);
		Booking.class.getDeclaredMethod("setTravelPackage", TravelPackage.class).invoke(booking, getTravelPackage("Paris Vacation", 20000.0));
		return booking;
	}
	
	static TravelPackage getTravelPackage(String packageName, double price) throws Exception {
		TravelPackage tp = new TravelPackage();
		TravelPackage.class.getDeclaredMethod("setPackageName", String.class).invoke(tp, packageName);
		TravelPackage.class.getDeclaredMethod("setPrice", double.class).invoke(tp, price);
		return tp;
	}
	
	static Booking addBooking(EntityManager em) throws Exception{
		Booking booking = getBooking("Vindhya travels", "Omkareshwar", LocalDate.now());
		em.getTransaction().begin();
		em.persist(booking);
		em.getTransaction().commit();
		return booking;
	}
	
	static List<Booking> addBookings(EntityManager em) throws Exception{
		em.getTransaction().begin();
		em.persist(getBooking("Vindhya travels", "Omkareshwar", LocalDate.now()));
		em.persist(getBooking("Paric Vacation", "Ladakh", LocalDate.now()));
		em.persist(getBooking("Future travels", "Dubai", LocalDate.now()));
		em.getTransaction().commit();
		return em.createQuery("FROM Booking", Booking.class).getResultList();
	}
	
	static TravelPackage addTravelPackage(EntityManager em) throws Exception{
		TravelPackage tp = getTravelPackage("Special winter tour", 20000.0);
		em.getTransaction().begin();
		em.persist(tp);
		em.getTransaction().commit();
		return tp;
	}
	
	static List<TravelPackage> addTravelPackages(EntityManager em) throws Exception{
		em.getTransaction().begin();
		em.persist(getTravelPackage("Special winter tour & travels", 27000.0));
		em.persist(getTravelPackage("Paric Vacation", 20000.0));
		em.persist(getTravelPackage("Welcome tour & travels", 10000.0));
		em.getTransaction().commit();
		return em.createQuery("FROM TravelPackage", TravelPackage.class).getResultList();
	}
	
	static void cleanData(EntityManager em) {
		em.getTransaction().begin();
		em.createQuery("delete from Booking").executeUpdate();
		em.createQuery("delete from TravelPackage").executeUpdate();
		em.getTransaction().commit();
	}
}
