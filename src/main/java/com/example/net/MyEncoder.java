package com.example.net;

import java.util.List;

import com.example.proto.UserHandler.Packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class MyEncoder extends MessageToMessageEncoder<Packet> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
		if (msg == null) {
			return;
		}
		byte[] bytes = msg.toByteArray();
		ByteBuf buf = Unpooled.buffer(bytes.length);
		buf.writeBytes(bytes);
		
		out.add(buf);
	}

}
