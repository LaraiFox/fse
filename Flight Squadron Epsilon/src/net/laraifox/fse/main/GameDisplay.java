package net.laraifox.fse.main;

import net.laraifox.lib.display.OpenGLDisplay;
import net.laraifox.lib.graphics.Camera;
import net.laraifox.lib.graphics.Color3f;
import net.laraifox.lib.graphics.FirstPersonCamera;
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
	private SimpleShader shader;
	private Fighter testFighter;

	public GameDisplay(int width, int height) {
		super("Flight Squadron Epsilon", width, height);
	}

	protected void initializeOpenGL() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(-1, 1, -1, 1, 0.1, 1000f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glViewport(0, 0, getWidth(), getHeight());
		GL11.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glViewport(0, 0, getWidth(), getHeight());
	}

	protected void initializeResources() {

	}

	protected void initializeVariables() {
		this.camera = new FirstPersonCamera();
		camera.setPosition(new Vector3f(0, 0, -1));
		this.shader = new SimpleShader();

		this.testFighter = new Fighter(Vector3f.Zero());

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

	private void handleInput() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			camera.move(camera.getForward(), 0.2f);
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
			camera.move(camera.getForward(), -0.2f);
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
			camera.move(camera.getLeft(), 0.2f);
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
			camera.move(camera.getRight(), 0.2f);
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			camera.move(Vector3f.Up(), 0.2f);
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			camera.move(Vector3f.Up(), -0.2f);
		if (Mouse.isGrabbed()) {
			// float mouseDX = Mouse.getX() - (getWidth() / 2.0f);
			// float mouseDY = (getHeight() / 2.0f) - Mouse.getY();
			//
			// Mouse.setCursorPosition((int) (getWidth() / 2.0f), (int) (getHeight() / 2.0f));
			//
			// camera.rotateX(mouseDY * 0.5f);
			// camera.rotateY(mouseDX * 0.5f);

			if (Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
				Mouse.setGrabbed(false);
			}
		} else if (Mouse.isInsideWindow() && Mouse.isButtonDown(0)) {
			Mouse.setCursorPosition((int) (getWidth() / 2.0f), (int) (getHeight() / 2.0f));
			Mouse.setGrabbed(true);
		} else {
			if (Keyboard.isKeyDown(Keyboard.KEY_UP))
				camera.rotateX(-1);
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
				camera.rotateX(1);
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
				camera.rotateY(-1);
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
				camera.rotateY(1);
		}
	}

	protected void update(double delta) {
		float mouseDX = 0;
		float mouseDY = 0;
		if (Mouse.isGrabbed()) {
			mouseDX = Mouse.getX() - (getWidth() / 2.0f);
			mouseDY = (getHeight() / 2.0f) - Mouse.getY();
			Mouse.setCursorPosition((int) (getWidth() / 2.0f), (int) (getHeight() / 2.0f));
		}
		handleInput();

		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			camera = new FirstPersonCamera();
			camera.setPosition(new Vector3f(0, 0, -1));
			Transformf.setCamera(camera);
		}

		testFighter.update(mouseDX, mouseDY);
	}

	protected void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glLoadIdentity();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		shader.bindShader();

		testFighter.render(shader);
	}
}
