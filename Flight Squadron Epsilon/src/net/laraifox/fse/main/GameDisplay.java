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
	private BasicShader shader;
	private Transformf transform;

	private Mesh test;

	public GameDisplay(int width, int height) {
		super("Flight Squadron Epsilon", width, height);
	}

	protected void initializeOpenGL() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		//GLU.gluPerspective(70f, (float) getWidth() / (float) getHeight(), 0.1f, 1000f);
		GL11.glOrtho(-1, 1, -1, 1, 0.1, 100);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glViewport(0, 0, getWidth(), getHeight());
		GL11.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glViewport(0, 0, getWidth(), getHeight());
	}

	protected void initializeResources() {

	}

	protected void initializeVariables() {
		this.camera = new Camera();
		camera.setPosition(new Vector3f(0, 0, -1));
		this.shader = new BasicShader();
		this.transform = new Transformf();
		transform.setScale(new Vector3f(1, 1, 1));

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

	float rx = 0, ry = 0, rz = 0;

	float temp = 0.0f;

	protected void update(double delta) {
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Mouse.setClipMouseCoordinatesToWindow(!Mouse.isClipMouseCoordinatesToWindow());
			Mouse.setGrabbed(!Mouse.isGrabbed());
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			camera = new Camera();
			camera.setPosition(new Vector3f(0, 0, -1));
			Transformf.setCamera(camera);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			camera.move(camera.getForward(), -1.0f);
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
			camera.move(camera.getForward(), 1.0f);
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
			camera.move(camera.getLeft(), 1.0f);
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
			camera.move(camera.getRight(), 1.0f);

		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			rx -= 1;
			camera.rotateX(-1);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			rx += 1;
			camera.rotateX(1);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			ry -= 1;
			camera.rotateY(-1);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			ry += 1;
			camera.rotateY(1);
		}

		float mouseDX = Mouse.getDX();
		float mouseDY = Mouse.getDY();

		// camera.rotateX(mouseDY);
		// camera.rotateY(mouseDX);

		temp += delta;

		float sinTemp = (float) Math.sin(temp);

		transform.setTranslation(0, 0, 2);
		// transform.setRotation(0, sinTemp * 180, 0);
	}

	protected void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glLoadIdentity();
		// GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		shader.bindShader();
		shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), new Color3f(1.0f, 1.0f, 1.0f));

		Matrix4f testMatrix = transform.getTransformation();
		System.out.println(testMatrix.toString());
		System.out.println("Camera == " + Transformf.getCamera().getPosition().toString(1));
		System.out.println("Orient == " + Transformf.getCamera().getForward().toString(1) + ", " + Transformf.getCamera().getUpward().toString(1) + "\n");

		test.render();
	}
}
