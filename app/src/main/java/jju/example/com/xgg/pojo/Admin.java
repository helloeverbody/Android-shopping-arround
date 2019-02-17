package jju.example.com.xgg.pojo;

/**
 * 管理员实体类
 *
 */
public class Admin extends Base {
	Integer admi_id; // id
	String admi_name; // 名称
	String admi_account; // 登录账号
	String admi_password; // 登录密码
	Integer admi_role; // 权限

	public Integer getAdmi_id() {
		return admi_id;
	}
	public void setAdmi_id(Integer admi_id) {
		this.admi_id = admi_id;
	}

	public String getAdmi_name() {
		return admi_name;
	}

	public void setAdmi_name(String admi_name) {
		this.admi_name = admi_name;
	}
	public String getAdmi_account() {
		return admi_account;
	}
	public void setAdmi_account(String admi_account) {
		this.admi_account = admi_account;
	}
	public String getAdmi_password() {
		return admi_password;
	}
	public void setAdmi_password(String admi_password) {
		this.admi_password = admi_password;
	}
	public Integer getAdmi_role() {
		return admi_role;
	}
	public void setAdmi_role(Integer admi_role) {
		this.admi_role = admi_role;
	}

	@Override
	public String toString() {
		return "Admin [admi_id=" + admi_id + ", admi_name=" + admi_name + ", admi_account=" + admi_account
				+ ", admi_password=" + admi_password + ", admi_role=" + admi_role + "]";
	}

	


}
