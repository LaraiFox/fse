#version 120

varying vec2 texCoord;

uniform vec3 color;
uniform sampler2D sampler;

void main() {
//	gl_FragColor = vec4(color, 1);

	vec4 textureColor = texture2D(sampler, texCoord.xy);

    if(textureColor == vec4(0,0,0,0))
        gl_FragColor = vec4(color, 1);
    else
        gl_FragColor = textureColor * vec4(color, 1);
}