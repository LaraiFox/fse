#version 120

attribute vec3 positionAttrib;

uniform mat4 objTransform;
uniform mat4 transform;

varying float yPos;

void main() {
	vec4 pos = objTransform * vec4(positionAttrib, 1.0);
	yPos = pos.y;
	
	gl_FrontColor = gl_Color;
    gl_Position = transform * vec4(positionAttrib, 1.0);
}