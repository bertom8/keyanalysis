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
@Table(name = "item")
public class Item {
	@Id
	@GeneratedValue
	private int id;

	@Column(name = "time")
	private Timestamp time = new Timestamp(new Date().getTime());

	@ManyToOne
	private Storage storage = null;

	@Column(name = "name")
	private String name = "";

	@Column(name = "deleted", nullable = false, columnDefinition = "BIT default 0")
	private boolean deleted = false;

	@Column(name = "filepath", nullable = false)
	private String filePath = "";

	@Column(name = "benchmark")
	private double benchmark = 0.0;

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(final String filePath) {
		this.filePath = filePath;
	}

	public double getBenchmark() {
		return this.benchmark;
	}

	public void setBenchmark(final double benchmark) {
		this.benchmark = benchmark;
	}

	public Storage getStorage() {
		return this.storage;
	}

	public void setStorage(final Storage storage) {
		this.storage = storage;
	}

	public boolean isDeleted() {
		return this.deleted;
	}

	public void setDeleted(final boolean deleted) {
		this.deleted = deleted;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public Timestamp getTime() {
		return this.time;
	}

	public void setTime(final Timestamp time) {
		this.time = time;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
