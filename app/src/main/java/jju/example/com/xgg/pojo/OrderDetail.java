package jju.example.com.xgg.pojo;

/**
 * 订单详情实体类
 */
public class OrderDetail extends Base {
	Integer ordeta_id; // 订单详情编号
	Integer ordeta_orde_id; // 订单编号
	Integer ordeta_prod_id; // 商品编号
	String ordeta_prod_name; // 商品名称
	Double ordeta_prod_price; // 商品单价
	Integer ordeta_prod_count; // 商品数量

	public Integer getOrdeta_id() {
		return ordeta_id;
	}

	public void setOrdeta_id(Integer ordeta_id) {
		this.ordeta_id = ordeta_id;
	}

	public Integer getOrdeta_orde_id() {
		return ordeta_orde_id;
	}

	public void setOrdeta_orde_id(Integer ordeta_orde_id) {
		this.ordeta_orde_id = ordeta_orde_id;
	}

	public Integer getOrdeta_prod_id() {
		return ordeta_prod_id;
	}

	public void setOrdeta_prod_id(Integer ordeta_prod_id) {
		this.ordeta_prod_id = ordeta_prod_id;
	}

	public String getOrdeta_prod_name() {
		return ordeta_prod_name;
	}

	public void setOrdeta_prod_name(String ordeta_prod_name) {
		this.ordeta_prod_name = ordeta_prod_name;
	}

	public Double getOrdeta_prod_price() {
		return ordeta_prod_price;
	}

	public void setOrdeta_prod_price(Double ordeta_prod_price) {
		this.ordeta_prod_price = ordeta_prod_price;
	}

	public Integer getOrdeta_prod_count() {
		return ordeta_prod_count;
	}

	public void setOrdeta_prod_count(Integer ordeta_prod_count) {
		this.ordeta_prod_count = ordeta_prod_count;
	}

	/*
	 * @Override public String toString() { return "OrderDetail [ordeta_id=" +
	 * ordeta_id + ", ordeta_orde_id=" + ordeta_orde_id + ", ordeta_prod_id=" +
	 * ordeta_prod_id + ", ordeta_prod_price=" + ordeta_prod_price +
	 * ", ordeta_prod_count=" + ordeta_prod_count + "]"; }
	 */
	public String toString() {
		return "OrderDetail [ordeta_orde_id=" + ordeta_orde_id + "]";
	}

}
