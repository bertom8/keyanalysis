package org.keyanalysis.Services.DB;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.HibernateException;
import org.keyanalysis.Model.Storage;
import org.keyanalysis.Model.User;

public class UserService {
	private static EntityManager em;

	public static boolean findAUser(final String username, final User u) {
		boolean succeeded = false;
		em = CreateService.createEntityManager();
		User user = new User();
		user = em.find(User.class, username);
		if (user != null) {
			u.setPassword(user.getPassword());
			// u.setPicture(user.getPicture());
			// u.setStatistics(user.isStatistics());
			u.setName(user.getName());
			u.setDeleted(user.isDeleted());
			u.setStorage(user.getStorage());
		}
		if (u.getName() != "") {
			succeeded = true;
		}
		em.close();
		return succeeded;
	}

	public static boolean changePassword(final String username, final String newPass) {
		boolean succeeded = false;

		em = CreateService.createEntityManager();
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			User u = null;
			u = em.find(User.class, username);
			if (u != null) {
				u.setPassword(LoginService.hashing(newPass));
			}
			em.merge(u);
			tx.commit();
			succeeded = true;
		} catch (final HibernateException e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			em.close();
		}

		return succeeded;
	}

	public static boolean addUser(final String name, final String pass, final int storageID) {
		boolean succeeded = false;

		em = CreateService.createEntityManager();
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			final User u = new User();
			u.setName(name);
			u.setPassword(pass);
			u.setStorage(em.find(Storage.class, storageID));
			em.persist(u);
			tx.commit();
			succeeded = true;
		} catch (final HibernateException e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			em.close();
		}

		return succeeded;
	}
}
