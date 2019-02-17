package jju.example.com.xgg.pojo;

/**
 * 商品实体类
 */
public class Product extends Shop {

	private Integer prod_id; // 商品id
	private String prod_name; // 商品名称
	private Double prod_price;// 商品价格
	private Integer prod_clas_id;// 商品分类id
	private Integer prod_shop_id;// 商家id
	private String prod_head;// 商品图片

	public String getProd_head() {
		return prod_head;
	}

	public void setProd_head(String prod_head) {
		this.prod_head = prod_head;
	}

	public Integer getProd_id() {
		return prod_id;
	}

	public void setProd_id(Integer prod_id) {
		this.prod_id = prod_id;
	}

	public String getProd_name() {
		return prod_name;
	}

	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}

	public Double getProd_price() {
		return prod_price;
	}

	public void setProd_price(Double prod_price) {
		this.prod_price = prod_price;
	}

	public Integer getProd_clas_id() {
		return prod_clas_id;
	}

	public void setProd_clas_id(Integer prod_clas_id) {
		this.prod_clas_id = prod_clas_id;
	}

	public Integer getProd_shop_id() {
		return prod_shop_id;
	}

	public void setProd_shop_id(Integer prod_shop_id) {
		this.prod_shop_id = prod_shop_id;
	}

	@Override
	public String toString() {
		return "Product [prod_id=" + prod_id + ", prod_name=" + prod_name + ", prod_price=" + prod_price
				+ ", prod_clas_id=" + prod_clas_id + ", prod_shop_id=" + prod_shop_id + ", prod_head=" + prod_head
				+ "]";
	}




}
