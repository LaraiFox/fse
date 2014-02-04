package net.laraifox.fse.main;

import java.awt.Font;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import net.laraifox.lib.display.OpenGLDisplay;
import net.laraifox.lib.graphics.Camera;
import net.laraifox.lib.graphics.EulerTransform;
import net.laraifox.lib.graphics.FirstPersonCamera;
import net.laraifox.lib.graphics.Mesh;
import net.laraifox.lib.graphics.MeshLoader;
import net.laraifox.lib.graphics.Transform;
import net.laraifox.lib.math.Vector2f;
import net.laraifox.lib.math.Vector3f;
import net.laraifox.lib.math.Vector4f;
import net.laraifox.lib.text.VectorFont;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GameDisplay extends OpenGLDisplay {
	private static final float MOUSE_LOOK_SENSITIVITY = 0.3f;

	private static ArrayList<String> stringBuffer = new ArrayList<String>();

	private VectorFont font;
	private Vector2f fontScale;
	private Vector2f fontAlignment;

	private Camera camera;
	private EulerTransform transform;
	private EulerTransform terrainTransform;
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
		GL11.glOrtho(-1, 1, -1, 1, 0.1, 10000f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glViewport(0, 0, (int) width, (int) height);

		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_FOG);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, (FloatBuffer) (new Vector4f(0.5f, 0.5f, 0.5f, 0.0f).toFloatBuffer().flip()));

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		{
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, (FloatBuffer) (new Vector4f(0.1745f, 0.01175f, 0.01175f, 1.0f).toFloatBuffer().flip()));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, (FloatBuffer) (new Vector4f(0.61424f, 0.04136f, 0.04136f, 1.0f).toFloatBuffer().flip()));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, (FloatBuffer) (new Vector4f(0.727811f, 0.62959f, 0.62959f, 1.0f).toFloatBuffer().flip()));
			GL11.glMateriali(GL11.GL_FRONT, GL11.GL_SHININESS, (int) (0.6 * 128));
		}

		GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
		GL11.glFog(GL11.GL_FOG_COLOR, (FloatBuffer) (new Vector4f(0.0f, 1.0f, 1.0f, 1.0f).toFloatBuffer().flip()));
		GL11.glFogf(GL11.GL_FOG_DENSITY, 0.35f);
		GL11.glHint(GL11.GL_FOG_HINT, GL11.GL_DONT_CARE);
		GL11.glFogf(GL11.GL_FOG_START, 1000.0f);
		GL11.glFogf(GL11.GL_FOG_END, 2000.0f);

		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);

		GL11.glViewport(0, 0, (int) width, (int) height);
	}

	protected void initializeResources() {

	}

	protected void initializeVariables() {
		this.font = new VectorFont(new Font("Courier", Font.PLAIN, 13), true);
		this.fontScale = new Vector2f(2.0f / width, 2.0f / height);
		this.fontAlignment = new Vector2f(width * 0.5f, height * 0.5f);

		this.camera = new FirstPersonCamera();
		camera.setPosition(new Vector3f(0, 2, 20));
		camera.setForward(Vector3f.Forward().negate());
		this.transform = new EulerTransform();
		this.terrainTransform = new EulerTransform();
		terrainTransform.setTranslation(0, -1000, 0);
		terrainTransform.setScale(Vector3f.One().scale(1000.0f));

		this.heightShader = new HeightShader();
		this.simpleShader = new SimpleShader();
		this.textureShader = new TextureShader();

		this.terrainMesh = MeshLoader.loadMesh("./res/models/terrain/test_terrain.obj");
		this.testFighter = new Fighter(Vector3f.Zero());

		Transform.setProjection(70.0f, (float) getWidth(), (float) getHeight(), 0.1f, 10000.0f);
		Transform.setCamera(camera);

		// try {
		// Keyboard.create();
		// Mouse.create();
		// } catch (LWJGLException e) {
		// e.printStackTrace();
		// }

		// Mouse.setClipMouseCoordinatesToWindow(!Mouse.isClipMouseCoordinatesToWindow());
		// Mouse.setGrabbed(!Mouse.isGrabbed());
	}

	protected void cleanUp() {

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
			testFighter = new Fighter(Vector3f.Zero());
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
		simpleShader.updateUniforms(transform.getProjectedTransformation(), new Vector3f(1.0f, 1.0f, 1.0f));

		drawWorldAxes();

		GL11.glColor3f(1, 1, 1);
		testFighter.render(simpleShader);

		heightShader.bindShader();
		heightShader.updateUniforms(terrainTransform.getTransformation(), terrainTransform.getProjectedTransformation(), new Vector3f(1.0f, 1.0f, 1.0f));

		terrainMesh.render();

		drawOnScreenData();
	}

	private void drawOnScreenData() {
		textureShader.bindShader();
		textureShader.updateUniforms(transform.getTransformation(), new Vector3f(1.0f, 1.0f, 1.0f));

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glColor3f(1, 1, 0);

		drawString(width - 10, 25, "FPS: " + getCurrentFPS() + ", (Updates: " + getCurrentUPS() + ")", VectorFont.ALIGN_RIGHT);
		drawString(width - 10, 45, "Camera Position: " + camera.getPosition().toString(1), VectorFont.ALIGN_RIGHT);

		for (int i = 0; i < stringBuffer.size(); i++) {
			drawString(10, (25 + i * 20), stringBuffer.get(i), VectorFont.ALIGN_LEFT);
		}

		GameDisplay.stringBuffer.clear();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	private void drawString(float x, float y, String string, int format) {
		float x_ = x / fontAlignment.getX() - 1.0f;
		float y_ = 1.0f - y / fontAlignment.getY();

		font.drawString(x_, y_, string, fontScale.getX(), fontScale.getY(), format);
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

		// simpleShader.updateUniforms(new Matrix4f().initializeRotation(Transformf.getCamera().getForward(),
		// Transformf.getCamera().getUpward()).multiply(transform.getTransformation()), new Color3f(1.0f, 1.0f, 1.0f));
		// GL11.glPushMatrix();
		// GL11.glTranslatef(0.9f, -0.9f, 0);
		// GL11.glBegin(GL11.GL_LINES);
		//
		// // X axis marker
		// GL11.glColor3f(1, 0, 0);
		// GL11.glVertex3f(0, 0, 0);
		// GL11.glVertex3f(0.05f, 0, 0);
		//
		// // Y axis marker
		// GL11.glColor3f(0, 1, 0);
		// GL11.glVertex3f(0, 0, 0);
		// GL11.glVertex3f(0, 0.05f, 0);
		//
		// // Z axis marker
		// GL11.glColor3f(0, 0, 1);
		// GL11.glVertex3f(0, 0, 0);
		// GL11.glVertex3f(0, 0, 0.05f);
		//
		// GL11.glEnd();
		// GL11.glPopMatrix();
	}

	public static void addStringToBuffer(String string) {
		GameDisplay.stringBuffer.add(string);
	}
}
