package io.micronaut.rabbitmq.annotation

import io.micronaut.context.annotation.Requires
import io.micronaut.messaging.annotation.MessageMapping
import io.micronaut.rabbitmq.AbstractRabbitMQTest

class QueueAnnotationSpec extends AbstractRabbitMQTest {

    void 'test that @Queue value aliases to @MessageMapping'() {
        given:
        startContext()

        def definition = applicationContext.getBeanDefinition(MyConsumer)

        when:
        def method = definition.getRequiredMethod('receive', String)
        def annotationValue = method.getValue(MessageMapping, String[])

        then:
        annotationValue.isPresent()
        annotationValue.get().contains 'simple'
    }

    @Requires(property = 'spec.name', value = 'QueueAnnotationSpec')
    @RabbitListener
    static class MyConsumer {

        List<String> stuff = []

        @Queue('simple')
        void receive(String thing) {
            stuff << thing
        }
    }
}
