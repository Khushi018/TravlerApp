package dao;

import java.util.List;

import entities.TravelPackage;
import exceptions.TravelPackageException;

public interface TravelPackageDao {
	
	public TravelPackage addTravelPackage(TravelPackage travelPackage) throws TravelPackageException;
	
	public TravelPackage updateTravelPackage(TravelPackage travelPackage) throws TravelPackageException;
	
	public boolean deleteTravelPackage(int travelPackageId) throws TravelPackageException;
	
	public TravelPackage getTravelPackageById(int travelPackageId) throws TravelPackageException;
	
	public List<TravelPackage> searchTravelPackages(String keyword) throws TravelPackageException;

}