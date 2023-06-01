package ibf2022.batch3.assessment.csf.orderbackend.respositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrder;

@Repository
public class OrdersRepository {

	@Autowired
	private MongoTemplate mongo;

	// TODO: Task 3
	// WARNING: Do not change the method's signature.
	// Write the native MongoDB query in the comment below
	// db.orders.insertOne(
	// {
	// _id: "12345678",
	// date: new Date(Date.now()),
	// total: "100.00",
	// name: "David",
	// email: "davvyap@gmail.com",
	// sauce: "Signature Sauce",
	// comments: "The pizza is so good!",
	// toppings: ["Chicken","Seafood"]
	// }
	// )
	public void add(PizzaOrder order) {
		Document d = convertPizzaOrderToMongoDocument(order);
		mongo.insert(d, "orders");
	}

	// TODO: Task 6
	// WARNING: Do not change the method's signature.
	// Write the native MongoDB query in the comment below
	// db.orders.aggregate([
	// { $match: { $and:[ { email: 'davvyap@gmail.com'}, { delivered: {$exists :
	// false} } ]}},
	// { $project: {date:1, total:1} },
	// { $sort: {date:-1} }
	// ])
	public List<PizzaOrder> getPendingOrdersByEmail(String email) {
		MatchOperation mop = Aggregation.match(Criteria.where("email").is(email).and("delivered").exists(false));

		ProjectionOperation pop = Aggregation.project("date", "total");

		SortOperation sop = Aggregation.sort(Sort.by(Direction.DESC, "date"));

		Aggregation pipeline = Aggregation.newAggregation(mop, pop, sop);

		AggregationResults<Document> rs = mongo.aggregate(pipeline, "orders", Document.class);

		List<Document> docs = rs.getMappedResults();

		List<PizzaOrder> orders = docs.stream().map(
				d -> convertDocumentTOPizzaOrder(d)).toList();

		return orders;
	}

	// TODO: Task 7
	// WARNING: Do not change the method's signature.
	// Write the native MongoDB query in the comment below
	// db.orders.updateOne({_id: 'e3efb596d7'}, { $set: {delivered: true} } )
	public boolean markOrderDelivered(String orderId) {
		Criteria criteria = Criteria.where("_id").is(orderId);
		Query query = new Query(criteria);
		Update update = new Update().set("delivered", true);

		UpdateResult updateResult = mongo.updateFirst(query, update, Document.class, "orders");

		long counts = updateResult.getModifiedCount();
		return counts > 0;
	}

	// EXTRA
	private Document convertPizzaOrderToMongoDocument(PizzaOrder order) {
		Document d = new Document();
		d.put("_id", order.getOrderId());
		d.put("date", order.getDate());
		d.put("total", order.getTotal());
		d.put("name", order.getName());
		d.put("email", order.getEmail());
		d.put("sauce", order.getSauce());
		d.put("size", order.getSize());
		d.put("comments", order.getComments());
		d.put("toppings", order.getTopplings());
		return d;
	}

	private PizzaOrder convertDocumentTOPizzaOrder(Document d) {
		PizzaOrder p = new PizzaOrder();
		p.setOrderId(d.getString("_id"));
		p.setDate(d.getDate("date"));
		p.setTotal(Float.parseFloat(d.getDouble("total").toString()));
		return p;
	}
}
