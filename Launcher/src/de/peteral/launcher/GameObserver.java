package de.peteral.launcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TimerTask;

import de.peteral.launcher.model.Game;
import de.peteral.launcher.model.Person;
import de.peteral.launcher.model.Server;
import javafx.application.Platform;

public class GameObserver extends TimerTask {

	private static final int SERVER_UPDATE_PERIOD = 60;
	private Game game;
	private int counter = 0;
	private ServerReporter serverReporter;

	public GameObserver(Game game, Person person, Server server) {
		this.game = game;
		this.serverReporter = new ServerReporter(server, person);
	}

	@Override
	public void run() {
		try {
			counter++;
			String line;
			StringBuilder pidInfo = new StringBuilder();

			Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");

			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

			while ((line = input.readLine()) != null) {
				pidInfo.append(line);
			}

			input.close();

			boolean running = pidInfo.toString().contains("thinclient.exe");

			if (!game.running.get() && running) {
				// trigger server update when state just changed to running
				counter = SERVER_UPDATE_PERIOD;
			}

			Platform.runLater(() -> {
				game.running.set(running);
			});

			if (counter >= SERVER_UPDATE_PERIOD) {
				counter = 0;

				if (running) {
					serverReporter.report();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
