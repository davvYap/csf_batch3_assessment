package ibf2022.batch3.assessment.csf.orderbackend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrder;
import ibf2022.batch3.assessment.csf.orderbackend.services.OrderingService;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

@Controller
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class OrderController {

	@Autowired
	private OrderingService orderSvc;

	// TODO: Task 3 - POST /api/order
	@PostMapping(path = "/order")
	@ResponseBody
	public ResponseEntity<String> createOrder(@RequestBody String orderJson) {

		PizzaOrder po = null;
		try {
			po = orderSvc.convertFromJsonString(orderJson);
			orderSvc.placeOrder(po);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.contentType(MediaType.APPLICATION_JSON)
					.body(Json.createObjectBuilder()
							.add("error", e.getMessage())
							.build().toString());
		}

		JsonObject response = orderSvc.pizzaOrderToJsonObject(po);

		return ResponseEntity.status(HttpStatus.ACCEPTED)
				.contentType(MediaType.APPLICATION_JSON)
				.body(response.toString());
	}

	// TODO: Task 6 - GET /api/orders/<email>
	@GetMapping(path = "/orders/{email}")
	@ResponseBody
	public ResponseEntity<String> getOrders(@PathVariable String email) {
		List<PizzaOrder> orders = orderSvc.getPendingOrdersByEmail(email);

		JsonArray ordersArr = orderSvc.convertOrdersToJsonArray(orders);

		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(ordersArr.toString());
	}

	// TODO: Task 7 - DELETE /api/order/<orderId>
	@DeleteMapping(path = "/order/{orderId}")
	@ResponseBody
	public ResponseEntity<String> deleteOrder(@PathVariable String orderId) {
		Boolean success = orderSvc.markOrderDelivered(orderId);

		if (!success) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.contentType(MediaType.APPLICATION_JSON)
					.body(Json.createObjectBuilder()
							.add("error", "Order not found")
							.build().toString());
		}

		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Json.createObjectBuilder()
						.build().toString());
	}

}
