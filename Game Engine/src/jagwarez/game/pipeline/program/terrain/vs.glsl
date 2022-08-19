#version 150

uniform mat4 transform;

in vec2 position;

void main(void) {
  gl_Position = transform * vec4(position.x, 0.0, position.y, 1.0);
}
