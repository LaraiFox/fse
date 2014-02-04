package net.laraifox.fse;

import java.io.File;
import java.io.IOException;

import net.laraifox.fse.main.GameDisplay;
import net.laraifox.fse.main.Settings;
import net.laraifox.lib.graphics.OrthographicProjection;

import org.lwjgl.LWJGLException;

public class BootFlightSquadronEpsilon {

	public static void main(String[] args) {
		int windowWidth = 800;
		int windowHeight = 600;

		String operatingSystem = System.getProperty("os.name").toLowerCase();
		String username = System.getProperty("user.name");
		String programFolder = Settings.getProgramFolderName();

		if (operatingSystem.contains("win")) {
			System.setProperty("org.lwjgl.librarypath", new File("res/lwjgl-natives/windows").getAbsolutePath());

			Settings.setProgramDirectory("/Users/" + username + "/AppData/Roaming/" + programFolder);

			windowWidth = 1600;
			windowHeight = 900;
		} else if (operatingSystem.contains("mac")) {
			System.setProperty("org.lwjgl.librarypath", new File("res/lwjgl-natives/macosx").getAbsolutePath());
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Flight Squadron Epsilon");

			Settings.setProgramDirectory("/Users/" + username + "/Library/" + programFolder);

			windowWidth = 1120;
			windowHeight = 630;
		} else if (operatingSystem.contains("linux")) {
			System.setProperty("org.lwjgl.librarypath", new File("res/lwjgl-natives/linux").getAbsolutePath());

			Settings.setProgramDirectory("/home/" + username + programFolder);
		} else {
			System.err.println("Your Operating System (" + operatingSystem + ") is unrecognised or unsupported");
			new Exception().printStackTrace();
			System.exit(1);
		}

		try {
			Settings.initializeProgramDirectories();
			Settings.loadSettings();

			GameDisplay programDisplay = new GameDisplay(windowWidth, windowHeight);
			programDisplay.setOrthographicProjection(new OrthographicProjection(0.1f, 1000.0f));
			programDisplay.intitialize();
			programDisplay.start();

			Settings.saveSettings();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
}
