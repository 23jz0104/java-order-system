package model;

import java.io.Serializable;

/**
 * 商品クラス
 * 
 * @author 23jz 井手
 * @version 1.0 2024/12/03
 */

public class Product implements Serializable{
	private int id;
	private String name;
	private int price;
	private int sizeId;
	private int categoryId;
	private String image;
	private String supplement;
	
	//コンストラクタ
	public Product() {
		
	}
	
	//すべてを含むコンストラクタ
	public Product(int id, String name, int price, int categoryId, String image, String supplement) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.categoryId = categoryId;
		this.image = image;
		this.supplement = supplement;
	}
	
	//連番以外をすべて含んだコンストラクタ
	public Product(String name, int price,int sizeId, int categoryId, String image, String supplement) {
		this.name = name;
		this.price = price;
		this.sizeId = sizeId;
		this.categoryId = categoryId;
		this.image = image;
		this.supplement = supplement;
	}
	
	//連番とサイズ以外を含むコンストラクタ
	public Product(String name, int price, int categoryId, String image, String supplement) {
		this.name = name;
		this.price = price;
		this.categoryId = categoryId;
		this.image = image;
		this.supplement = supplement;
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getSizeId() {
		return sizeId;
	}

	public void setSizeId(int sizeId) {
		this.sizeId = sizeId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSupplement() {
		return supplement;
	}

	public void setSupplement(String supplement) {
		this.supplement = supplement;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", price=" + price + ", sizeId=" + sizeId + ", categoryId="
				+ categoryId + ", image=" + image + ", supplement=" + supplement + "]";
	}
}
