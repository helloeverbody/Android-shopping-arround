package jju.example.com.xgg.pojo;

/**
 * 客户实体类
 *
 */
public class Customer extends Base {
	Integer cust_id; // id
	String cust_name; // 姓名
	Integer cust_sex; // 性别
	String cust_birthday; // 生日
	String cust_account; // 登录账号
	String cust_password; // 密码
	String cust_phone; // 手机号
	String cust_province; // 省份
	String cust_city; // 城市
	String cust_area; // 县区
	String cust_address; // 地址
	String cust_head; // 头像

	public Customer() {

	}

	public Integer getCust_id() {
		return cust_id;
	}

	public void setCust_id(Integer cust_id) {
		this.cust_id = cust_id;
	}

	public String getCust_name() {
		return cust_name;
	}

	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}

	public Integer getCust_sex() {
		return cust_sex;
	}

	public void setCust_sex(Integer cust_sex) {
		this.cust_sex = cust_sex;
	}

	public String getCust_birthday() {
		return cust_birthday;
	}

	public void setCust_birthday(String cust_birthday) {
		this.cust_birthday = cust_birthday;
	}

	public String getCust_account() {
		return cust_account;
	}

	public void setCust_account(String cust_account) {
		this.cust_account = cust_account;
	}

	public String getCust_password() {
		return cust_password;
	}

	public void setCust_password(String cust_password) {
		this.cust_password = cust_password;
	}

	public String getCust_phone() {
		return cust_phone;
	}

	public void setCust_phone(String cust_phone) {
		this.cust_phone = cust_phone;
	}

	public String getCust_province() {
		return cust_province;
	}

	public void setCust_province(String cust_province) {
		this.cust_province = cust_province;
	}

	public String getCust_city() {
		return cust_city;
	}

	public void setCust_city(String cust_city) {
		this.cust_city = cust_city;
	}

	public String getCust_area() {
		return cust_area;
	}

	public void setCust_area(String cust_area) {
		this.cust_area = cust_area;
	}

	public String getCust_address() {
		return cust_address;
	}

	public void setCust_address(String cust_address) {
		this.cust_address = cust_address;
	}

	public String getCust_head() {
		return cust_head;
	}

	public void setCust_head(String cust_head) {
		this.cust_head = cust_head;
	}

	@Override
	public String toString() {
		return "Customer [cust_id=" + cust_id + ", cust_name=" + cust_name + ", cust_sex=" + cust_sex
				+ ", cust_birthday=" + cust_birthday + ", cust_account=" + cust_account + ", cust_password="
				+ cust_password + ", cust_phone=" + cust_phone + ", cust_province=" + cust_province + ", cust_city="
				+ cust_city + ", cust_area=" + cust_area + ", cust_address=" + cust_address + ", cust_head=" + cust_head
				+ "]";
	}


}
