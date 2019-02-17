package jju.example.com.xgg.pojo;

/**
 * 评价实体类
 *
 */
public class Comment extends Base {
	Integer comm_id; // id
	Integer comm_ordr_id; // 订单id
	String comm_time; // 评价时间
	String comm_content; // 评价内容
	String comm_level; // 评价等级

	public Integer getComm_id() {
		return comm_id;
	}

	public void setComm_id(Integer comm_id) {
		this.comm_id = comm_id;
	}

	public Integer getComm_ordr_id() {
		return comm_ordr_id;
	}

	public void setComm_ordr_id(Integer comm_ordr_id) {
		this.comm_ordr_id = comm_ordr_id;
	}

	public String getComm_time() {
		return comm_time;
	}

	public void setComm_time(String comm_time) {
		this.comm_time = comm_time;
	}

	public String getComm_content() {
		return comm_content;
	}

	public void setComm_content(String comm_content) {
		this.comm_content = comm_content;
	}

	public String getComm_level() {
		return comm_level;
	}

	public void setComm_level(String comm_level) {
		this.comm_level = comm_level;
	}

	@Override
	public String toString() {
		return "Comment [comm_id=" + comm_id + ", comm_ordr_id=" + comm_ordr_id + ", comm_time=" + comm_time
				+ ", comm_content=" + comm_content + ", comm_level=" + comm_level + "]";
	}

}
