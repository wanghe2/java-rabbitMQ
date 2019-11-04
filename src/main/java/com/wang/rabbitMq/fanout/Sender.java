package com.wang.rabbitMq.fanout;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.wang.rabbitMq.common.ConnectFactorySelf;

/**
 * 消息发送方---发布订阅模式
 * @author wanghe
 *
 */
public class Sender {
	
	/**
	 * 交换器的名字
	 */
	private final static String EXCHANGE_NAME = "fanout_exchange";
	
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		ConnectionFactory factory=ConnectFactorySelf.newInstance();
		Connection connection=factory.newConnection();
		Channel channel=connection.createChannel();
		/**
		 * 可以用来负载均衡，多个接收者时，效率快的，接收的多(能者多劳模式，必须将确认设置成手动状态）
		 */
        channel.basicQos(1);
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String message="rabbitMq fanout ";
		channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
		channel.close();
	    connection.close();
	}
}
