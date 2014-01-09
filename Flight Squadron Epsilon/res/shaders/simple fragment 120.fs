#version 120

uniform vec3 color;

void main() {
	gl_FragColor = gl_Color * vec4(color, 1.0);
}