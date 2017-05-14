package org.keyanalysis.Model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tweet")
public class Tweet {
	@Column(name = "date")
	private Date date = null;

	@Column(name = "retweetId")
	private long retweetId = -1;

	@Column(name = "favoriteCount")
	private int favoriteCount = 0;

	@Column(name = "id")
	@Id
	private long id = -1;

	@Column(name = "inReplyToScreenName")
	private String inReplyToScreenName = null;

	@Column(name = "inReplyToStatusId")
	private long inReplyToStatusId = -1;

	@Column(name = "inReplyToUserId")
	private long inReplyToUserId = -1;

	@Column(name = "lang")
	private String lang = null;

	@Column(name = "quotedStatusId")
	private long quotedStatusId = -1;

	@Column(name = "retweetCount")
	private int retweetCount = 0;

	@Column(name = "source")
	private String source = null;

	@Column(name = "user")
	private String user = null;

	@Column(name = "text")
	private String text = null;

	@Column(name = "geoLatitude")
	private double geoLatitude = 0;

	@Column(name = "geoLongitude")
	private double geoLongitude = 0;

	@Column(name = "country")
	private String country = "";

	@Column(name = "countryCode")
	private String countryCode = "";

	public Date getDate() {
		return this.date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public long getRetweetId() {
		return this.retweetId;
	}

	public void setRetweetId(final long retweetId) {
		this.retweetId = retweetId;
	}

	public int getFavoriteCount() {
		return this.favoriteCount;
	}

	public void setFavoriteCount(final int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public long getId() {
		return this.id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getInReplyToScreenName() {
		return this.inReplyToScreenName;
	}

	public void setInReplyToScreenName(final String inReplyToScreenName) {
		this.inReplyToScreenName = inReplyToScreenName;
	}

	public long getInReplyToStatusId() {
		return this.inReplyToStatusId;
	}

	public void setInReplyToStatusId(final long inReplyToStatusId) {
		this.inReplyToStatusId = inReplyToStatusId;
	}

	public long getInReplyToUserId() {
		return this.inReplyToUserId;
	}

	public void setInReplyToUserId(final long inReplyToUserId) {
		this.inReplyToUserId = inReplyToUserId;
	}

	public String getLang() {
		return this.lang;
	}

	public void setLang(final String lang) {
		this.lang = lang;
	}

	public long getQuotedStatusId() {
		return this.quotedStatusId;
	}

	public void setQuotedStatusId(final long quotedStatusId) {
		this.quotedStatusId = quotedStatusId;
	}

	public int getRetweetCount() {
		return this.retweetCount;
	}

	public void setRetweetCount(final int retweetCount) {
		this.retweetCount = retweetCount;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(final String source) {
		this.source = source;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public double getGeoLatitude() {
		return this.geoLatitude;
	}

	public void setGeoLatitude(final double geoLatitude) {
		this.geoLatitude = geoLatitude;
	}

	public double getGeoLongitude() {
		return this.geoLongitude;
	}

	public void setGeoLongitude(final double geoLongitude) {
		this.geoLongitude = geoLongitude;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(final String country) {
		this.country = country;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(final String countryCode) {
		this.countryCode = countryCode;
	}
}
