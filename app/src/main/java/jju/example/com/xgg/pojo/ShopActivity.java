package jju.example.com.xgg.pojo;

/**
 * 商家活动实体类
 */
public class ShopActivity extends Base {

	private Integer acti_id; // 活动id
	private String acti_name;// 活动名称
	private Integer acti_shop_id;// 商家id
	private String acti_ad;// 广告图
	private String acti_detail;// 活动详情

	public Integer getActi_id() {
		return acti_id;
	}

	public void setActi_id(Integer acti_id) {
		this.acti_id = acti_id;
	}

	public String getActi_name() {
		return acti_name;
	}

	public void setActi_name(String acti_name) {
		this.acti_name = acti_name;
	}

	public Integer getActi_shop_id() {
		return acti_shop_id;
	}

	public void setActi_shop_id(Integer acti_shop_id) {
		this.acti_shop_id = acti_shop_id;
	}

	public String getActi_ad() {
		return acti_ad;
	}

	public void setActi_ad(String acti_ad) {
		this.acti_ad = acti_ad;
	}

	public String getActi_detail() {
		return acti_detail;
	}

	public void setActi_detail(String acti_detail) {
		this.acti_detail = acti_detail;
	}

	@Override
	public String toString() {
		return "ShopActivity [acti_id=" + acti_id + ", acti_name=" + acti_name + ", acti_shop_id=" + acti_shop_id
				+ ", acti_ad=" + acti_ad + ", acti_detail=" + acti_detail + "]";
	}

}
