package com.example.net;

import java.nio.charset.Charset;
import java.util.List;

import com.google.gson.Gson;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class MyDecoder extends MessageToMessageDecoder<ByteBuf> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		String json = in.toString(Charset.defaultCharset());
		out.add(new Gson().fromJson(json, Packet.class));
	}

}
