package net.laraifox.fse.main;

import net.laraifox.lib.graphics.Mesh;
import net.laraifox.lib.graphics.MeshLoader;
import net.laraifox.lib.graphics.Transformf;
import net.laraifox.lib.math.MathHelper;
import net.laraifox.lib.math.Matrix4f;
import net.laraifox.lib.math.Vector2f;
import net.laraifox.lib.math.Vector3f;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class Fighter {

	private static final float HORIZONTAL_THRUST_INCREMENT_RATE = 0.3f;
	// private static final float HORIZONTAL_THRUST_RESET_RATE = 1.0f;
	private static final float HORIZONTAL_THRUST_MIN = 0.0f;
	private static final float HORIZONTAL_THRUST_MAX = 10.0f;

	private static final float VERTICLE_THRUST_INCREMENT_RATE = 0.3f;
	private static final float VERTICLE_THRUST_RESET_RATE = 0.2f;
	private static final float VERTICLE_THRUST_IDLE = 3.5f; // Must be a non-zero amount to partially counteract gravity
	private static final float VERTICLE_THRUST_MIN = -2.0f;
	private static final float VERTICLE_THRUST_MAX = 5.0f;

	private static final float ROTATION_SPEED_PITCH = 1.0f;
	private static final float ROTATION_SPEED_YAW = 0.3f;
	private static final float ROTATION_SPEED_ROLL = 1.5f;

	private static final float MOUSE_FLIGHT_SENSITIVITY = 0.1f;

	private float worldRhoValue = World.getPressure() / (World.ATMOSPHERIC_GAS_CONSTANT * World.getTemperature()) * 0.2f;

	private Mesh modelMesh;
	private Transformf transform;
	private Vector3f forward;
	private Vector3f upward;

	private float throttleLevel;
	private float hoverLevel;
	private Vector3f velocity;
	private float weight;

	public Fighter(Vector3f position) {
		this.modelMesh = MeshLoader.loadMesh("./res/models/fighters/f14d.obj");
		this.transform = new Transformf();
		transform.setTranslation(position);
		this.forward = Vector3f.Forward();
		this.upward = Vector3f.Up();

		this.throttleLevel = HORIZONTAL_THRUST_MIN;
		this.hoverLevel = VERTICLE_THRUST_IDLE;
		this.velocity = new Vector3f();
		this.weight = 100.0f;
	}

	/**
	 * Handle user input to affect the aircraft's flight.
	 * 
	 * @param mouseDX
	 * @param mouseDY
	 */
	public void handleInput(float mouseDX, float mouseDY) {
		if (Keyboard.isKeyDown(Input.AIRCRAFT_THROTTLE_INCREASE))
			throttleLevel = MathHelper.clamp(throttleLevel + HORIZONTAL_THRUST_INCREMENT_RATE, HORIZONTAL_THRUST_MIN, HORIZONTAL_THRUST_MAX);
		if (Keyboard.isKeyDown(Input.AIRCRAFT_THROTTLE_DECREASE))
			throttleLevel = MathHelper.clamp(throttleLevel - HORIZONTAL_THRUST_INCREMENT_RATE, HORIZONTAL_THRUST_MIN, HORIZONTAL_THRUST_MAX);

		if (((!Keyboard.isKeyDown(Input.AIRCRAFT_VERTICLE_THRUST_INCREASE) && !Keyboard.isKeyDown(Input.AIRCRAFT_VERTICLE_THRUST_DECREASE)) || (Keyboard
				.isKeyDown(Input.AIRCRAFT_VERTICLE_THRUST_INCREASE) && Keyboard.isKeyDown(Input.AIRCRAFT_VERTICLE_THRUST_DECREASE)))) {
			if (hoverLevel != VERTICLE_THRUST_IDLE) {
				if (hoverLevel > VERTICLE_THRUST_IDLE - VERTICLE_THRUST_RESET_RATE && VERTICLE_THRUST_IDLE + VERTICLE_THRUST_RESET_RATE > hoverLevel)
					hoverLevel = VERTICLE_THRUST_IDLE;
				else if (hoverLevel > VERTICLE_THRUST_IDLE)
					hoverLevel -= VERTICLE_THRUST_RESET_RATE;
				else if (hoverLevel < VERTICLE_THRUST_IDLE)
					hoverLevel += VERTICLE_THRUST_RESET_RATE;
			}
		} else if (Keyboard.isKeyDown(Input.AIRCRAFT_VERTICLE_THRUST_INCREASE))
			hoverLevel = MathHelper.clamp(hoverLevel + VERTICLE_THRUST_INCREMENT_RATE, VERTICLE_THRUST_MIN, VERTICLE_THRUST_MAX);
		else if (Keyboard.isKeyDown(Input.AIRCRAFT_VERTICLE_THRUST_DECREASE))
			hoverLevel = MathHelper.clamp(hoverLevel - VERTICLE_THRUST_INCREMENT_RATE, VERTICLE_THRUST_MIN, VERTICLE_THRUST_MAX);

		float drx = mouseDY * -MOUSE_FLIGHT_SENSITIVITY;
		float dry = 0.0f;
		float drz = mouseDX * MOUSE_FLIGHT_SENSITIVITY;

		if (Keyboard.isKeyDown(Input.AIRCRAFT_PITCH_UP_KEY) || Mouse.isButtonDown(Input.AIRCRAFT_PITCH_UP_BUTTON))
			drx -= ROTATION_SPEED_PITCH;
		if (Keyboard.isKeyDown(Input.AIRCRAFT_PITCH_DOWN_KEY) || Mouse.isButtonDown(Input.AIRCRAFT_PITCH_DOWN_BUTTON))
			drx += ROTATION_SPEED_PITCH;
		if (Keyboard.isKeyDown(Input.AIRCRAFT_YAW_LEFT))
			dry -= ROTATION_SPEED_YAW;
		if (Keyboard.isKeyDown(Input.AIRCRAFT_YAW_RIGHT))
			dry += ROTATION_SPEED_YAW;
		if (Keyboard.isKeyDown(Input.AIRCRAFT_ROLL_LEFT))
			drz -= ROTATION_SPEED_ROLL;
		if (Keyboard.isKeyDown(Input.AIRCRAFT_ROLL_RIGHT))
			drz += ROTATION_SPEED_ROLL;

		if (drx != 0.0f) {
			Vector3f xAxis = Vector3f.cross(forward, upward).normalize();
			forward.rotate(drx, xAxis).normalize();
			upward = Vector3f.cross(xAxis, forward).normalize();
		}
		if (dry != 0.0f) {
			Vector3f xAxis = Vector3f.cross(forward, upward).normalize();
			xAxis.rotate(dry, upward).normalize();
			forward = Vector3f.cross(upward, xAxis).normalize();
		}
		if (drz != 0.0f) {
			upward.rotate(drz, forward).normalize();
		}
	}

	public void update(float mouseDX, float mouseDY) {
		handleInput(mouseDX, mouseDY);

		Vector3f momentum = Vector3f.scale(velocity, weight);

		Vector3f thrustForce = calculateThrustVector();
		Vector3f liftForce = calculateLiftVector();
		Vector3f gravityVector = calculateGravityVector();
		Vector3f dragForce = calculateDragVector();

		// Calculate the final change in momentum for the aircraft and update the velocity and position
		Vector3f deltaMomentum = Vector3f.sum(thrustForce, liftForce, gravityVector, dragForce);
		momentum.add(deltaMomentum);

		velocity.set(momentum.scale(1.0f / weight));

		transform.getTranslation().add(velocity);

		/*** Output of debugging data for on-screen display ***/
		GameDisplay.addStringToBuffer("");
		GameDisplay.addStringToBuffer("Position:    " + transform.getTranslation().toString(1));
		GameDisplay.addStringToBuffer("Forward:     " + forward.toString(1));
		GameDisplay.addStringToBuffer("Upward:      " + upward.toString(1));
		GameDisplay.addStringToBuffer("");
		GameDisplay.addStringToBuffer("Velocity:    " + velocity.toString(1));
		GameDisplay.addStringToBuffer("Airspeed:    " + velocity.length() + "  -  " + velocity.dot(forward.normalize()));
		GameDisplay.addStringToBuffer("");
		GameDisplay.addStringToBuffer("Gravity:     " + gravityVector.toString(1));
		GameDisplay.addStringToBuffer("H Thrust:    " + throttleLevel);
		GameDisplay.addStringToBuffer("V Thrust:    " + hoverLevel);
	}

	/**
	 * Calculate the horizontal and vertical engine thrust effects on the aircraft's velocity.
	 * 
	 * @return Vector3f - the sum of both horizontal and vertical thrust vectors.
	 */
	private Vector3f calculateThrustVector() {
		Vector3f thrustForce = new Vector3f();

		Vector3f horizontalThrust = Vector3f.normalize(forward).negate();
		horizontalThrust.scale(throttleLevel);

		Vector3f verticalThrust = Vector3f.normalize(upward);

		float alpha = Vector3f.Up().dot(upward.normalize());

		verticalThrust.scale(hoverLevel * 0.4f);
		verticalThrust.add(Vector3f.Up().scale(hoverLevel * alpha * 0.6f));

		thrustForce.set(Vector3f.add(horizontalThrust, verticalThrust));

		return thrustForce;
	}

	/**
	 * Calculate the lift generated by the aircraft's airspeed and pitch.
	 * 
	 * @return Vector3f - the lift vector.
	 */
	private Vector3f calculateLiftVector() {
		Vector3f liftForce = new Vector3f();

		Vector2f sideVector = new Vector2f();
		Vector2f topVector = forward.getXZ();
		float topArctangent = (float) Math.abs(Math.toDegrees(Math.atan2(topVector.getX(), -topVector.getY())));
		if (135 > topArctangent && topArctangent > 45)
			sideVector = forward.getXY();
		else
			sideVector = forward.getYZ().reverse();

		sideVector.normalize().absolute();

		float alpha = Vector2f.Right().dot(sideVector);// * 10.0f;
		float horizontalAirspeed = (sideVector.getX() * velocity.length()) * alpha;
		float liftFactor = 0.1f * horizontalAirspeed;

		liftForce = Vector3f.scale(upward, liftFactor);

		return liftForce;
	}

	/**
	 * Calculate the effects of gravity on the aircraft's velocity.
	 * 
	 * @return Vector3f - the gravity vector.
	 */
	private Vector3f calculateGravityVector() {
		Vector3f gravityVector = Vector3f.scale(Vector3f.Down(), World.GRAVITATIONAL_FORCE * weight);

		float horizontalThrustScale = (HORIZONTAL_THRUST_MAX - throttleLevel) / HORIZONTAL_THRUST_MAX;
		float verticalThrustScale = (VERTICLE_THRUST_MAX - (hoverLevel - VERTICLE_THRUST_IDLE)) / VERTICLE_THRUST_MAX;
		verticalThrustScale *= Math.abs(Vector3f.Up().dot(upward.normalize()));

		// gravityVector.scale(horizontalThrustScale * verticalThrustScale);

		return gravityVector;
	}

	/**
	 * Calculate the drag on the aircraft based on velocity and size.
	 * 
	 * @return Vector3f - the drag vector.
	 */
	private Vector3f calculateDragVector() {
		Vector3f dragForce = new Vector3f();

		float airspeed = velocity.length();
		float area = 10.0f;
		float dragCoefficient = 0.75f;
		float dragFactor = 0.5f * worldRhoValue * airspeed * dragCoefficient * area;

		Vector3f headingVector = Vector3f.normalize(velocity);

		dragForce = Vector3f.scale(headingVector.negate(), dragFactor);

		return dragForce;
	}

	public void render(SimpleShader shader) {
		Matrix4f translationMatrix = new Matrix4f().initializeTranslation(transform.getTranslation().getX(), transform.getTranslation().getY(), transform
				.getTranslation().getZ());
		Matrix4f rotationMatrix = new Matrix4f().initializeRotation(forward, upward);
		Matrix4f scaleMatrix = new Matrix4f().initializeScale(transform.getScale().getX(), transform.getScale().getY(), transform.getScale().getZ());
		Matrix4f transformationMatrix = translationMatrix.multiply(rotationMatrix.multiply(scaleMatrix));

		// GameDisplay.addStringToBuffer(rotationMatrix.toString());

		shader.updateUniforms(transform.createProjectedTransformation(transformationMatrix), new Vector3f(1.0f, 1.0f, 1.0f));
		modelMesh.render();

		// drawAxes(10, false);
	}

	@SuppressWarnings("unused")
	private void drawAxes(float length, boolean drawNegatives) {
		GL11.glLineWidth(3.0f);
		GL11.glBegin(GL11.GL_LINES);

		// X axis marker
		GL11.glColor3f(1, 0, 0);
		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(length, 0, 0);

		// Y axis marker
		GL11.glColor3f(0, 1, 0);
		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(0, length, 0);

		// Z axis marker
		GL11.glColor3f(0, 0, 1);
		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(0, 0, -length * 1.2f);

		GL11.glEnd();

		if (drawNegatives) {
			GL11.glLineWidth(0.5f);
			GL11.glBegin(GL11.GL_LINES);

			// -X axis marker
			GL11.glColor3f(1, 0, 0);
			GL11.glVertex3f(0, 0, 0);
			GL11.glVertex3f(-length, 0, 0);

			// -Y axis marker
			GL11.glColor3f(0, 1, 0);
			GL11.glVertex3f(0, 0, 0);
			GL11.glVertex3f(0, -length, 0);

			// -Z axis marker
			GL11.glColor3f(0, 0, 1);
			GL11.glVertex3f(0, 0, 0);
			GL11.glVertex3f(0, 0, length);

			GL11.glEnd();
		}

		GL11.glLineWidth(1.0f);
	}

	public Vector3f getCameraPosition() {
		Vector3f cameraPosition = new Vector3f(transform.getTranslation());
		cameraPosition.add(Vector3f.scale(forward, 20.0f));
		cameraPosition.add(Vector3f.scale(upward, 2.0f));

		return cameraPosition;
	}

	public Vector3f getForward() {
		return new Vector3f(forward).negate();
	}

	public Vector3f getUpward() {
		return new Vector3f(upward);
	}
}
