package dao;

import java.util.List;

import entities.TravelPackage;
import exceptions.TravelPackageException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import utilities.EMUtil;

public class TravelPackageDaoImpl implements TravelPackageDao {

	@Override
	public TravelPackage addTravelPackage(TravelPackage travelPackage) throws TravelPackageException {
		// TODO Auto-generated method stub
		EntityManager entityManager = EMUtil.getConn();
		 entityManager.persist(travelPackage);
         return travelPackage;
	}

	@Override
	public TravelPackage updateTravelPackage(TravelPackage travelPackage) throws TravelPackageException {
		// TODO Auto-generated method stub
		EntityManager entityManager = EMUtil.getConn();
		return entityManager.merge(travelPackage);
//		return null;
	}

	@Override
	public boolean deleteTravelPackage(int travelPackageId) throws TravelPackageException {
		// TODO Auto-generated method stub
		EntityManager entityManager = EMUtil.getConn();
		 TravelPackage travelPackage = entityManager.find(TravelPackage.class, travelPackageId);
         if (travelPackage == null) {
             throw new TravelPackageException("Travel package not found");
         }
         entityManager.remove(travelPackage);
         return true;
		
	}

	@Override
	public TravelPackage getTravelPackageById(int travelPackageId) throws TravelPackageException {
		EntityManager entityManager = EMUtil.getConn();
		 TravelPackage travelPackage = entityManager.find(TravelPackage.class, travelPackageId);
         if (travelPackage == null) {
             throw new TravelPackageException("Travel package not found");
         }
         return travelPackage;
	}

	@Override
	public List<TravelPackage> searchTravelPackages(String keyword) throws TravelPackageException {
		EntityManager entityManager = EMUtil.getConn();
		TypedQuery<TravelPackage> query = entityManager.createQuery(
                "SELECT t FROM TravelPackage t WHERE t.name LIKE :keyword OR t.description LIKE :keyword",
                TravelPackage.class);
        query.setParameter("keyword", "%" + keyword + "%");
        return query.getResultList();
	}

}
