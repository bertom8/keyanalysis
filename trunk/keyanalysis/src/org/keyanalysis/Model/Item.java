package org.keyanalysis.Model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="item")
public class Item {
	@Id
	@GeneratedValue
	private int id;
	
	@Column(name="time")
	private Timestamp time = new Timestamp(new Date().getTime());

	@ManyToOne
	private Storage storage = null;
	
	@Column(name="name")
	private String name = "";
	
	@Column(name="deleted", nullable = false, columnDefinition = "BIT default 0")
	private boolean deleted = false;
	
	@Column(name="filepath", nullable = false)
	private String filePath = "";
	
	@Column(name="benchmark")
	private double benchmark = 0.0;
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public double getBenchmark() {
		return benchmark;
	}

	public void setBenchmark(double benchmark) {
		this.benchmark = benchmark;
	}

	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
