#version 120

uniform vec3 color;

varying float yPos;

void main() {
	float mapColor = clamp((yPos + 1000.0) / 5000.0, 0.0f, 0.9f);
	
	mapColor = float(int(mapColor * 64.0f) / 64.0f);
	
	gl_FragColor = vec4(color, 1.0) * vec4(0.0f, mapColor, 0.0f, 1.0f);
}