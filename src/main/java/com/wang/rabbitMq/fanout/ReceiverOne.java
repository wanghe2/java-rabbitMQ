package com.wang.rabbitMq.fanout;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.wang.rabbitMq.common.ConnectFactorySelf;

/**
 * 消息接收方
 * @author wanghe
 *
 */
public class ReceiverOne {

	private final static String QUEUE_NAME = "fanout_queue_1";
	private final static String EXCHANGE_NAME = "fanout_exchange";
	
    public static void main(String[] args) throws IOException, TimeoutException {
    	ConnectionFactory factory=ConnectFactorySelf.newInstance();
    	Connection connection= factory.newConnection();
    	final Channel channel= connection.createChannel();
    	channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    	//绑定队列到交换机
    	channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
    	//同一时刻服务器只会发送一条消息给消费者
    	channel.basicQos(1);
    	Consumer consumer=new DefaultConsumer(channel) {
    		@Override
    	    public void handleDelivery(String consumerTag,Envelope envelope,AMQP.BasicProperties properties,byte[] body)throws IOException{
    			String message = new String(body, "UTF-8");
                System.out.println(" 消费者1 Received '" + message + "'"+new Date());
                try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
    	};
    	//consume,消息接收
    	channel.basicConsume(QUEUE_NAME, false,consumer);
    }
	
}
