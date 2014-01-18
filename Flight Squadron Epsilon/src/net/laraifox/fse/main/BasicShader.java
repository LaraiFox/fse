package net.laraifox.fse.main;

import net.laraifox.lib.graphics.Shader;
import net.laraifox.lib.graphics.ShaderLoader;
import net.laraifox.lib.math.Matrix4f;
import net.laraifox.lib.math.Vector3f;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class BasicShader extends Shader {
	public BasicShader() {
		super();
		System.out.println("Latest GLSL shader version supported by this system: " + GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));

		addVertexShader(ShaderLoader.loadShader("./res/shaders/basic vertex 120.vs"));
		addFragmentShader(ShaderLoader.loadShader("./res/shaders/basic fragment 120.fs"));
		compileShader();

		addUniform("transform");
		addUniform("color");
	}

	public void updateUniforms(Matrix4f translationMatrix, Matrix4f projectionMatrix, Vector3f color) {
		setUniform("transform", projectionMatrix);
		setUniform("color", color);
	}
}
