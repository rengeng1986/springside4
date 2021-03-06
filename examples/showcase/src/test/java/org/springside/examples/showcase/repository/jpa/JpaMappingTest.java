package org.springside.examples.showcase.repository.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springside.modules.test.data.DataFixtures;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

@DirtiesContext
@ContextConfiguration(locations = { "/applicationContext.xml" })
//如果存在多个transactionManager，可以需显式指定
@TransactionConfiguration(transactionManager = "transactionManager")
public class JpaMappingTest extends SpringTransactionalTestCase {

	private static Logger logger = LoggerFactory.getLogger(JpaMappingTest.class);

	@PersistenceContext
	private EntityManager em;

	@Test
	public void allClassMapping() throws Exception {
		DataFixtures.executeScript(dataSource, "classpath:data/cleanup-data.sql", "classpath:data/import-data.sql");

		Metamodel model = em.getEntityManagerFactory().getMetamodel();
		for (EntityType entityType : model.getEntities()) {
			String entityName = entityType.getName();
			em.createQuery("select o from " + entityName + " o").getFirstResult();
			logger.info("ok: " + entityName);
		}
	}
}
