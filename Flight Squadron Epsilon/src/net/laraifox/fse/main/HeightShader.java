package net.laraifox.fse.main;

import net.laraifox.lib.graphics.Shader;
import net.laraifox.lib.graphics.ShaderLoader;
import net.laraifox.lib.math.Matrix4f;
import net.laraifox.lib.math.Vector3f;

public class HeightShader extends Shader {
	public HeightShader() {
		super();

		addVertexShader(ShaderLoader.loadShader("./res/shaders/height vertex.vs"));
		addFragmentShader(ShaderLoader.loadShader("./res/shaders/height fragment.fs"));
		compileShader();

		addUniform("objTransform");
		addUniform("transform");
		addUniform("color");
	}

	public void updateUniforms(Matrix4f transformationMatrix, Matrix4f projectedTransformationMatrix, Vector3f color) {
		setUniform("objTransform", transformationMatrix);
		setUniform("transform", projectedTransformationMatrix);
		setUniform("color", color);
	}

	public void bindShader() {
		super.bind();
	}
}
