package com.sadik.web.dao.user;

import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sadik.web.model.user.User;

@Repository
//@Transactional
public class UserDetailsDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public Optional<User> findOneByUserId(String username) {
		User user = (User) sessionFactory.getCurrentSession().get(User.class, username);
		return Optional.of(user);
	}
}
