package jju.example.com.xgg.pojo;

/**
 * 购物车实体类
 */
public class ShoppingCart extends Base {

	Integer cart_id; // 购物车id
	Integer cart_cust_id; // 客户id
	Integer cart_shop_id; // 商家id
	Integer cart_prod_id; // 商品id
	String cart_prod_name; // 商品名称
	Double cart_prod_price; // 商品单价
	Integer cart_prod_count; // 商品数量
	String cart_date; // 生成时间

	public Integer getCart_id() {
		return cart_id;
	}

	public void setCart_id(Integer cart_id) {
		this.cart_id = cart_id;
	}

	public Integer getCart_cust_id() {
		return cart_cust_id;
	}

	public void setCart_cust_id(Integer cart_cust_id) {
		this.cart_cust_id = cart_cust_id;
	}

	public Integer getCart_shop_id() {
		return cart_shop_id;
	}

	public void setCart_shop_id(Integer cart_shop_id) {
		this.cart_shop_id = cart_shop_id;
	}

	public Integer getCart_prod_id() {
		return cart_prod_id;
	}

	public void setCart_prod_id(Integer cart_prod_id) {
		this.cart_prod_id = cart_prod_id;
	}

	public String getCart_prod_name() {
		return cart_prod_name;
	}

	public void setCart_prod_name(String cart_prod_name) {
		this.cart_prod_name = cart_prod_name;
	}

	public Double getCart_prod_price() {
		return cart_prod_price;
	}

	public void setCart_prod_price(Double cart_prod_price) {
		this.cart_prod_price = cart_prod_price;
	}

	public Integer getCart_prod_count() {
		return cart_prod_count;
	}

	public void setCart_prod_count(Integer cart_prod_count) {
		this.cart_prod_count = cart_prod_count;
	}

	public String getCart_date() {
		return cart_date;
	}

	public void setCart_date(String cart_date) {
		this.cart_date = cart_date;
	}

	@Override
	public String toString() {
		return "ShoppingCart [cart_id=" + cart_id + ", cart_cust_id=" + cart_cust_id + ", cart_shop_id=" + cart_shop_id
				+ ", cart_prod_id=" + cart_prod_id + ", cart_prod_name=" + cart_prod_name + ", cart_prod_price="
				+ cart_prod_price + ", cart_prod_count=" + cart_prod_count + ", cart_date=" + cart_date + "]";
	}


}
