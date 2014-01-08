package net.laraifox.fse.main;

import net.laraifox.lib.display.OpenGLDisplay;
import net.laraifox.lib.graphics.Camera;
import net.laraifox.lib.graphics.Color3f;
import net.laraifox.lib.graphics.Mesh;
import net.laraifox.lib.graphics.MeshLoader;
import net.laraifox.lib.graphics.Transformf;
import net.laraifox.lib.math.Matrix4f;
import net.laraifox.lib.math.Vector3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class GameDisplay extends OpenGLDisplay {
	private Camera camera;
	private Transformf transform;

	private Mesh test;

	public GameDisplay(float width, float height) {
		super("Flight Squadron Epsilon", width, height);
	}

	protected void initializeOpenGL() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(70f, width / height, 0.1f, 1000f);
		// GL11.glOrtho(-1, 1, -1, 1, 0.1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glViewport(0, 0, (int) width, (int) height);
		GL11.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glViewport(0, 0, (int) width, (int) height);
	}

	protected void initializeResources() {

	}

	protected void initializeVariables() {
		this.camera = new Camera();
		camera.setPosition(new Vector3f(-5, 0, 0));
		this.transform = new Transformf();

		this.test = MeshLoader.loadMesh("./res/models/unit_cube.obj");

		Transformf.setProjection(70.0f, (float) getWidth(), (float) getHeight(), 0.1f, 1000.0f);
		Transformf.setCamera(camera);

		// try {
		// Keyboard.create();
		// Mouse.create();
		// } catch (LWJGLException e) {
		// e.printStackTrace();
		// }

		// Mouse.setClipMouseCoordinatesToWindow(!Mouse.isClipMouseCoordinatesToWindow());
		// Mouse.setGrabbed(!Mouse.isGrabbed());
	}

	protected void tick() {

	}

	protected void update(double delta) {
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			camera.move(camera.getRight(), 0.1f);
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
			camera.move(camera.getLeft(), 0.1f);
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
			camera.move(camera.getForward(), -0.1f);
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
			camera.move(camera.getForward(), 0.1f);

		if (Keyboard.isKeyDown(Keyboard.KEY_UP))
			camera.rotateX(-1);
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
			camera.rotateX(1);
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
			camera.rotateY(-1);
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
			camera.rotateY(1);

		if (Mouse.isGrabbed()) {
			float dx = (width / 2.0f - Mouse.getX()) * 0.6f;
			float dy = (Mouse.getY() - height / 2.0f) * 0.6f;

			Mouse.setCursorPosition((int) (width / 2.0f), (int) (height / 2.0f));

			camera.rotateY(dx);
			camera.rotateX(dy);

			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				Mouse.setGrabbed(false);
			}
		} else if (Mouse.isButtonDown(0)) {
			Mouse.setCursorPosition((int) (width / 2.0f), (int) (height / 2.0f));
			Mouse.setGrabbed(true);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			camera = new Camera();
			camera.setPosition(new Vector3f(0, 0, -1));
			Transformf.setCamera(camera);
		}
	}

	protected void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glLoadIdentity();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		Vector3f position = camera.getPosition();
		Vector3f forward = Vector3f.add(position, camera.getForward());
		Vector3f upward = Vector3f.add(position, camera.getUpward());

		GLU.gluLookAt(position.getX(), position.getY(), position.getZ(), forward.getX(), forward.getY(), forward.getZ(), upward.getX(), upward.getY(), upward.getZ());

		GL11.glPushMatrix();
		GL11.glTranslatef(0, 0, 10);
		GL11.glColor3f(1, 0, 0);
		test.render();
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(0, 0, -10);
		GL11.glColor3f(0, 1, 1);
		test.render();
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(0, 10, 0);
		GL11.glColor3f(0, 1, 0);
		test.render();
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(0, -10, 0);
		GL11.glColor3f(1, 0, 1);
		test.render();
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(10, 0, 0);
		GL11.glColor3f(0, 0, 1);
		test.render();
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(-10, 0, 0);
		GL11.glColor3f(1, 1, 0);
		test.render();
		GL11.glPopMatrix();
	}
}
