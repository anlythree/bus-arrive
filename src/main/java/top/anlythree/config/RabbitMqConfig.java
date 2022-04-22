package top.anlythree.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitMqConfig implements RabbitTemplate.ConfirmCallback,
RabbitTemplate.ReturnCallback{

    /**
     * 该方法是消息加入交换机的回调函数
     * @param correlationData
     * @param b
     * @param s
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        if(b){
            System.out.println("消息加入mq交换机成功，消息内容："+correlationData);
        }else {
            System.out.println("消息加入mq交换机失败，消息内容："+correlationData);
        }
    }

    /**
     * 该方法是消息未成功加入队列的回调函数
     * @param message
     * @param i
     * @param s
     * @param s1
     * @param s2
     */
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        System.out.println("消息加入mq队列失败，消息内容："+message);
    }
}
