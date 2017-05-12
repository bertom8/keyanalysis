package org.keyanalysis.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="Users")
public class User {
	@Id
	@Column(name="username", nullable = false)
	private String name = "";
	
	@Column(name="password", nullable = false)
	private String password = org.keyanalysis.Services.DB.LoginService.hashing("default");
	
	@Column(name="deleted", nullable = false, columnDefinition = "BIT default 0")
	private boolean deleted = false;
	
	@ManyToOne
	private Storage storage = null;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public Storage getStorage() {
		return storage;
	}
	public void setStorage(Storage storage) {
		this.storage = storage;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
