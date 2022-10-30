#version 150

in vec2 position;

uniform mat4 world;
uniform mat4 camera;
uniform mat4 transform;

void main(void) {
    gl_Position = world * camera * transform * vec4(position.x, position.y, 0, 1);

}
