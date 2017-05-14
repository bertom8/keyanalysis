package org.keyanalysis.Services.DB;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.EntityManager;

import org.hibernate.HibernateException;
import org.keyanalysis.Model.Log;
import org.keyanalysis.Model.User;

import com.vaadin.ui.UI;

public class LogService {
	public static void AddLogEntry(final String action, final Object obj, final String type) {

		final EntityManager em = CreateService.createEntityManager();
		try {
			em.getTransaction().begin();
			final Log log = new Log();
			User user = null;
			if (obj == null) {
				user = (User) UI.getCurrent().getSession().getAttribute("USER");
			} else {
				user = (User) obj;
			}
			log.setUser(user);
			log.setAction(action);
			log.setType(type);
			log.setTime(new Timestamp(new Date().getTime()));

			em.persist(log);
			em.getTransaction().commit();
		} catch (final HibernateException e) {
			if (em.getTransaction() != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
				e.printStackTrace();
			}
		} finally {
			em.close();
		}

	}
}
