#version 120

attribute vec3 positionAttrib;

uniform transform;

void main() {
    gl_Position = transform * vec4(positionAttrib, 1.0);
}