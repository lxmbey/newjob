package com.example.net;

import io.netty.channel.Channel;

public class Request {
	public Packet packet;
	public Channel channel;

	public void sendMessage() {
		channel.writeAndFlush(packet);
	}
}
