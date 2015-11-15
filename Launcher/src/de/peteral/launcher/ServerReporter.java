package de.peteral.launcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import de.peteral.launcher.model.Person;
import de.peteral.launcher.model.Server;
import javafx.application.Platform;

public class ServerReporter {

	private static final int HTTP_OK = 200;
	private Server server;
	private Person person;

	public ServerReporter(Server server, Person person) {
		this.server = server;
		this.person = person;
	}

	public void report() {
		try {
			URL url = new URL(server.url.get() + "/rest/public/report/" + person.id.get().replaceAll(" ", "_"));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			int responseCode = connection.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			setServerFailure(responseCode != HTTP_OK);

		} catch (IOException e) {
			e.printStackTrace();
			setServerFailure(true);
		}
	}

	private void setServerFailure(boolean failed) {
		Platform.runLater(() -> {
			server.failed.set(failed);
		});
	}

}
