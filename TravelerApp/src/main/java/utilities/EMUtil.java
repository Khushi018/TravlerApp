package utilities;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EMUtil {
	private static EntityManagerFactory emf;
	static {
		emf = Persistence.createEntityManagerFactory("TravelerApp");
		
	}
	public static EntityManager getConn() {
		return emf.createEntityManager();
	}
	 
}
