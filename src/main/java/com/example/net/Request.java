package com.example.net;

import com.example.proto.UserHandler.Packet;

import io.netty.channel.Channel;

public class Request {
	public Packet packet;
	public Channel channel;

	public void sendMessage() {
		channel.writeAndFlush(packet);
	}
}
