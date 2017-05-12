package org.keyanalysis.Services.DB;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.HibernateException;
import org.keyanalysis.Model.Storage;
import org.keyanalysis.Model.User;

public class StorageService {
	private static EntityManager em;
	
	public static boolean getStorage(final User u, Storage s) {
		boolean succed = false;
		em = CreateService.createEntityManager();
		Storage stor = em.find(Storage.class, u.getStorage().getId());
		if (stor != null) {
			s.setId(stor.getId());
			s.setDeleted(stor.isDeleted());
			succed = true;
		}
		em.close();
		return succed;
	}

	public static int addStorage() {
		em = CreateService.createEntityManager();
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			Storage s = new Storage();
			em.persist(s);
			em.flush();
			int id =  s.getId();
			tx.commit();
			return id;
		} catch(HibernateException e) {
			if (tx != null && tx.isActive())
				tx.rollback();
			e.printStackTrace();
		}
		finally {
			em.close();

		}
		return 0;
	}
}
