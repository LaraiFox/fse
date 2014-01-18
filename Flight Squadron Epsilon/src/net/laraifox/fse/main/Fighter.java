package net.laraifox.fse.main;

import net.laraifox.lib.graphics.Color3f;
import net.laraifox.lib.graphics.Mesh;
import net.laraifox.lib.graphics.MeshLoader;
import net.laraifox.lib.graphics.Transformf;
import net.laraifox.lib.math.MathHelper;
import net.laraifox.lib.math.Matrix3f;
import net.laraifox.lib.math.Matrix4f;
import net.laraifox.lib.math.Vector3f;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class Fighter {
	private float HORIZONTAL_THRUST_MIN = 0.0f;
	private float HORIZONTAL_THRUST_MAX = 0.0f;
	private float VERTICLE_THRUST_MIN = 0.0f; // Must be a non-zero amount to partially counteract gravity
	private float VERTICLE_THRUST_MAX = 0.0f;

	private float ROTATION_SPEED_PITCH = 1.0f;
	private float ROTATION_SPEED_YAW = 0.2f;
	private float ROTATION_SPEED_ROLL = 1.5f;

	private Mesh modelMesh;
	private Transformf transform;
	private Vector3f forward;
	private Vector3f upward;

	private Vector3f velocity;
	private float speed;

	private boolean KEY_NUMPAD0;

	public Fighter(Vector3f position) {
		this.modelMesh = MeshLoader.loadMesh("./res/models/fighters/f14d.obj");
		this.transform = new Transformf();
		transform.setTranslation(position);
		this.forward = Vector3f.Forward();
		this.upward = Vector3f.Up();

		this.velocity = new Vector3f();
	}

	public void handleInput(float mouseDX, float mouseDY) {
		if (Keyboard.isKeyDown(Input.AIRCRAFT_THROTTLE_INCREASE))
			speed = MathHelper.clamp(speed + 0.01f, 0.0f, 10.0f);
		if (Keyboard.isKeyDown(Input.AIRCRAFT_THROTTLE_DECREASE))
			speed = MathHelper.clamp(speed - 0.01f, 0.0f, 10.0f);

		float drx = mouseDY * -0.2f;
		float dry = 0.0f;
		float drz = mouseDX * 0.2f;

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

		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0) && !KEY_NUMPAD0) {
			// transform.setRotation(Vector3f.Zero());
			transform.setTranslation(Vector3f.Zero());
			forward = Vector3f.Forward();
			upward = Vector3f.Up();
		}

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

		velocity.set(Vector3f.scale(forward, -speed));
	}

	public void update(float mouseDX, float mouseDY) {
		handleInput(mouseDX, mouseDY);

		float rho = 1.225f;

		float dragForce;

		transform.setTranslation(transform.getTranslation().add(velocity));

		KEY_NUMPAD0 = Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0);
	}

	public void render(SimpleShader shader) {
		Matrix4f translationMatrix = new Matrix4f().initializeTranslation(transform.getTranslation().getX(), transform.getTranslation().getY(), transform.getTranslation().getZ());
		Matrix4f rotationMatrix = new Matrix4f().initializeRotation(forward, upward);
		Matrix4f scaleMatrix = new Matrix4f().initializeScale(transform.getScale().getX(), transform.getScale().getY(), transform.getScale().getZ());
		Matrix4f transformationMatrix = translationMatrix.multiply(rotationMatrix.multiply(scaleMatrix));

		// GameDisplay.addStringToBuffer(rotationMatrix.toString());

		shader.updateUniforms(transform.createProjectedTransformation(transformationMatrix), new Color3f(1.0f, 1.0f, 1.0f));
		modelMesh.render();

		// drawAxes(10, false);
	}

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
