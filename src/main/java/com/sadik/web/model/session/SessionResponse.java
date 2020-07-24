package com.sadik.web.model.session;

import com.sadik.web.model.response.OperationResponse;

public class SessionResponse extends OperationResponse {
	private SessionItem item;

	public SessionItem getItem() {
		return item;
	}

	public void setItem(SessionItem item) {
		this.item = item;
	}

}
