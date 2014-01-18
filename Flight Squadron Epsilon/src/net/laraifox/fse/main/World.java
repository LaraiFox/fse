package net.laraifox.fse.main;

public class World {
	public static final float GRAVITATIONAL_FORCE = 9.81f / 200.0f;
	public static final float ATMOSPHERIC_GAS_CONSTANT = 287.1f;

	private static float temperature = convertToKelvin(15.0f);
	private static float pressure = 101325.0f;

	public static float getTemperature() {
		return temperature;
	}

	public static float getPressure() {
		return pressure;
	}

	public static float convertToKelvin(float celcius) {
		return 273.15f + celcius;
	}
}
