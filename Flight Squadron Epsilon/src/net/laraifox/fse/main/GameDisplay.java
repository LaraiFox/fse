package net.laraifox.fse.main;

import java.awt.Font;
import java.util.ArrayList;

import net.laraifox.lib.display.OpenGLDisplay;
import net.laraifox.lib.graphics.Camera;
import net.laraifox.lib.graphics.Color3f;
import net.laraifox.lib.graphics.FirstPersonCamera;
import net.laraifox.lib.graphics.FlightCamera;
import net.laraifox.lib.graphics.Mesh;
import net.laraifox.lib.graphics.MeshLoader;
import net.laraifox.lib.graphics.Transformf;
import net.laraifox.lib.math.Vector2f;
import net.laraifox.lib.math.Vector3f;
import net.laraifox.lib.text.VectorFont;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GameDisplay extends OpenGLDisplay {
	private static final float MOUSE_LOOK_SENSITIVITY = 0.3f;

	private static ArrayList<String> stringBuffer = new ArrayList<String>();

	private Mesh UNIT_CUBE;

	private VectorFont font;
	private Vector2f fontScale;

	private Camera camera;
	private Transformf transform;
	private Transformf terrainTransform;
	private HeightShader heightShader;
	private SimpleShader simpleShader;
	private TextureShader textureShader;

	private Mesh terrainMesh;
	private Fighter testFighter;

	public GameDisplay(float width, float height) {
		super("3D Flight Game", width, height);
	}

	protected void initializeOpenGL() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		// GLU.gluPerspective(70f, width / height, 0.1f, 1000f);
		GL11.glOrtho(-1, 1, -1, 1, 0.1, 1000f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glViewport(0, 0, (int) width, (int) height);

		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glViewport(0, 0, (int) width, (int) height);
	}

	protected void initializeResources() {

	}

	protected void initializeVariables() {
		this.UNIT_CUBE = MeshLoader.loadMesh("./res/models/unit_cube.obj");

		this.font = new VectorFont(new Font("Consolas", Font.PLAIN, 13), true);
		this.fontScale = new Vector2f(2.0f / width, 2.0f / height);

		this.camera = new FirstPersonCamera();
		camera.setPosition(new Vector3f(0, 2, 20));
		camera.setForward(Vector3f.Forward().negate());
		this.transform = new Transformf();
		this.terrainTransform = new Transformf();
		terrainTransform.setTranslation(0, -100, 0);
		terrainTransform.setScale(Vector3f.One().scale(50.0f));
		
		this.heightShader = new HeightShader();
		this.simpleShader = new SimpleShader();
		this.textureShader = new TextureShader();

		this.terrainMesh = MeshLoader.loadMesh("./res/models/terrain/test_terrain.obj");
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

	private void handleInput(float mouseDX, float mouseDY) {
		if (Keyboard.isKeyDown(Keyboard.KEY_I))
			camera.move(camera.getForward(), 0.2f);
		if (Keyboard.isKeyDown(Keyboard.KEY_K))
			camera.move(camera.getForward(), -0.2f);
		if (Keyboard.isKeyDown(Keyboard.KEY_J))
			camera.move(camera.getLeft(), 0.2f);
		if (Keyboard.isKeyDown(Keyboard.KEY_L))
			camera.move(camera.getRight(), 0.2f);
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			camera.move(Vector3f.Up(), 0.2f);
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
			camera.move(Vector3f.Up(), -0.2f);

		if (Mouse.isGrabbed()) {
			camera.rotateX(mouseDY * MOUSE_LOOK_SENSITIVITY);
			camera.rotateY(mouseDX * MOUSE_LOOK_SENSITIVITY);

			if (Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
				Mouse.setGrabbed(false);
			}
		} else if (Mouse.isInsideWindow() && Mouse.isButtonDown(0)) {
			Mouse.setCursorPosition((int) (getWidth() / 2.0f), (int) (getHeight() / 2.0f));
			Mouse.setGrabbed(true);
		}
	}

	protected void update(double delta) {
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			super.stop();
		}

		float mouseDX = 0;
		float mouseDY = 0;
		if (Mouse.isGrabbed()) {
			mouseDX = Mouse.getX() - (getWidth() / 2.0f);
			mouseDY = (getHeight() / 2.0f) - Mouse.getY();
			Mouse.setCursorPosition((int) (getWidth() / 2.0f), (int) (getHeight() / 2.0f));
		}
		handleInput(mouseDX, mouseDY);

		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			camera.setPosition(new Vector3f(0, 2, 20));
			camera.setForward(Vector3f.Forward().negate());
		}

		testFighter.update(mouseDX, mouseDY);
		camera.setPosition(testFighter.getCameraPosition());
		camera.setForward(testFighter.getForward());
		camera.setUpward(testFighter.getUpward());
	}

	protected void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glLoadIdentity();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		simpleShader.bindShader();
		transform.setScale(Vector3f.One());
		simpleShader.updateUniforms(transform.getProjectedTransformation(), new Color3f(1.0f, 1.0f, 1.0f));

		drawWorldAxes();

		GL11.glColor3f(1, 1, 1);
		testFighter.render(simpleShader);

		heightShader.bindShader();
		heightShader.updateUniforms(terrainTransform.getTransformation(), terrainTransform.getProjectedTransformation(), new Color3f(1.0f, 1.0f, 1.0f));

		terrainMesh.render();

		drawOnScreenData();
	}

	private void drawOnScreenData() {
		textureShader.bindShader();
		textureShader.updateUniforms(transform.getTransformation(), new Color3f(1.0f, 1.0f, 1.0f));

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glColor3f(1, 1, 0);

		font.drawString(0.99f, 0.96f, "FPS: " + getCurrentFPS() + ", (Updates: " + getCurrentUPS() + ")", fontScale.getX(), fontScale.getY(), VectorFont.ALIGN_RIGHT);
		font.drawString(0.99f, 0.92f, "Camera Position: " + camera.getPosition(), fontScale.getX(), fontScale.getY(), VectorFont.ALIGN_RIGHT);

		for (int i = 0; i < stringBuffer.size(); i++) {
			float x = (10 / width * 2.0f) - 1.0f;
			float y = 1.0f - ((25 + i * 90) / height * 2.0f);
			font.drawString(x, y, stringBuffer.get(i), fontScale.getX(), fontScale.getY());
		}

		GameDisplay.stringBuffer.clear();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	private void drawWorldAxes() {
		GL11.glLineWidth(3.0f);
		GL11.glBegin(GL11.GL_LINES);

		// X axis marker
		GL11.glColor3f(1, 0, 0);
		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(100, 0, 0);

		// Y axis marker
		GL11.glColor3f(0, 1, 0);
		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(0, 100, 0);

		// Z axis marker
		GL11.glColor3f(0, 0, 1);
		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(0, 0, 100);

		GL11.glEnd();
		GL11.glLineWidth(0.5f);
		GL11.glBegin(GL11.GL_LINES);

		// -X axis marker
		GL11.glColor3f(1, 0, 0);
		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(-100, 0, 0);

		// -Y axis marker
		GL11.glColor3f(0, 1, 0);
		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(0, -100, 0);

		// -Z axis marker
		GL11.glColor3f(0, 0, 1);
		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(0, 0, -100);

		GL11.glEnd();
		GL11.glLineWidth(1.0f);
	}

	public static void addStringToBuffer(String string) {
		GameDisplay.stringBuffer.add(string);
	}
}
