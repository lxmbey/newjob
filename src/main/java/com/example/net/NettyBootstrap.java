package com.example.net;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.example.common.AllFunctions;
import com.example.common.AllFunctions.MethodWrapper;
import com.example.proto.UserHandler.Packet;
import com.google.protobuf.Message;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

@Component
public class NettyBootstrap {

	@PostConstruct
	public void start() {
		EventLoopGroup boss = new NioEventLoopGroup(1);
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(boss, boss).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
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
								Request request = new Request();
								request.packet = (Packet) msg;
								request.channel = ctx.channel();
								MethodWrapper method = AllFunctions.getMethod(request.packet.getName());
								if (method == null) {
									System.out.println("不存在的协议" + request.packet.getName());
									return;
								}
								Message message = (Message) method.method.invoke(method.instance, request);
								if (message != null) {
									Packet.Builder packet = Packet.newBuilder();
									packet.setName(request.packet.getName());
									packet.setData(message.toByteString());
									ctx.channel().writeAndFlush(packet.build());
								}
							}

						});
					}
				}).option(ChannelOption.SO_REUSEADDR, true).option(ChannelOption.TCP_NODELAY, true)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
				.childOption(ChannelOption.TCP_NODELAY, true)
				.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		bootstrap.bind(8888);
	}
}
