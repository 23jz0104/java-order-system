package model;

import java.io.Serializable;

/**
 * カテゴリークラス
 * @author 23jz 井手
 * @version 1.0 2024/12/03
 */

public class Category implements Serializable{
	private int id;
	private String name;
	
	public Category() {
		
	}
	
	public Category(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Category [id = " + id + ", name = " + name + "]";
	}
}
