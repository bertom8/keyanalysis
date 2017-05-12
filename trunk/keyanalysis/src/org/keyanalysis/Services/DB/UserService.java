package org.keyanalysis.Services.DB;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.HibernateException;
import org.keyanalysis.Model.Storage;
import org.keyanalysis.Model.User;

public class UserService {
	private static EntityManager em;
	
	public static boolean findAUser(final String username, User u) {
		boolean succeeded = false;
		em = CreateService.createEntityManager();
		User user = new User();
		user = em.find(User.class, username);
		if (user != null) {
			u.setPassword(user.getPassword());
			//u.setPicture(user.getPicture());
			//u.setStatistics(user.isStatistics());
			u.setName(user.getName());
			u.setDeleted(user.isDeleted());
			u.setStorage(user.getStorage());
		}
		if (u.getName() != "") 
			succeeded = true;
		em.close();
		return succeeded;
	}
	
	public static boolean changePassword(String username, String newPass) {
		boolean succeeded = false;
		
		em = CreateService.createEntityManager();
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			User u = null;
			u = em.find(User.class, username);
			if (u != null)
				u.setPassword(LoginService.hashing(newPass));
			em.merge(u);
			tx.commit();
			succeeded = true;
		}
		catch(HibernateException e) {
			if (tx != null && tx.isActive())
				tx.rollback();
			e.printStackTrace();
		}
		finally {
			em.close();
		}
		
		return succeeded;
	}

	public static boolean addUser(String name, String pass, int storageID) {
		boolean succeeded = false;
		
		em = CreateService.createEntityManager();
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			User u = new User();
			u.setName(name);
			u.setPassword(pass);
			u.setStorage(em.find(Storage.class, storageID));
			em.persist(u);
			tx.commit();
			succeeded = true;
		}
		catch(HibernateException e) {
			if (tx != null && tx.isActive())
				tx.rollback();
			e.printStackTrace();
		}
		finally {
			em.close();
		}
		
		return succeeded;
	}
}
