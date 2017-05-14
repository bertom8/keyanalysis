package org.keyanalysis.Model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "log")
public class Log {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	@ManyToOne
	private User user = null;

	@Column(name = "type")
	private String type = "";

	@Column(name = "action")
	private String action = "";

	@Column(name = "time")
	private Timestamp time = null;

	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(final String action) {
		this.action = action;
	}

	public Timestamp getTime() {
		return this.time;
	}

	public void setTime(final Timestamp time) {
		this.time = time;
	}

}
