package ibf2022.batch3.assessment.csf.orderbackend.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrder;
import ibf2022.batch3.assessment.csf.orderbackend.respositories.OrdersRepository;
import ibf2022.batch3.assessment.csf.orderbackend.respositories.PendingOrdersRepository;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

@Service
public class OrderingService {

	@Autowired
	private OrdersRepository ordersRepo;

	@Autowired
	private PendingOrdersRepository pendingOrdersRepo;

	private static final String PIZZA_REST_API_URL = "https://pizza-pricing-production.up.railway.app/order";

	// TODO: Task 5
	// WARNING: DO NOT CHANGE THE METHOD'S SIGNATURE
	public PizzaOrder placeOrder(PizzaOrder order) throws OrderException {
		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("name", order.getName());
		form.add("email", order.getEmail());
		form.add("sauce", order.getSauce());
		form.add("size", order.getSize().toString());
		form.add("thickCrust", order.getThickCrust().toString());
		String toppings = toppingsListToString(order.getTopplings());
		form.add("toppings", toppings);
		form.add("comments", order.getComments());
		System.out.println(form);

		RequestEntity req = RequestEntity.post(PIZZA_REST_API_URL)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.header("Accept", "text/plain")
				.body(form);

		RestTemplate template = new RestTemplate();

		ResponseEntity<String> response = template.exchange(req, String.class);

		String payload = response.getBody();

		String[] splitArray = payload.split(",");
		order.setOrderId(splitArray[0]);
		order.setDate(convertMillisecondsToDate(Long.parseLong(splitArray[1])));
		order.setTotal(Float.parseFloat(splitArray[2]));

		ordersRepo.add(order);
		pendingOrdersRepo.add(order);

		return order;
	}

	// For Task 6
	// WARNING: Do not change the method's signature or its implemenation
	public List<PizzaOrder> getPendingOrdersByEmail(String email) {
		return ordersRepo.getPendingOrdersByEmail(email);
	}

	// For Task 7
	// WARNING: Do not change the method's signature or its implemenation
	public boolean markOrderDelivered(String orderId) {
		return ordersRepo.markOrderDelivered(orderId) && pendingOrdersRepo.delete(orderId);
	}

	// EXTRA
	private String toppingsListToString(List<String> toppings) {
		StringBuilder toppingsString = new StringBuilder();
		for (String topping : toppings) {
			toppingsString.append(topping).append(",");
		}
		System.out.println("topping list to string >>> " + toppingsString.toString());
		// topping list to string >>> "chicken","seafood", //NOTE
		return toppingsString.toString();
	}

	private Date convertMillisecondsToDate(long milliseconds) {
		return new Date(milliseconds);
	}

	public PizzaOrder convertFromJsonString(String js) throws IOException {
		PizzaOrder po = new PizzaOrder();
		if (js != null) {
			try (InputStream is = new ByteArrayInputStream(js.getBytes())) {
				JsonReader jr = Json.createReader(is);
				JsonObject jsObj = jr.readObject();

				po.setName(jsObj.getString("name"));
				po.setEmail(jsObj.getString("email"));
				po.setSize(jsObj.getInt("size"));

				po.setThickCrust((jsObj.getJsonString("base").toString().equalsIgnoreCase("thick") ? true : false));
				po.setSauce(jsObj.getString("sauce"));

				JsonArray jsArr = jsObj.getJsonArray("toppings");
				List<String> toppings = new LinkedList<>();
				for (JsonValue jsonValue : jsArr) {
					toppings.add(jsonValue.toString());
				}

				System.out.println("List >>> " + toppings);
				po.setTopplings(toppings);
				po.setComments(jsObj.getString("comments"));
			}
		}
		return po;
	}

	public JsonObject pizzaOrderToJsonObject(PizzaOrder po) {
		return Json.createObjectBuilder()
				.add("orderId", po.getOrderId())
				.add("date", po.getDate().getTime())
				.add("name", po.getName())
				.add("email", po.getEmail())
				.add("total", po.getTotal())
				.build();
	}

	public JsonArray convertOrdersToJsonArray(List<PizzaOrder> orders) {
		JsonArrayBuilder array = Json.createArrayBuilder();

		for (PizzaOrder order : orders) {
			JsonObjectBuilder builder = Json.createObjectBuilder()
					.add("orderId", order.getOrderId())
					.add("total", order.getTotal())
					.add("date", order.getDate().toInstant().toEpochMilli());
			array.add(builder);
		}

		return array.build();
	}

}
