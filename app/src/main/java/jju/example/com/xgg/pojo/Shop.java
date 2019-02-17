package jju.example.com.xgg.pojo;

import java.util.List;

/**
 * Title: Shop
 * 
 * @Description: 商家实体类
 * @author yhfu
 * @date 2018年10月22日 下午11:13:25
 */
public class Shop extends Base {
	Integer shop_id; // 商家id
	String shop_account; // 登录账号
	String shop_password; // 登录密码
	String shop_name; // 店铺名称
	String shop_owner; // 店主
	String shop_phone; // 联系方式
	String shop_label; // 店铺标签
	String shop_head; // 店铺图片
	double shop_level; // 店铺评价星级
	String shop_notice; // 店铺公告

	public String getShop_head() {
		return shop_head;
	}

	public void setShop_head(String shop_head) {
		this.shop_head = shop_head;
	}

	public double getShop_level() {
		return shop_level;
	}

	public void setShop_level(double shop_level) {
		this.shop_level = shop_level;
	}

	public String getShop_notice() {
		return shop_notice;
	}

	public void setShop_notice(String shop_notice) {
		this.shop_notice = shop_notice;
	}

	List<Product> productList; // 店铺内的商品集合

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public Integer getShop_id() {
		return shop_id;
	}

	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}

	public String getShop_account() {
		return shop_account;
	}

	public void setShop_account(String shop_account) {
		this.shop_account = shop_account;
	}

	public String getShop_password() {
		return shop_password;
	}

	public void setShop_password(String shop_password) {
		this.shop_password = shop_password;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getShop_owner() {
		return shop_owner;
	}

	public void setShop_owner(String shop_owner) {
		this.shop_owner = shop_owner;
	}

	public String getShop_phone() {
		return shop_phone;
	}

	public void setShop_phone(String shop_phone) {
		this.shop_phone = shop_phone;
	}

	public String getShop_label() {
		return shop_label;
	}

	public void setShop_label(String shop_label) {
		this.shop_label = shop_label;
	}

	@Override
	public String toString() {
		return "Shop [shop_id=" + shop_id + ", shop_account=" + shop_account + ", shop_password=" + shop_password
				+ ", shop_name=" + shop_name + ", shop_owner=" + shop_owner + ", shop_phone=" + shop_phone
				+ ", shop_label=" + shop_label + ", shop_head=" + shop_head + ", shop_level=" + shop_level
				+ ", shop_notice=" + shop_notice + "]";
	}


}
