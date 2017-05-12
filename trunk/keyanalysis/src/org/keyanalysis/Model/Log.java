package org.keyanalysis.Model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="log")
public class Log {
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	
	@ManyToOne
	private User user = null;
	
	@Column(name="type")
	private String type = "";
	
	@Column(name="action")
	private String action = "";
	
	@Column(name="time")
	private Timestamp time = null;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}
	
}
