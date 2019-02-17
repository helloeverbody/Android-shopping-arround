package jju.example.com.xgg.pojo;

/**
 * 实体类基类
 *
 */
public class Base {

	String condition;// 查询条件
	String limit;// 记录数
	String orderBy;// 排序条件
  

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	@Override
	public String toString() {
		return "Base [condition=" + condition + ", limit=" + limit + ", orderBy=" + orderBy + "]";
	}

}
