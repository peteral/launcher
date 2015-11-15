package de.peteral.launcher.view;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import de.peteral.launcher.model.Game;
import de.peteral.launcher.model.Person;
import de.peteral.launcher.model.Server;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class MainWindowController {
	private static final String FAILED = "failed";
	private static final String OK = "ok";
	private Game game;
	private Person person;
	private Server server;
	private Stage stage;

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	private TextField serverUrl;

	@FXML
	private TextField personId;

	@FXML
	private Button launchGame;

	@FXML
	private TextField gameFolder;

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
		gameFolder.textProperty().bind(game.path);
		launchGame.textProperty().bind(game.status);
		game.running.addListener((observer, oldValue, newValue) -> {
			if (newValue) {
				launchGame.getStyleClass().add(OK);
			} else {
				launchGame.getStyleClass().remove(OK);
			}
		});
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
		personId.textProperty().bind(person.id);
		personId.textProperty().addListener((observer, oldValue, newValue) -> {
			stylePersonId(newValue);
		});
		stylePersonId(person.id.get());
	}

	private void stylePersonId(String newValue) {
		if (Person.DEFAULT_ID.equals(newValue)) {
			personId.getStyleClass().add(FAILED);
		} else {
			personId.getStyleClass().remove(FAILED);
		}
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
		serverUrl.textProperty().bind(server.url);

		server.failed.addListener((observer, oldValue, newValue) -> {
			if (newValue) {
				serverUrl.getStyleClass().add(FAILED);
			} else {
				serverUrl.getStyleClass().remove(FAILED);
			}
		});
	}

	@FXML
	private void initialize() {
	}

	@FXML
	private void changePerson() {
		TextInputDialog dialog = new TextInputDialog(person.id.get());
		dialog.setTitle("Eingabe");
		dialog.setContentText("Deine ID:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			person.id.set(result.get());
		}
	}

	@FXML
	private void startGame() {
		try {
			Runtime.getRuntime().exec("cmd /C \"start thinclient.exe\"", null, new File(game.path.get()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void changeGameFolder() {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setInitialDirectory(new File(game.path.get()));
		File selectedFile = chooser.showDialog(stage);
		if (selectedFile != null) {
			try {
				game.path.set(selectedFile.getCanonicalPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
