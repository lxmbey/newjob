package com.example.net;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

import com.google.gson.Gson;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class MyEncoder extends MessageToMessageEncoder<Packet> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
		String json = new Gson().toJson(msg);
		if (json.length() == 0) {
			return;
		}
		out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(json), Charset.defaultCharset()));
	}

}
