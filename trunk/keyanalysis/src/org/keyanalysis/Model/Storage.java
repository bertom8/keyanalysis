package org.keyanalysis.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="storage")
public class Storage {
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	
	/*@OneToMany(mappedBy="id")
	@OrderBy("time")
	@Column(name="list")
	private List<Item> list;*/
	
	@Column(name="deleted", nullable = false, columnDefinition = "BIT default 0")
	private boolean deleted = false;

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

	/*public List<Item> getList() {
		return list;
	}

	public void setList(List<Item> list) {
		this.list = list;
	}*/

}
