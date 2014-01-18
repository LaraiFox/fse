package net.laraifox.fse.main;

import net.laraifox.lib.graphics.Shader;
import net.laraifox.lib.graphics.ShaderLoader;
import net.laraifox.lib.math.Matrix4f;
import net.laraifox.lib.math.Vector3f;

public class SimpleShader extends Shader {
	public SimpleShader() {
		super();

		addVertexShader(ShaderLoader.loadShader("./res/shaders/simple vertex 120.vs"));
		addFragmentShader(ShaderLoader.loadShader("./res/shaders/simple fragment 120.fs"));
		compileShader();

		addUniform("transform");
		addUniform("color");
	}

	public void updateUniforms(Matrix4f transformationMatrix, Vector3f color) {
		setUniform("transform", transformationMatrix);
		setUniform("color", color);
	}

	public void bindShader() {
		super.bind();
	}
}
