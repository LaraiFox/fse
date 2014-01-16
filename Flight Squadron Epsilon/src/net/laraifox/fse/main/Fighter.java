package net.laraifox.fse.main;

import net.laraifox.lib.graphics.Color3f;
import net.laraifox.lib.graphics.Mesh;
import net.laraifox.lib.graphics.MeshLoader;
import net.laraifox.lib.graphics.Transformf;
import net.laraifox.lib.math.Vector3f;

import org.lwjgl.input.Keyboard;

public class Fighter {
	private float MAX_FORWARD_THRUST_POWER = 0.0f;
	private float MIX_FORWARD_THRUST_POWER = 0.0f;
	private float ROTATION_SPEED = 2.0f;

	private Mesh modelMesh;
	private Transformf transform;
	private Vector3f forward;
	private Vector3f upward;

	private Vector3f thrustVelocity;
	private float forwardThrustLevel;
	private float upwardThrustLevel;

	private boolean KEY_NUMPAD0;
	private boolean KEY_NUMPAD8;
	private boolean KEY_NUMPAD5;
	private boolean KEY_NUMPAD4;
	private boolean KEY_NUMPAD6;
	private boolean KEY_NUMPAD9;
	private boolean KEY_NUMPAD7;

	public Fighter(Vector3f position) {
		this.modelMesh = MeshLoader.loadMesh("./res/models/fighters/f14d.obj");
		this.transform = new Transformf();
		transform.setTranslation(position);
		transform.setRotation(0, 0, 0);

		this.forward = Vector3f.Forward();
		this.upward = Vector3f.Up();
	}

