#version 150

in vec2 position;

out vec4 local_position;

uniform mat4 world;
uniform mat4 camera;
uniform mat4 transform;

void main(void) {

    local_position = vec4(position.x, position.y, 0, 1);

    gl_Position = world * camera * transform * local_position;

}
