package com.sadik.web.controller.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sadik.web.model.response.OperationResponse.ResponseStatusEnum;
import com.sadik.web.model.session.SessionItem;
import com.sadik.web.model.session.SessionResponse;
import com.sadik.web.model.user.Login;
import com.sadik.web.model.user.Role;
import com.sadik.web.model.user.User;

@RestController
public class SessionController {

	//@Autowired private UserRepo userRepo;

	@RequestMapping(value = "/session", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public SessionResponse newSession(@RequestBody Login login, HttpServletRequest request,
			HttpServletResponse response) {
		User user = new User("sadik", "sadik", Role.ADMIN, "Sadik", "Hussain", true, "Lexmark", null, null, null, null,
				null, null, null, false, false);// TODO: userRepo.findOneByUserIdAndPassword(login.getUsername(),
												// login.getPassword()).orElse(null);
		SessionResponse resp = new SessionResponse();
		SessionItem sessionItem = new SessionItem();
		if (user != null) {
			sessionItem.setToken("xxx.xxx.xxx");
			sessionItem.setUserId(user.getUserId());
			sessionItem.setFirstName(user.getFirstName());
			sessionItem.setLastName(user.getLastName());
			sessionItem.setEmail(user.getEmail());
			// sessionItem.setRole(user.getRole());

			resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
			resp.setOperationMessage("Dummy Login Success");
			resp.setItem(sessionItem);
		} else {
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setOperationMessage("Login Failed");
		}
		return resp;
	}
}