	public void handleInput() {
		// if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
		// forwardThrustLevel = MathHelper.clamp(forwardThrustLevel + 0.003f, 0.0f, 1.0f);
		// }
		// if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
		// forwardThrustLevel = MathHelper.clamp(forwardThrustLevel - 0.003f, 0.0f, 1.0f);
		// }
		// if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
		// upwardThrustLevel = MathHelper.clamp(upwardThrustLevel + 0.003f, -1.0f, 1.0f);
		// }
		// if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
		// upwardThrustLevel = MathHelper.clamp(upwardThrustLevel - 0.003f, -1.0f, 1.0f);
		// }
		// // if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
		// // Vector3f xAxis = Vector3f.cross(upward, forward).normalize();
		// // xAxis.rotate(0.2f, upward).normalize();
		// // forward = Vector3f.cross(xAxis, upward).normalize();
		// // }
		// // if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
		// // Vector3f xAxis = Vector3f.cross(upward, forward).normalize();
		// // xAxis.rotate(-0.2f, upward).normalize();
		// // forward = Vector3f.cross(xAxis, upward).normalize();
		// // }
		//
		// Vector3f xAxis = Vector3f.cross(upward, forward).normalize();
		// upward.rotate(mouseDX * 0.2f, forward).normalize();
		// forward = Vector3f.cross(xAxis, upward).normalize();
		//
		// xAxis = Vector3f.cross(upward, forward).normalize();
		// forward.rotate(mouseDY * 0.2f, xAxis).normalize();
		// upward = Vector3f.cross(forward, xAxis).normalize();
		//
		//
		// Matrix4f rotation = new Matrix4f().initializeRotation(45, 0, 0);
		// Matrix4f lookAt = new Matrix4f().initializeRotation(//
		// new Vector3f(0, 0, 1).rotate(45, new Vector3f(1, 0, 0)),
		// new Vector3f(0, 1, 0).rotate(45, new Vector3f(1, 0, 0))
		// // new Vector3f(0, (float) Math.sin(45), (float) Math.cos(45)),
		// // new Vector3f(0, (float) Math.cos(45), (float)-Math.sin(45))
		// );
		//
		//
		// System.out.println(rotation.toString() + "\n" + lookAt.toString() + "\n\n");
		//
		// //System.out.println(forward.toString(2) + "  -  " + upward.toString(2) + "  -  " + xAxis.toString(2));

		float rotationSpeed = 1.0f;

		if (true || Keyboard.isKeyDown(Keyboard.KEY_I) || Keyboard.isKeyDown(Keyboard.KEY_K) || Keyboard.isKeyDown(Keyboard.KEY_J) || Keyboard.isKeyDown(Keyboard.KEY_L) || Keyboard.isKeyDown(Keyboard.KEY_U)
				|| Keyboard.isKeyDown(Keyboard.KEY_O)) {
			float drx = 0.0f;
			float dry = 0.0f;
			float drz = 0.0f;

			if (Keyboard.isKeyDown(Keyboard.KEY_I))
				drx += rotationSpeed;
			if (Keyboard.isKeyDown(Keyboard.KEY_K))
				drx -= rotationSpeed;
			if (Keyboard.isKeyDown(Keyboard.KEY_J))
				dry += rotationSpeed;
			if (Keyboard.isKeyDown(Keyboard.KEY_L))
				dry -= rotationSpeed;
			if (Keyboard.isKeyDown(Keyboard.KEY_U))
				drz += rotationSpeed;
			if (Keyboard.isKeyDown(Keyboard.KEY_O))
				drz -= rotationSpeed;

			if (Keyboard.isKeyDown(Keyboard.KEY_RETURN) && !KEY_NUMPAD0)
				transform.setRotation(Vector3f.Zero());
			if (Keyboard.isKeyDown(Keyboard.KEY_LBRACKET) && !KEY_NUMPAD8)
				drx -= 90;
			if (Keyboard.isKeyDown(Keyboard.KEY_APOSTROPHE) && !KEY_NUMPAD5)
				drx += 90;
			if (Keyboard.isKeyDown(Keyboard.KEY_SEMICOLON) && !KEY_NUMPAD4)
				dry += 90;
			if (Keyboard.isKeyDown(Keyboard.KEY_BACKSLASH) && !KEY_NUMPAD6)
				dry -= 90;
			if (Keyboard.isKeyDown(Keyboard.KEY_RBRACKET) && !KEY_NUMPAD9)
				drz += 90;
			if (Keyboard.isKeyDown(Keyboard.KEY_P) && !KEY_NUMPAD7)
				drz -= 90;

			float rx = transform.getRotation().getX();
			float ry = transform.getRotation().getY();
			float rz = transform.getRotation().getZ();

			Matrix3f rotationMatrix = Matrix3f.initializeRotationMatrix(rx, ry, rz);
			Matrix3f deltaMatrix = Matrix3f.initializeRotationMatrix(drx, dry, drz);

			rx = (float) Math.toRadians(rx);
			ry = (float) Math.toRadians(ry);
			rz = (float) Math.toRadians(rz);

			Matrix3f rxMatrix = new Matrix3f();
			Matrix3f ryMatrix = new Matrix3f();
			Matrix3f rzMatrix = new Matrix3f();

			rxMatrix.setDataAt(0, 0, 1);
			rxMatrix.setDataAt(0, 1, 0);
			rxMatrix.setDataAt(0, 2, 0);
			rxMatrix.setDataAt(1, 0, 0);
			rxMatrix.setDataAt(1, 1, (float) Math.cos(rx));
			rxMatrix.setDataAt(1, 2, (float) -Math.sin(rx));
			rxMatrix.setDataAt(2, 0, 0);
			rxMatrix.setDataAt(2, 1, (float) Math.sin(rx));
			rxMatrix.setDataAt(2, 2, (float) Math.cos(rx));

			ryMatrix.setDataAt(0, 0, (float) Math.cos(ry));
			ryMatrix.setDataAt(0, 1, 0);
			ryMatrix.setDataAt(0, 2, (float) -Math.sin(ry));
			ryMatrix.setDataAt(1, 0, 0);
			ryMatrix.setDataAt(1, 1, 1);
			ryMatrix.setDataAt(1, 2, 0);
			ryMatrix.setDataAt(2, 0, (float) Math.sin(ry));
			ryMatrix.setDataAt(2, 1, 0);
			ryMatrix.setDataAt(2, 2, (float) Math.cos(ry));

			rzMatrix.setDataAt(0, 0, (float) Math.cos(rz));
			rzMatrix.setDataAt(0, 1, (float) -Math.sin(rz));
			rzMatrix.setDataAt(0, 2, 0);
			rzMatrix.setDataAt(1, 0, (float) Math.sin(rz));
			rzMatrix.setDataAt(1, 1, (float) Math.cos(rz));
			rzMatrix.setDataAt(1, 2, 0);
			rzMatrix.setDataAt(2, 0, 0);
			rzMatrix.setDataAt(2, 1, 0);
			rzMatrix.setDataAt(2, 2, 1);

			Matrix3f xyPlane = ryMatrix.multiply(rxMatrix);
			Matrix3f xzPlane = rzMatrix.multiply(rxMatrix);
			Matrix3f yzPlane = rzMatrix.multiply(ryMatrix);

			GameDisplay.addStringToBuffer(rotationMatrix.toString());
			GameDisplay.addStringToBuffer(xyPlane.toString());
			GameDisplay.addStringToBuffer(xzPlane.toString());
			GameDisplay.addStringToBuffer(yzPlane.toString());

			// float x = drx * yzPlane.getDataAt(0, 0) + drx * yzPlane.getDataAt(0, 1) + drx * yzPlane.getDataAt(0, 2);
			// float y = dry * xzPlane.getDataAt(0, 0) + dry * xzPlane.getDataAt(0, 1) + dry * xzPlane.getDataAt(0, 2);
			// float z = drz * xyPlane.getDataAt(0, 0) + drz * xyPlane.getDataAt(0, 1) + drz * xyPlane.getDataAt(0, 2);

			Matrix3f rotationTransformationMatrix = rotationMatrix.multiply(deltaMatrix);

			Vector3f result = rotationTransformationMatrix.multiply(new Vector3f(drx, dry, drz)); // new Vector3f(x, y, z);
			result.add(transform.getRotation());
			transform.setRotation(result);
		}
	}

	public void update() {
		handleInput();

		float rho = 1.225f;

		float dragForce;

		KEY_NUMPAD0 = Keyboard.isKeyDown(Keyboard.KEY_RETURN);
		KEY_NUMPAD4 = Keyboard.isKeyDown(Keyboard.KEY_SEMICOLON);
		KEY_NUMPAD5 = Keyboard.isKeyDown(Keyboard.KEY_APOSTROPHE);
		KEY_NUMPAD6 = Keyboard.isKeyDown(Keyboard.KEY_BACKSLASH);
		KEY_NUMPAD7 = Keyboard.isKeyDown(Keyboard.KEY_P);
		KEY_NUMPAD8 = Keyboard.isKeyDown(Keyboard.KEY_LBRACKET);
		KEY_NUMPAD9 = Keyboard.isKeyDown(Keyboard.KEY_RBRACKET);
	}

	public void render(SimpleShader shader) {
		shader.updateUniforms(transform.getProjectedTransformation(), new Color3f(1.0f, 1.0f, 1.0f));
		modelMesh.render();
	}
}
