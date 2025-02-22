package model;

import java.io.Serializable;

/**
 * サイズクラス
 * @author 23jz 井手
 * @version 1.0 2024/12/04
 */

public class Size implements Serializable{
	private int id;
	private String name;
	private int difference;
	
	public Size() {
		
	}
	
	public Size(int id, String name, int difference) {
		this.id = id;
		this.name = name;
		this.difference = difference;
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

	public int getDifference() {
		return difference;
	}

	public void setDifference(int difference) {
		this.difference = difference;
	}
	
	@Override
	public String toString() {
		return "Size [id = " + id + ", name = " + name + ", difference = " + difference + "]";
	}
	
}
