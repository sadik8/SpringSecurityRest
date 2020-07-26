package com.sadik.web.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sadik.web.dao.user.UserDetailsDao;
import com.sadik.web.identity.TokenUser;
import com.sadik.web.model.user.User;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
	
	@Autowired
	private UserDetailsDao userDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findOneByUserId(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		TokenUser currentUser;
        if (user.isActive()){
            currentUser = new TokenUser(user);
        }
        else{
            throw new DisabledException("User is not activated (Disabled User)");
        }
        return currentUser;
	}

}
