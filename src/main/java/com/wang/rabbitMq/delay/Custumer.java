package com.wang.rabbitMq.delay;

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

public class Custumer {
	private final static String DELAY_QUEUE="delayqueue";
	private final static String DELAY_EXCHANGE="delayexchange";
	
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		ConnectionFactory factory=ConnectFactorySelf.newInstance();
		Connection connection=factory.newConnection();
		final Channel channel=connection.createChannel();
		channel.queueDeclare(DELAY_QUEUE,false, false, false, null);
		channel.exchangeDeclare(DELAY_EXCHANGE, "fanout");
		channel.queueBind(DELAY_QUEUE, DELAY_EXCHANGE, "");
		Consumer consumer=new DefaultConsumer(channel) {
			@Override
    	    public void handleDelivery(String consumerTag,Envelope envelope,AMQP.BasicProperties properties,byte[] body)throws IOException{
    			String message = new String(body, "UTF-8");
                System.out.println(" 消费者 延时  Received '" + message + "'"+new Date());
                try {
					Thread.sleep(80);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
                channel.basicAck(envelope.getDeliveryTag(), false);
    	    }
		};
		
		channel.basicConsume(DELAY_QUEUE, false,consumer);
	}
}
