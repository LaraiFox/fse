package net.laraifox.fse.main;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Input {
	public static int MOUSE_BUTTON_LEFT = 0;
	public static int MOUSE_BUTTON_RIGHT = 1;
	public static int MOUSE_BUTTON_MIDDLE = 2;
	public static int MOUSE_BUTTON_FOUR = 3;
	public static int MOUSE_BUTTON_FIVE = 4;

	/*** Example declaration of a control assignment ***/
	public static int CONTROL_NONE = Keyboard.KEY_NONE;

	/*** Controls for piloting aircraft ***/
	public static int AIRCRAFT_PITCH_UP_KEY = Keyboard.KEY_DOWN;
	public static int AIRCRAFT_PITCH_UP_BUTTON = MOUSE_BUTTON_FOUR;
	public static int AIRCRAFT_PITCH_DOWN_KEY = Keyboard.KEY_UP;
	public static int AIRCRAFT_PITCH_DOWN_BUTTON = MOUSE_BUTTON_FIVE;
	public static int AIRCRAFT_YAW_LEFT = Keyboard.KEY_A;
	public static int AIRCRAFT_YAW_RIGHT = Keyboard.KEY_D;
	public static int AIRCRAFT_ROLL_LEFT = Keyboard.KEY_LEFT;
	public static int AIRCRAFT_ROLL_RIGHT = Keyboard.KEY_RIGHT;
	public static int AIRCRAFT_THROTTLE_INCREASE = Keyboard.KEY_W;
	public static int AIRCRAFT_THROTTLE_DECREASE = Keyboard.KEY_S;
	public static int AIRCRAFT_VERTICLE_THRUST_INCREASE = Keyboard.KEY_SPACE;
	public static int AIRCRAFT_VERTICLE_THRUST_DECREASE = Keyboard.KEY_LCONTROL;
	public static int AIRCRAFT_WEAPON_FIRE_BUTTON = MOUSE_BUTTON_LEFT;
	public static int AIRCRAFT_WEAPON_FIRE_KEY = Keyboard.KEY_NONE;
	public static int AIRCRAFT_WEAPON_ALTFIRE_BUTTON = MOUSE_BUTTON_RIGHT;
	public static int AIRCRAFT_WEAPON_ALTFIRE_KEY = Keyboard.KEY_NONE;
	public static int AIRCRAFT_WEAPON_PRIMARY = Keyboard.KEY_1;
	public static int AIRCRAFT_WEAPON_SECONDARY = Keyboard.KEY_2;
	public static int AIRCRAFT_WEAPON_TERTIARY = Keyboard.KEY_3;
	public static int AIRCRAFT_WEAPON_RELOAD = Keyboard.KEY_R;
	public static int AIRCRAFT_ACTIVATE_AFTERBURNER = Keyboard.KEY_LSHIFT;
	public static int AIRCRAFT_ACTIVATE_UTILITY = Keyboard.KEY_F;
	public static int AIRCRAFT_TARGET_SPOT = Keyboard.KEY_Q;

	/*** Controls for the free flying observer camera ***/
	public static int OBSERVER_CAMERA_LOOK_UP = Keyboard.KEY_UP;
	public static int OBSERVER_CAMERA_LOOK_DOWN = Keyboard.KEY_DOWN;
	public static int OBSERVER_CAMERA_LOOK_LEFT = Keyboard.KEY_LEFT;
	public static int OBSERVER_CAMERA_LOOK_RIGHT = Keyboard.KEY_RIGHT;
	public static int OBSERVER_CAMERA_MOVE_FORWARD = Keyboard.KEY_W;
	public static int OBSERVER_CAMERA_MOVE_BACKWARD = Keyboard.KEY_S;
	public static int OBSERVER_CAMERA_MOVE_LEFT = Keyboard.KEY_A;
	public static int OBSERVER_CAMERA_MOVE_RIGHT = Keyboard.KEY_D;
	public static int OBSERVER_CAMERA_MOVE_UP = Keyboard.KEY_SPACE;
	public static int OBSERVER_CAMERA_MOVE_DOWN = Keyboard.KEY_LCONTROL;
}
