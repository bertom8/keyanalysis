package org.keyanalysis.Services.DB;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.HibernateException;
import org.keyanalysis.Model.Item;
import org.keyanalysis.Model.User;

import com.vaadin.server.VaadinSession;

public class ItemService {
	private static EntityManager em;

	public static int addItem(final String filePath, final String filename, final double bench) {
		em = CreateService.createEntityManager();
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			final Item i = new Item();
			i.setDeleted(false);
			i.setName(filename);
			i.setStorage(((User) VaadinSession.getCurrent().getAttribute("USER")).getStorage());
			i.setTime(new Timestamp(new Date().getTime()));
			i.setFilePath(filePath);
			i.setBenchmark(bench);
			em.persist(i);
			em.flush();
			final int id = i.getId();
			tx.commit();
			return id;
		} catch (final HibernateException e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			em.close();

		}
		return 0;
	}

	public static boolean addItem(final Item item) {
		boolean success = false;
		if (item == null) {
			throw new IllegalArgumentException();
		}
		em = CreateService.createEntityManager();
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			em.persist(item);
			em.flush();
			tx.commit();
			success = true;
		} catch (final HibernateException e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			em.close();

		}
		return success;
	}

}
