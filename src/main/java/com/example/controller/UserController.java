package com.example.controller;

import org.springframework.stereotype.Controller;

import com.example.common.NotLogin;
import com.example.common.Push;
import com.example.net.Request;
import com.example.net.Packet;
import com.example.proto.UserHandler.LoginData;
import com.example.proto.UserHandler.User;

import io.netty.channel.Channel;

@Controller("user")
public class UserController {

	@NotLogin
	public User login(Request request) throws Exception {
		LoginData data = LoginData.parseFrom(request.packet.data);
		System.out.println(data);
		User.Builder user = User.newBuilder();
		user.setId("111");
		user.setName("lxm");

		showMessage(data, request.channel);
		return user.build();
	}

	@Push
	public void showMessage(LoginData data, Channel channel) {
		String name = this.getClass().getAnnotation(Controller.class).value();
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		name += "/" + method;
		Request session = new Request();
		session.packet = new Packet(name, data.toByteArray());
		session.channel = channel;
		session.sendMessage();
	}
}
