package org.keyanalysis.Services.DB;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.HibernateException;
import org.keyanalysis.Model.Tweet;

import com.vaadin.server.VaadinServlet;

import twitter4j.Status;

public class TweetService {
	private static EntityManager em;

	public static boolean addTweet(final Status t) {
		boolean succeeded = false;

		em = CreateService.createEntityManager();
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			final Tweet tweet = new Tweet();

			tweet.setDate(t.getCreatedAt());
			tweet.setRetweetId(t.getCurrentUserRetweetId());
			tweet.setFavoriteCount(t.getFavoriteCount());
			tweet.setId(t.getId());
			tweet.setInReplyToScreenName(t.getInReplyToScreenName());
			tweet.setInReplyToStatusId(t.getInReplyToStatusId());
			tweet.setInReplyToUserId(t.getInReplyToUserId());
			tweet.setLang(t.getLang());
			tweet.setQuotedStatusId(t.getQuotedStatusId());
			tweet.setRetweetCount(t.getRetweetCount());
			tweet.setSource(t.getSource());
			tweet.setUser(t.getUser().getName());
			tweet.setText(t.getText());

			if (t.getGeoLocation() == null) {
				tweet.setGeoLatitude(0);
				tweet.setGeoLongitude(0);
			} else {
				tweet.setGeoLatitude(t.getGeoLocation().getLatitude());
				tweet.setGeoLongitude(t.getGeoLocation().getLongitude());
			}

			if (t.getPlace() == null) {
				tweet.setCountry(null);
				tweet.setCountryCode(null);
			} else {
				tweet.setCountry(t.getPlace().getCountry());
				tweet.setCountryCode(t.getPlace().getCountryCode());
			}
			em.persist(tweet);
			tx.commit();
			appendTweetFile(tweet.getText());
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

	public static void appendTweetFile(final File file, final String end) {
		if (!file.exists()) {
			System.out.println("Nincs ilyen file");
		}
		PrintWriter pw = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(
					VaadinServlet.getCurrent().getServletContext().getRealPath("") + "uploadDatas/twitter.txt" + end),
					true)));
			String txt;
			while ((txt = br.readLine()) != null) {
				pw.println(txt);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (br != null) {
				try {
					br.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
			file.delete();
		}
	}

	public static void appendTweetFile(final String txt) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(
					VaadinServlet.getCurrent().getServletContext().getRealPath("") + "/uploadDatas/newTweets.txt"),
					true)));
			pw.println(txt);
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}
}
