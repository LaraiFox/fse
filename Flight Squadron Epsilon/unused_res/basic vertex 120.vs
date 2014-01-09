#version 120

attribute vec3 positionAttrib;
attribute vec2 texCoordAttrib;

varying vec2 texCoord;

uniform mat4 transform;

void main() {
    texCoord = texCoordAttrib;

//    vec4 position = transform * vec4(positionAttrib, 1.0);
    gl_Position = transform * vec4(positionAttrib, 1.0);
}