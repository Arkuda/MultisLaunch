package net.ftb.workers;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.SwingWorker;

import net.ftb.util.AppUtils;
import net.ftb.util.ErrorUtils;

/**
 * SwingWorker that logs into minecraft.net. Returns a string containing the response received from the server.
 */
public class LoginWorker extends SwingWorker<String, Void> {
	private String username, password;

	public LoginWorker(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Override
	protected String doInBackground() {
		 return "1351157607000:deprecated:" + this.username + ":6c83666eafdc4420aecca854f658c25e:6c83666eafdc4420aecca854f658c25e";
	}
	
}
