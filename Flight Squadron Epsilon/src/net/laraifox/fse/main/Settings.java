package net.laraifox.fse.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * <Summary> Settings Class <Summary>
 * 
 * The settings class will be a single static object that contains the main settings for the game. Most of these settings can be changed from the
 * options menu and some will be a part of the setting file however, the user can still change them if they know what they're doing.
 * 
 * This class must contain methods for saving and loading setting to and from a file the pathname of which will likely be stored as a private static
 * final String object in this class. There might be a method for retrieving the pathname if necessary however no outside class should be able to
 * change the value of the pathname.
 * 
 * If the pathname contains a null value then an exception is thrown when trying to load or save the settings.
 * 
 */
public class Settings {
	private static final String PROGRAM_FOLDER_NAME = "Flight Squadron Epsilon";
	private static final String SETTINGS_FILE_NAME = "setting.cfg";

	private static String programDirectory = "";

	private static int width, height;

	public static void initializeProgramDirectories() {
		File tmpFile = null;

		tmpFile = new File(programDirectory);
		if (!tmpFile.exists())
			tmpFile.mkdir();
	}

	public static void loadSettings() throws IllegalStateException, IOException {
		if (programDirectory.equals(""))
			throw new IllegalStateException("Program Directory was null. Failed to locate user program directory.");

		/*
		 * Load all the necessary settings from the settings file located under the directory stored in the constant "PATHNAME" and place them into
		 * their appropriate variables.
		 */

		File settingsFile = new File(programDirectory + "/" + SETTINGS_FILE_NAME);
		if (!settingsFile.exists()) {
			settingsFile.createNewFile();
			createDefaultSettingsFile();
		}

		FileInputStream inputStream = new FileInputStream(settingsFile);
		Properties properties = new Properties();
		properties.load(inputStream);
		String value = new String();

		value = properties.getProperty("resolution");
		if (value != null) {
			String[] separatedValues = value.split("x");
			width = Integer.parseInt(separatedValues[0]);
			height = Integer.parseInt(separatedValues[1]);
		}
		if (width <= 0 || height <= 0) {
			width = 1024;
			height = 640;
		}
	}

	public static void saveSettings() throws IllegalStateException, IOException {
		if (programDirectory.equals(""))
			throw new IllegalStateException("Program Directory was null. Failed to locate user program directory.");

		/*
		 * Save all the settings stored in the variables located in this class to the file under the directory stored in the constant "PATHNAME" using
		 * the appropriate format.
		 */

		File settingsFile = new File(programDirectory + "/" + SETTINGS_FILE_NAME);
		FileOutputStream outputStream = new FileOutputStream(settingsFile);
		Properties properties = new Properties();

		properties.setProperty("resolution", String.valueOf(width) + "x" + String.valueOf(height));

		properties.store(outputStream, "Tower Defense configuration file");
	}

	private static void createDefaultSettingsFile() throws IllegalStateException, IOException {
		width = 1600;
		height = 900;

		saveSettings();
	}

	public static void setProgramDirectory(String programDirectory) {
		Settings.programDirectory = programDirectory;
		File programDirectoryFolder = new File(programDirectory);
		if (!programDirectoryFolder.exists())
			programDirectoryFolder.mkdirs();
	}

	public static String getProgramDirectory() {
		return programDirectory;
	}

	public static String getProgramFolderName() {
		return PROGRAM_FOLDER_NAME;
	}

	public static String getSettingsFileName() {
		return SETTINGS_FILE_NAME;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	public static void setResolution(int width, int height) {
		Settings.width = width;
		Settings.height = height;
	}
}
