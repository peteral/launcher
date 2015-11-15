package de.peteral.launcher.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Game {
	private static final String GAME_NOT_RUNNING = "Spiel starten...";
	private static final String GAME_RUNNING = "Spiel läuft...";
	private static final String DEFAULT_GAME_PATH = "C:\\Program Files (x86)\\Drakensang Online";

	public BooleanProperty running = new SimpleBooleanProperty(false);
	public StringProperty path = new SimpleStringProperty(DEFAULT_GAME_PATH);
	public StringProperty status = new SimpleStringProperty(GAME_NOT_RUNNING);

	public Game() {
		running.addListener((observer, oldValue, newValue) -> {
			if (newValue) {
				status.set(GAME_RUNNING);
			} else {
				status.set(GAME_NOT_RUNNING);
			}
		});
	}
}
