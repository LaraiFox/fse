package net.laraifox.fse.main;

import net.laraifox.lib.graphics.Color3f;
import net.laraifox.lib.graphics.Shader;
import net.laraifox.lib.graphics.ShaderLoader;
import net.laraifox.lib.math.Matrix4f;
import net.laraifox.lib.util.VectorUtil;

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

	public void updateUniforms(Matrix4f transformationMatrix, Matrix4f projectedTransformationMatrix, Color3f color) {
		setUniform("objTransform", transformationMatrix);
		setUniform("transform", projectedTransformationMatrix);
		setUniform("color", VectorUtil.toVector3f(color));
	}

	public void bindShader() {
		super.bind();
	}
}
