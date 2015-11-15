package de.peteral.launcher.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Server {
	private static final String DEFAULT_URL = "http://peteral-wildfly1.rhcloud.com/";

	public BooleanProperty failed = new SimpleBooleanProperty(false);
	public StringProperty url = new SimpleStringProperty(DEFAULT_URL);
}
