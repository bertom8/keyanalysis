package org.keyanalysis.Services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.keyanalysis.Model.DAO;
import org.keyanalysis.Model.Tweet;
import org.keyanalysis.Services.DB.TweetService;
import org.keyanalysis.View.KeyanalysisUI;

import com.google.gson.JsonObject;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

import twitter4j.Query;
import twitter4j.Query.ResultType;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TweetSearcher {
	public static void searchHunTweets(long num) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
	    cb.setDebugEnabled(true)
	            .setOAuthConsumerKey("ORlWMuKHZL12IsBPYUpxrMlLR")
	            .setOAuthConsumerSecret("oaicGQdctmawow6KMJ95uJ9nV0knjliazHpSE3pDQb4IHeIHMf")
	            .setOAuthAccessToken("788766851123863553-NeP7lLtvuhxi79rU0XiUR57ZhRaMwGz")
	            .setOAuthAccessTokenSecret("v3mS0NzwyUHbFi0UUR38g6WnxlGZ9xVv0amdg8jOPOvD3");
	    TwitterFactory tf = new TwitterFactory(cb.build());
	    Twitter twitter = tf.getInstance();
	    Query query = new Query("a");
	    query.count(100);
	    query.setResultType(ResultType.recent);
	    query.setUntil(new SimpleDateFormat("YYYY-MM-dd").format(new Date(num)));
	    query.setSince(new SimpleDateFormat("YYYY-MM-dd").format(new Date(num - 10800000)));
	    //query.setMaxId(num);
	    //query.setSinceId(num);;
	    query.setLang("hu");
	    QueryResult result = null;
		try {
			result = twitter.search(query);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		if (result == null)
			return;
		System.out.println(result.getTweets().size());
	    for (Status tweet : result.getTweets()) {
	        System.out.println("@" + tweet.getUser().getScreenName() + ":" + tweet.getText());
	        TweetService.addTweet(tweet);
	    }
	    ProcessService ps = new ProcessService("twitter.txt", 
	    		VaadinServlet.getCurrent().getServletContext().getRealPath("") + "/data/");
	    ((KeyanalysisUI)UI.getCurrent()).setProcess(ps);
	    ((KeyanalysisUI)UI.getCurrent()).getProcess().run();
	    TweetService.appendTweetFile(new File("data/newTweets.txt"));
	    TweetService.appendTweetFile(new File("data/newTweets.txt" + Constants.aes));
	    TweetService.appendTweetFile(new File("data/newTweets.txt" + Constants.key));
	    TweetService.appendTweetFile(new File("data/newTweets.txt" + Constants.sha));
	    TweetService.appendTweetFile(new File("data/newTweets.txt" + Constants.skein));
	    TweetService.appendTweetFile(new File("data/newTweets.txt" + Constants.serpent));
	    TweetService.appendTweetFile(new File("data/newTweets.txt" + Constants.blowfish));
	    TweetService.appendTweetFile(new File("data/newTweets.txt" + Constants.twofish));
	}
	
	public static void searchEngTweets(long num) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
	    cb.setDebugEnabled(true)
	            .setOAuthConsumerKey("ORlWMuKHZL12IsBPYUpxrMlLR")
	            .setOAuthConsumerSecret("oaicGQdctmawow6KMJ95uJ9nV0knjliazHpSE3pDQb4IHeIHMf")
	            .setOAuthAccessToken("788766851123863553-NeP7lLtvuhxi79rU0XiUR57ZhRaMwGz")
	            .setOAuthAccessTokenSecret("v3mS0NzwyUHbFi0UUR38g6WnxlGZ9xVv0amdg8jOPOvD3");
	    TwitterFactory tf = new TwitterFactory(cb.build());
	    Twitter twitter = tf.getInstance();
	    Query query = new Query("a");
	    query.count(100);
	    query.setResultType(ResultType.recent);
	    query.setUntil(new SimpleDateFormat("YYYY-MM-dd").format(new Date(num)));
	    query.setSince(new SimpleDateFormat("YYYY-MM-dd").format(new Date(num - 10800000)));
	    //query.setMaxId(num);
	    //query.setSinceId(num);;
	    query.setLang("en");
	    QueryResult result = null;
		try {
			result = twitter.search(query);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		if (result == null)
			return;
		System.out.println(result.getTweets().size());
	    for (Status tweet : result.getTweets()) {
	        System.out.println("@" + tweet.getUser().getScreenName() + ":" + tweet.getText());
	        TweetService.addTweet(tweet);
	    }
	    ProcessService ps = new ProcessService("twitter.txt", 
	    		VaadinServlet.getCurrent().getServletContext().getRealPath("") + "/data/");
	    ((KeyanalysisUI)UI.getCurrent()).setProcess(ps);
	    ((KeyanalysisUI)UI.getCurrent()).getProcess().run();
	    TweetService.appendTweetFile(new File("data/newTweets.txt"));
	    TweetService.appendTweetFile(new File("data/newTweets.txt" + Constants.aes));
	    TweetService.appendTweetFile(new File("data/newTweets.txt" + Constants.key));
	    TweetService.appendTweetFile(new File("data/newTweets.txt" + Constants.sha));
	    TweetService.appendTweetFile(new File("data/newTweets.txt" + Constants.skein));
	    TweetService.appendTweetFile(new File("data/newTweets.txt" + Constants.serpent));
	    TweetService.appendTweetFile(new File("data/newTweets.txt" + Constants.blowfish));
	    TweetService.appendTweetFile(new File("data/newTweets.txt" + Constants.twofish));
	    
	}
	
	public static void saveTweetsFromJson(File file) {
		if (file == null) {
			return;
		}
		ReadTweets p = new ReadTweets();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.out.println("There is not this file!");
			e.printStackTrace();
		}
		String line = null;
		long id = 0;
		DAO dao = new DAO();
		try {
			while((line=reader.readLine())!=null){
				JsonObject o = p.readFromString(line);
				if(o==null)continue;
				Tweet t = new Tweet();
				t.setLang(o.get("user").getAsJsonObject().get("lang").getAsString());
				if ("en".equals(t.getLang()) || "hu".equals(t.getLang())) {
					if (!o.get("place").isJsonNull())
						if (!o.get("place").getAsJsonObject().isJsonObject()) {
							if (!o.get("place").getAsJsonObject().get("country").isJsonNull())
								t.setCountry(o.get("place").getAsJsonObject().get("country").getAsString());
							if (!o.get("place").getAsJsonObject().get("countryCode").isJsonNull())
								t.setCountryCode(o.get("place").getAsJsonObject().get("countryCode").getAsString());
						}
					DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
					try {
						t.setDate(format.parse(o.get("created_at").getAsString()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					t.setFavoriteCount(0);
					if (!o.get("geo").isJsonNull()) 
						if (o.get("geo").getAsJsonObject().isJsonObject())
							if (o.get("geo").getAsJsonObject().get("coordinates").isJsonArray()) {
								if (o.get("geo").getAsJsonObject().get("coordinates").getAsJsonArray().get(1).isJsonPrimitive())
									t.setGeoLatitude(o.get("geo").getAsJsonObject().get("coordinates").getAsJsonArray().get(1).getAsDouble());
								if (o.get("geo").getAsJsonObject().get("coordinates").getAsJsonArray().get(0).isJsonPrimitive())
									t.setGeoLongitude(o.get("geo").getAsJsonObject().get("coordinates").getAsJsonArray().get(0).getAsDouble());
							}
					t.setId(o.get("id").getAsLong());
					if (!o.get("in_reply_to_screen_name").isJsonNull())
						t.setInReplyToScreenName(o.get("in_reply_to_screen_name").getAsString());
					if (!o.get("in_reply_to_status_id").isJsonNull())
						t.setInReplyToStatusId(o.get("in_reply_to_status_id").getAsLong());
					if (!o.get("in_reply_to_user_id").isJsonNull())
						t.setInReplyToUserId(o.get("in_reply_to_user_id").getAsLong());
					t.setQuotedStatusId(-1);
					if (!o.get("retweet_count").isJsonNull())
						t.setRetweetCount(o.get("retweet_count").getAsInt());
					t.setRetweetId(-1);
					t.setSource(o.get("source").getAsString());
					t.setText(o.get("text").getAsString().replace('\n', ' ').replace('\t',' ').replace('"', ' '));
					t.setUser(o.get("user").getAsJsonObject().get("name").getAsString());
					dao.addTweet(t);
					//String text = o.get("text").getAsString().replace('\n', ' ').replace('\t',' ').replace('"', ' ');
					//dao.addEntry(text);
					//hossz.add(text.length());
					//h.add(text);
					System.out.println(id + ":- " + t.getText());
					++id;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
