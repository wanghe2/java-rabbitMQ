package com.wang.rabbitMq.delay;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.wang.rabbitMq.common.ConnectFactorySelf;
/**
 * 做一个延时队列
 * @author wanghe
 *
 */
public class Sender {
	/**
	 * TTL_QUEUE是一个死亡队列，这个队列是不放置消费者，当消息发出去后，超时后被踢出去，进入死亡路由
	 * 根据死亡路由，进入相应的处理队列DELAY_QUEUE
	 */
	private final static String TTL_QUEUE="ttlqueue";
	private final static String TTL_EXCHANGE="ttlexchange";
	private final static String DELAY_EXCHANGE="delayexchange";
	private final static String DELAY_ROUTE_KEY="delayexchange";
	
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		ConnectionFactory factory=ConnectFactorySelf.newInstance();
		Connection connection=factory.newConnection();
		Channel channel=connection.createChannel();
		
		Map<String, Object> arguments=new HashMap<String, Object>();
		arguments.put("x-dead-letter-exchange", DELAY_EXCHANGE) ;
		arguments.put("x-dead-letter-routing-key", DELAY_ROUTE_KEY);
		channel.queueDeclare(TTL_QUEUE, true, false, false, arguments);
		channel.exchangeDeclare(TTL_EXCHANGE, "fanout");
		channel.queueBind(TTL_QUEUE, TTL_EXCHANGE, "");
		
		
        String message="rabbitMQ delay queue";
        BasicProperties props = new AMQP.BasicProperties.Builder().expiration(String.valueOf(5 * 1000)).build();
        channel.basicPublish(TTL_EXCHANGE, "", props, message.getBytes());
		System.err.print("*******发了一条消息*****"+new Date());
		channel.close();
	    connection.close();
	}

}
