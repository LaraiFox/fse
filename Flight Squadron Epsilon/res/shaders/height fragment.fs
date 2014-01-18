#version 120

uniform vec3 color;

varying float yPos;

void main() {
	float mapColor = clamp((yPos + 150.0) / 800.0, 0.05f, 0.95f);
	
	mapColor = float(int(mapColor * 16.0f) / 16.0f);
	
	gl_FragColor = vec4(color, 1.0) * vec4(mapColor, mapColor, mapColor, 1.0f);
}