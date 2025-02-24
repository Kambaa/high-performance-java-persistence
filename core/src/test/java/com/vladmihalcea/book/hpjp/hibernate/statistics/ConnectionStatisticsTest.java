package com.vladmihalcea.book.hpjp.hibernate.statistics;

import com.vladmihalcea.book.hpjp.util.AbstractTest;
import com.vladmihalcea.book.hpjp.util.providers.entity.BlogEntityProvider;
import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.resource.jdbc.spi.PhysicalConnectionHandlingMode;
import org.hibernate.stat.Statistics;
import org.hibernate.stat.internal.StatisticsInitiator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Vlad Mihalcea
 */
public class ConnectionStatisticsTest extends AbstractTest {

    @Override
    protected Class<?>[] entities() {
        return new Class[] {
            Post.class
        };
    }

    protected void additionalProperties(Properties properties) {
        properties.put(
            AvailableSettings.GENERATE_STATISTICS,
            Boolean.TRUE.toString()
        );

        properties.put(
            StatisticsInitiator.STATS_BUILDER,
            TransactionStatisticsFactory.class.getName()
        );
    }

    @Test
    public void test() {
        int iterations = 5;

        for (long i = 1; i <= iterations; i++) {
            final long currentIteration = i;
            doInJPA(entityManager -> {
                Post post = new Post();
                post.setTitle(
                    String.format(
                        "High-Performance Java Persistence, Part %d", currentIteration
                    )
                );
                entityManager.persist(post);

                Number postCount = entityManager.createQuery(
                    "select count(p) from Post p", Number.class)
                    .getSingleResult();

                assertEquals(currentIteration, postCount.longValue());
            });
        }
    }

    @Test
    public void testStatistics() {
        doInJPA(entityManager -> {
            Session session = entityManager.unwrap(Session.class);

            Statistics statistics = session.getSessionFactory().getStatistics();
            assertTrue(statistics instanceof TransactionStatistics);
        });
    }

    @Entity(name = "Post")
    @Table(name = "post")
    public static class Post {

        @Id
        //To get an extra connection
        @GeneratedValue(strategy = GenerationType.TABLE)
        private Long id;

        private String title;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
