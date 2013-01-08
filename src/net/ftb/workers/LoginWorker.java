////////////////////////
//THIS FILE BEEN MODED//
////////////////////////

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
	private boolean ispremium;

	public LoginWorker(String username, String password, boolean ispremium) {
		super();
		this.username = username;
		this.password = password;
		this.ispremium = ispremium;
	}

	@Override
	protected String doInBackground() {
		if(ispremium)
		{
			try {
				return AppUtils.downloadString(new URL("https://login.minecraft.net/?user=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&version=13"));
			} catch(IOException e) {
				ErrorUtils.tossError("IOException, minecraft servers might be down. Check @ help.mojang.com");
				return "";
			}
		}
		else {
			return "1351157607000:deprecated:" + this.username + ":6c83666eafdc4420aecca854f658c25e:6c83666eafdc4420aecca854f658c25e";
	
		}
	}
	
}
