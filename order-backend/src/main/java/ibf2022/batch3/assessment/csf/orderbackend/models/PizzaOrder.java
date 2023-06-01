package ibf2022.batch3.assessment.csf.orderbackend.models;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

// WARNING: DO NOT MODIFY THIS FILE. 2 MARKS WILL BE DEDUCTED FOR EVERY MODIFIED LINE
public class PizzaOrder {

	private String orderId = "not set";
	private Date date;
	private String name;
	private String email;
	private String sauce;
	private Integer size;
	private Boolean thickCrust = false;
	private List<String> toppings = new LinkedList<>();
	private String comments;
	private Float total;

	public void setOrderId(String orderId) { this.orderId = orderId; }
	public String getOrderId() { return this.orderId; }

	public void setDate(Date date) { this.date = date; }
	public Date getDate() { return this.date; }

	public void setName(String name) { this.name = name; }
	public String getName() { return this.name; }
	
	public void setEmail(String email) { this.email = email; }
	public String getEmail() { return this.email; }

	public void setSauce(String sauce) { this.sauce = sauce; }
	public String getSauce() { return this.sauce; }

	public void setSize(Integer size) { this.size = size; }
	public Integer getSize() { return this.size; }

	public void setThickCrust(Boolean thickCrust) { this.thickCrust = thickCrust; }
	public Boolean getThickCrust() { return this.thickCrust; }
	public Boolean isThickCrust() { return this.thickCrust; }

	public void setTopplings(List<String> toppings) { this.toppings = toppings; }
	public List<String> getTopplings() { return this.toppings; }
	public void addTopping(String topping) { this.toppings.add(topping); }

	public void setComments(String comments) { this.comments = comments; }
	public String getComments() { return this.comments; }

	public void setTotal(Float total) { this.total = total; }
	public Float getTotal() { return this.total; }

	@Override
	public String toString() {
		return "PizzaOrder{orderId=%s, date=%s, name=%s, email=%s, sauce=%s, size=%d, thickCurst=%b, toppings=%s, comments=%s, total=%f}"
				.formatted(orderId, date, name, email, sauce, size, thickCrust, toppings, comments, total);
	}

	// WARNING: DO NOT MODIFY THIS FILE. 2 MARKS WILL BE DEDUCTED FOR EVERY MODIFIED LINE
}
