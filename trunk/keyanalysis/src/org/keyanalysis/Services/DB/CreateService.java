package org.keyanalysis.Services.DB;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class CreateService {

	public static EntityManager createEntityManager() {
		final EntityManagerFactory factory = Persistence.createEntityManagerFactory("org.keyanalysis");
		final EntityManager em = factory.createEntityManager();

		return em;
	}
}