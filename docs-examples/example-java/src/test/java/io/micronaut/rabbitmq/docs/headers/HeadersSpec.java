package io.micronaut.rabbitmq.docs.headers;

import io.micronaut.rabbitmq.AbstractRabbitMQTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HeadersSpec extends AbstractRabbitMQTest {

    @Test
    void testPublishingAndReceivingHeaders() {
        startContext();

// tag::producer[]
        ProductClient productClient = applicationContext.getBean(ProductClient.class);
        productClient.send("body".getBytes());
        productClient.send("medium", 20L, "body2".getBytes());
        productClient.send(null, 30L, "body3".getBytes());

        Map<String, Object> headers = new HashMap<>(3);
        headers.put("productSize", "large");
        headers.put("x-product-count", 40L);
        productClient.send(headers, "body4".getBytes());
// end::producer[]

        ProductListener productListener = applicationContext.getBean(ProductListener.class);

        waitFor(() ->
                productListener.messageProperties.size() == 4 &&
                productListener.messageProperties.contains("true|10|small") &&
                productListener.messageProperties.contains("true|20|medium") &&
                productListener.messageProperties.contains("true|30|null") &&
                productListener.messageProperties.contains("true|40|large")
        );
    }
}
