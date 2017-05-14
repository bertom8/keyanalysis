package org.keyanalysis.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class User {
	@Id
	@Column(name = "username", nullable = false)
	private String name = "";

	@Column(name = "password", nullable = false)
	private String password = org.keyanalysis.Services.DB.LoginService.hashing("default");

	@Column(name = "deleted", nullable = false, columnDefinition = "BIT default 0")
	private boolean deleted = false;

	@ManyToOne
	private Storage storage = null;

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public boolean isDeleted() {
		return this.deleted;
	}

	public void setDeleted(final boolean deleted) {
		this.deleted = deleted;
	}

	public Storage getStorage() {
		return this.storage;
	}

	public void setStorage(final Storage storage) {
		this.storage = storage;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
