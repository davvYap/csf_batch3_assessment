package ibf2022.batch3.assessment.csf.orderbackend.respositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrder;
import jakarta.json.Json;

@Repository
public class PendingOrdersRepository {

	@Autowired
	@Qualifier("pending-orders")
	private RedisTemplate<String, String> redis;

	// TODO: Task 3
	// WARNING: Do not change the method's signature.
	public void add(PizzaOrder order) {
		redis.opsForValue().set((order.getOrderId()), this.toJsonString(order));
	}

	// TODO: Task 7
	// WARNING: Do not change the method's signature.
	public boolean delete(String orderId) {
		return redis.opsForValue().getOperations().delete(orderId);
	}

	public String toJsonString(PizzaOrder po) {
		return Json.createObjectBuilder()
				.add("orderId", po.getOrderId())
				.add("date", po.getDate().toString())
				.add("total", po.getTotal())
				.add("name", po.getName())
				.add("email", po.getEmail())
				.build()
				.toString();
	}

}
