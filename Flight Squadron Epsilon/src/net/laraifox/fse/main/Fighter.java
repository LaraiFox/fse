package net.laraifox.fse.main;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.laraifox.lib.graphics.Color3f;
import net.laraifox.lib.graphics.Mesh;
import net.laraifox.lib.graphics.MeshLoader;
import net.laraifox.lib.graphics.Transformf;
import net.laraifox.lib.math.MathHelper;
import net.laraifox.lib.math.Matrix4f;
import net.laraifox.lib.math.Vector3f;

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

	public Fighter(Vector3f position) {
		this.modelMesh = MeshLoader.loadMesh("./res/models/fighters/f14d.obj");
		this.transform = new Transformf();
		transform.setTranslation(position);
		transform.setRotation(0, 0, 0);

		this.forward = Vector3f.Forward();
		this.upward = Vector3f.Up();
	}

	public void handleInput(float mouseDX, float mouseDY) {
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

		if (mouseDX != 0.0f || mouseDY != 0.0f || Keyboard.isKeyDown(Keyboard.KEY_I) || Keyboard.isKeyDown(Keyboard.KEY_K)
				|| Keyboard.isKeyDown(Keyboard.KEY_J) || Keyboard.isKeyDown(Keyboard.KEY_L) || Keyboard.isKeyDown(Keyboard.KEY_U)
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

			drx += mouseDY;
			drz += mouseDX;

			float rx = transform.getRotation().getX();
			float ry = transform.getRotation().getY();
			float rz = transform.getRotation().getZ();

			Matrix3f rotationMatrix = Matrix3f.initializeRotationMatrix(rx, ry, rz);

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

			float x = drx * yzPlane.getDataAt(0, 0) + drx * yzPlane.getDataAt(0, 1) + drx * yzPlane.getDataAt(0, 2);
			float y = dry * xzPlane.getDataAt(0, 0) + dry * xzPlane.getDataAt(0, 1) + dry * xzPlane.getDataAt(0, 2);
			float z = drz * xyPlane.getDataAt(0, 0) + drz * xyPlane.getDataAt(0, 1) + drz * xyPlane.getDataAt(0, 2);

			Vector3f result = new Vector3f(x, y, z); // rotationMatrix.multiply(new Vector3f(drx, dry, drz));
			result.add(transform.getRotation());
			transform.setRotation(result);
		}
	}

	public void update(float mouseDX, float mouseDY) {
		handleInput(mouseDX, mouseDY);

		float rho = 1.225f;

		float dragForce;
	}

	public void render(SimpleShader shader) {
		shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), new Color3f(0xFFFFFF));
		modelMesh.render();
	}
}
