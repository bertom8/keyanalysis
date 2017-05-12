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

import twitter4j.Status;

public class TweetService {
	private static EntityManager em;
	public static boolean addTweet(Status t) {
		boolean succeeded = false;
		
		em = CreateService.createEntityManager();
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			Tweet tweet = new Tweet();
			
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
	
	public static void appendTweetFile(File file) {
		PrintWriter pw = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("data/twitter.txt"), true)));
			String txt;
			while ((txt = br.readLine()) != null) {
				pw.println(txt);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null)
				pw.close();
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			file.delete();
		}
	}
	
	private static void appendTweetFile(String txt) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("data/newtweets.txt"), true)));
			pw.println(txt);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null)
				pw.close();
		}
	}
}
