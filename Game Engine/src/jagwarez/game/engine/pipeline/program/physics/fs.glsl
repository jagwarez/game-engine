#version 150

in float height;

out vec4 color;

void main(void) {
    color = vec4(height, 0, 0, 1);
}
