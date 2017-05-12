package org.keyanalysis.Model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tweet")
public class Tweet {
	@Column(name="date")
	private Date date = null;
	
	@Column(name="retweetId")
	private long retweetId = -1;
	
	@Column(name="favoriteCount")
	private int favoriteCount = 0;
	
	@Column(name="id")
	@Id
	private long id = -1;
	
	@Column(name="inReplyToScreenName")
	private String inReplyToScreenName = null;
	
	@Column(name="inReplyToStatusId")
	private long inReplyToStatusId = -1;
	
	@Column(name="inReplyToUserId")
	private long inReplyToUserId = -1;
	
	@Column(name="lang")
	private String lang = null;
	
	@Column(name="quotedStatusId")
	private long quotedStatusId = -1;
	
	@Column(name="retweetCount")
	private int retweetCount = 0;
	
	@Column(name="source")
	private String source = null;
	
	@Column(name="user")
	private String user = null;
	
	@Column(name="text")
	private String text = null;
	
	@Column(name="geoLatitude")
	private double geoLatitude = 0;
	
	@Column(name="geoLongitude")
	private double geoLongitude = 0;
	
	@Column(name="country")
	private String country = "";
	
	@Column(name="countryCode")
	private String countryCode = "";
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public long getRetweetId() {
		return retweetId;
	}
	public void setRetweetId(long retweetId) {
		this.retweetId = retweetId;
	}
	public int getFavoriteCount() {
		return favoriteCount;
	}
	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getInReplyToScreenName() {
		return inReplyToScreenName;
	}
	public void setInReplyToScreenName(String inReplyToScreenName) {
		this.inReplyToScreenName = inReplyToScreenName;
	}
	public long getInReplyToStatusId() {
		return inReplyToStatusId;
	}
	public void setInReplyToStatusId(long inReplyToStatusId) {
		this.inReplyToStatusId = inReplyToStatusId;
	}
	public long getInReplyToUserId() {
		return inReplyToUserId;
	}
	public void setInReplyToUserId(long inReplyToUserId) {
		this.inReplyToUserId = inReplyToUserId;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public long getQuotedStatusId() {
		return quotedStatusId;
	}
	public void setQuotedStatusId(long quotedStatusId) {
		this.quotedStatusId = quotedStatusId;
	}
	public int getRetweetCount() {
		return retweetCount;
	}
	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public double getGeoLatitude() {
		return geoLatitude;
	}
	public void setGeoLatitude(double geoLatitude) {
		this.geoLatitude = geoLatitude;
	}
	public double getGeoLongitude() {
		return geoLongitude;
	}
	public void setGeoLongitude(double geoLongitude) {
		this.geoLongitude = geoLongitude;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
}
