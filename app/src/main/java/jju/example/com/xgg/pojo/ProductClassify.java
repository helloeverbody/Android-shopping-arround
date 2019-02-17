package jju.example.com.xgg.pojo;

/**
 * 分类实体类
 *
 */
public class ProductClassify extends Base {
	Integer pc_id; // id
	Integer pc_shop_id; // 店铺id
	String pc_name; // 分类名称

	public Integer getPc_id() {
		return pc_id;
	}

	public void setPc_id(Integer pc_id) {
		this.pc_id = pc_id;
	}

	public Integer getPc_shop_id() {
		return pc_shop_id;
	}

	public void setPc_shop_id(Integer pc_shop_id) {
		this.pc_shop_id = pc_shop_id;
	}

	public String getPc_name() {
		return pc_name;
	}

	public void setPc_name(String pc_name) {
		this.pc_name = pc_name;
	}
	@Override
	public String toString() {
		return "ProductClassify [pc_id=" + pc_id + ", pc_shop_id=" + pc_shop_id + ", pc_name=" + pc_name + "]";
	}

}
