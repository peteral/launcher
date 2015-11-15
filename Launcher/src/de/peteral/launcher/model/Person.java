package de.peteral.launcher.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Person {
	public static final String DEFAULT_ID = "Bitte Deine ID eintragen...";
	public StringProperty id = new SimpleStringProperty(DEFAULT_ID);
}
