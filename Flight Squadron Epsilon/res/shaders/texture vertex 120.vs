#version 120

attribute vec3 positionAttrib;

uniform mat4 transform;

void main() {
	gl_FrontColor = gl_Color;
	gl_TexCoord[0] = gl_MultiTexCoord0;
    gl_Position = transform * vec4(positionAttrib, 1.0);
}