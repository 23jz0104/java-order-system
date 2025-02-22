package model;

import java.io.Serializable;

/**
 * オーダーステータスクラス
 * @author 23jz 井手
 * @version 1.0 2024/12/04
 */

public class OrderStatus implements Serializable{
	private int id;
	private String name;
	
	public OrderStatus() {
		
	}
	
	public OrderStatus(int id, String name) {
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
	
	
}
