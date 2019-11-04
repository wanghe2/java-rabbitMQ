package com.wang.rabbitMq.qos;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.wang.rabbitMq.common.ConnectFactorySelf;

/**
 * 消息发送方----能者多劳模式
 * @author wanghe
 *
 */
public class Sender {
	
	/**
	 * 队列的名字
	 */
	private final static String QUEUE_NAME = "Hello";
	
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
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message="hello world";
		//publish,这是消息发布(发送多条消息到队列)
		for(int i=0;i<20;i++) {
			String newmessage=message+"--"+i;
			channel.basicPublish("", QUEUE_NAME, null, newmessage.getBytes());
			Thread.sleep(10);
			
		}
		System.err.print("*******发了一条消息*****"+new Date());
		channel.close();
	    connection.close();
	}
}
