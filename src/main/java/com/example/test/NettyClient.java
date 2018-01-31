package com.example.test;

import com.example.net.MyDecoder;
import com.example.net.MyEncoder;
import com.example.proto.UserHandler.LoginData;
import com.example.proto.UserHandler.Packet;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class NettyClient {
	public static void main(String[] args) {
		EventLoopGroup group = new NioEventLoopGroup(1);
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
				pipeline.addLast(new LengthFieldPrepender(4));
				pipeline.addLast(new MyEncoder());
				pipeline.addLast(new MyDecoder());

				pipeline.addLast(new ChannelInboundHandlerAdapter() {

					@Override
					public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
						Packet packet = (Packet) msg;
						System.out.println(packet.getName());
					}

					@Override
					public void channelActive(ChannelHandlerContext ctx) throws Exception {
						LoginData.Builder data = LoginData.newBuilder();
						data.setUsername("wzy");
						data.setPassword(System.currentTimeMillis() + "");
						Packet.Builder packet = Packet.newBuilder();
						packet.setName("user/login");
						packet.setData(data.build().toByteString());
						ctx.writeAndFlush(packet.build());
					}

				});
			}
		}).option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.connect("localhost", 8888);
	}
}
