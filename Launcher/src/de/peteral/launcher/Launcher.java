package de.peteral.launcher;

import java.io.IOException;
import java.util.Timer;
import java.util.prefs.Preferences;

import de.peteral.launcher.model.Game;
import de.peteral.launcher.model.Person;
import de.peteral.launcher.model.Server;
import de.peteral.launcher.view.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Launcher extends Application {

	private static final String GAME_PATH = "gamePath";
	private static final String PERSON_ID = "personId";
	private static final String SERVER_URL = "serverUrl";
	private Stage primaryStage;
	private AnchorPane rootLayout;
	private Game game;
	private Person person;
	private Server server;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Launcher");

		createModel();

		initMainWindow();

		startGameObserver();
	}

	private void startGameObserver() {
		GameObserver gameObserver = new GameObserver(game, person, server);
		Timer timer = new Timer(true);
		timer.schedule(gameObserver, 0, 1000L);
	}

	private void createModel() {
		Preferences prefs = Preferences.userNodeForPackage(Launcher.class);

		game = new Game();
		game.path.set(prefs.get(GAME_PATH, game.path.get()));
		game.path.addListener((observable, oldValue, newValue) -> {
			prefs.put(GAME_PATH, newValue);
			try {
				prefs.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		person = new Person();
		person.id.set(prefs.get(PERSON_ID, person.id.get()));
		person.id.addListener((observable, oldValue, newValue) -> {
			prefs.put(PERSON_ID, newValue);
			try {
				prefs.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		server = new Server();
		server.url.set(prefs.get(SERVER_URL, server.url.get()));
		server.url.addListener((observable, oldValue, newValue) -> {
			prefs.put(SERVER_URL, newValue);
			try {
				prefs.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private void initMainWindow() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Launcher.class.getResource("view/MainWindow.fxml"));

			rootLayout = (AnchorPane) loader.load();

			MainWindowController controller = loader.getController();
			controller.setGame(game);
			controller.setPerson(person);
			controller.setServer(server);
			controller.setStage(primaryStage);

			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add(Launcher.class.getResource("stylesheet.css").toString());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
