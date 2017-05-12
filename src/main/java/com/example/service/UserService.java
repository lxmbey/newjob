package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.BaseDao;
import com.example.entity.User;

@Service
public class UserService {
	@Autowired
	private BaseDao baseDao;

	public User get(int id) {
		return baseDao.get(id, User.class);
	}
}
