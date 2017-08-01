package pl.piomin.services.shipment;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.SendTo;

import pl.piomin.service.common.message.Order;
import pl.piomin.service.common.message.Request;

@SpringBootApplication
@EnableBinding(Processor.class)
public class Application {

	@Autowired
	private ShipmentService shipmentService;
	
	protected Logger logger = Logger.getLogger(Application.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@StreamListener(Processor.INPUT)
	@SendTo(Processor.OUTPUT)
	public Request<Order> processOrder(Request<Order> order) {
		logger.info("Processing order: " + order);
		order.getData().setShipment(shipmentService.processOrder(order.getData()));
		logger.info("Output order: " + order);
		return order;
	}

	@Bean
	public AlwaysSampler defaultSampler() {
		return new AlwaysSampler();
	}

}
