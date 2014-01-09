package net.laraifox.fse.main;

import net.laraifox.lib.graphics.Color3f;
import net.laraifox.lib.graphics.Shader;
import net.laraifox.lib.graphics.ShaderLoader;
import net.laraifox.lib.math.Matrix4f;
import net.laraifox.lib.util.VectorUtil;

public class SimpleShader extends Shader {
	public SimpleShader() {
		super();

		addVertexShader(ShaderLoader.loadShader("./res/shaders/simple vertex 120.vs"));
		addFragmentShader(ShaderLoader.loadShader("./res/shaders/simple fragment 120.fs"));
		compileShader();

		addUniform("transform");
		addUniform("color");
	}

	public void updateUniforms(Matrix4f transformationMatrix, Color3f color) {
		setUniform("transform", transformationMatrix);
		setUniform("color", VectorUtil.toVector3f(color));
	}

	public void bindShader() {
		super.bind();
	}
}
