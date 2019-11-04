package com.wang.rabbitMq.common;

import com.rabbitmq.client.ConnectionFactory;

/**
 * Hello world!
 *
 */
public class ConnectFactorySelf 
{
	
    public static ConnectionFactory newInstance()
    {
		/**
		  *  connection是socket连接的抽象，并且为我们管理协议版本协商（protocol version negotiation），
		  *  认证（authentication ）等等事情。这里我们要连接的消息代理在本地，因此我们将host设为“localhost”。
		  *  如果我们想连接其他机器上的代理，只需要将这里改为特定的主机名或IP地址。
		 */
    	ConnectionFactory connectionFactory=new ConnectionFactory();
		connectionFactory.setHost("127.0.0.1");
		connectionFactory.setPort(5672);
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		return connectionFactory;
    }
    
}
