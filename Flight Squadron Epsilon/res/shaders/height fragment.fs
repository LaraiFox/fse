#version 120

uniform vec3 color;

varying float yPos;

void main() {
	float mapColor = clamp((yPos + 100.0) / 200.0, 0.0, 1.0);
	gl_FragColor = vec4(color, 1.0) * vec4(mapColor, 0.0, 0.0f, 1.0f);
}