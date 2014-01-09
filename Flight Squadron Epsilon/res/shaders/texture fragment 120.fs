#version 120

uniform vec3 color;
uniform sampler2D texture;

void main() {
	gl_FragColor = texture2D(texture, gl_TexCoord[0].xy) * gl_Color * vec4(color, 1.0);
}