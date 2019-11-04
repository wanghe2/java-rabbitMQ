package com.wang.rabbitMq.direct;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.wang.rabbitMq.common.ConnectFactorySelf;

/**
 * 消息发送方----路由模式
 * @author wanghe
 *
 */
public class Sender {
	
	/**
	 * 队列的名字
	 */
	private final static String EXCHANGE_NAME = "direct_exchange";
	
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		ConnectionFactory factory=ConnectFactorySelf.newInstance();
		Connection connection=factory.newConnection();
		Channel channel=connection.createChannel();
		/**
		 * 可以用来负载均衡，多个接收者时，效率快的，接收的多(能者多劳模式，必须将确认设置成手动状态）
		 */
        channel.basicQos(1);
		/**
		 * 发送之前，我们必须声明消息要发往哪个队列，然后我们可以向队列发一条消息
		 */
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String message="rabbitMQ direct exchange";
		//publish,这是消息发布(发送多条消息到队列)
        channel.basicPublish(EXCHANGE_NAME, "route-key-2", null, message.getBytes());
		System.err.print("*******发了一条消息*****"+new Date());
		channel.close();
	    connection.close();
	}
}
