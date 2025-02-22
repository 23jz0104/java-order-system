package model;

import java.io.Serializable;
import java.sql.Time;

public class Order implements Serializable{
	private int id;
	private Time orderTime;
	private int tableNumber;
	
	public Order() {
		
	}
	
	public Order(int tableNumber) {
		this.tableNumber = tableNumber;
	}
	
	public Order(int id, Time orderTime, int tableNumber) {
		this.id = id;
		this.orderTime = orderTime;
		this.tableNumber = tableNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Time getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Time orderTime) {
		this.orderTime = orderTime;
	}
	
	public int getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(int tableNumber) {
		this.tableNumber = tableNumber;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", orderTime=" + orderTime + ", tableNumber=" + tableNumber + "]";
	}
}
