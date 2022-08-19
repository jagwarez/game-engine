#version 150

out vec4 color;

uniform vec3 patch_color;

void main(void) {
  color = vec4(patch_color, 1.0);
}
