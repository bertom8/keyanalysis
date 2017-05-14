package org.keyanalysis.Model;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import twitter4j.Status;

public class DAO {
	MongoClient client = null;
	MongoDatabase db = null;

	public DAO() {
	}

	public boolean addTweet(final Status t) {
		boolean success = false;
		try {
			this.client = new MongoClient("localhost", 27017);
			this.db = this.client.getDatabase("tweetDatabase");

			final MongoCollection<Document> col = this.db.getCollection("tweets");
			final Document d = new Document();
			d.append("date", t.getCreatedAt()).append("retweetId", t.getCurrentUserRetweetId())
					.append("favoriteCount", t.getFavoriteCount()).append("id", t.getId())
					.append("inReplyToScreenName", t.getInReplyToScreenName())
					.append("inReplyToStatusId", t.getInReplyToStatusId())
					.append("inReplyToUserId", t.getInReplyToUserId()).append("lang", t.getLang())
					.append("quotedStatusId", t.getQuotedStatusId()).append("retweetCount", t.getRetweetCount())
					.append("source", t.getSource()).append("user", t.getUser().getName()).append("text", t.getText());

			if (t.getGeoLocation() == null) {
				d.append("geoLatitude", "null");
				d.append("geoLongitude", "null");
			} else {
				d.append("geoLatitude", t.getGeoLocation().getLatitude());
				d.append("geoLongitude", t.getGeoLocation().getLongitude());
			}

			if (t.getPlace() == null) {
				d.append("country", "null");
				d.append("countryCode", "null");
			} else {
				d.append("country", t.getPlace().getCountry());
				d.append("countryCode", t.getPlace().getCountryCode());
			}

			/*
			 * d.put("date", t.getCreatedAt()); d.put("user",
			 * t.getUser().getName()); d.put("lang", t.getLang()); d.put("text",
			 * t.getText());
			 */

			col.insertOne(d);
			success = true;
		} catch (final MongoException e) {
			e.printStackTrace();
		} finally {
			this.client.close();
		}
		// Document found = col.find(
		// com.mongodb.client.model.Filters.eq("lang", "hu") ).first();

		return success;
	}

	public boolean addTweet(final Tweet t) {
		boolean success = false;
		try {
			this.client = new MongoClient("localhost", 27017);
			this.db = this.client.getDatabase("tweetDatabase");

			final MongoCollection<Document> col = this.db.getCollection("tweetsFromWorld");
			final Document d = new Document();
			d.append("date", t.getDate()).append("retweetId", t.getRetweetId())
					.append("favoriteCount", t.getFavoriteCount()).append("id", t.getId())
					.append("inReplyToScreenName", t.getInReplyToScreenName())
					.append("inReplyToStatusId", t.getInReplyToStatusId())
					.append("inReplyToUserId", t.getInReplyToUserId()).append("lang", t.getLang())
					.append("quotedStatusId", t.getQuotedStatusId()).append("retweetCount", t.getRetweetCount())
					.append("source", t.getSource()).append("user", t.getUser()).append("text", t.getText())
					.append("geoLatitude", t.getGeoLatitude()).append("geoLongitude", t.getGeoLongitude())
					.append("country", t.getCountry()).append("countryCode", t.getCountryCode());

			/*
			 * d.put("date", t.getCreatedAt()); d.put("user",
			 * t.getUser().getName()); d.put("lang", t.getLang()); d.put("text",
			 * t.getText());
			 */

			col.insertOne(d);
			success = true;
		} catch (final MongoException e) {
			e.printStackTrace();
		} finally {
			this.client.close();
		}
		// Document found = col.find(
		// com.mongodb.client.model.Filters.eq("lang", "hu") ).first();

		return success;
	}
}
