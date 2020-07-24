package com.sadik.web.model.user;

import com.sadik.web.model.response.OperationResponse;

public class UserResponse extends OperationResponse {
	private User data = new User();

	public User getData() {
		return data;
	}

	public void setData(User data) {
		this.data = data;
	}
}
