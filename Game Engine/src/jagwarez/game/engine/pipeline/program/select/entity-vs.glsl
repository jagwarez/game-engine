#version 330

in vec3 position;

out vec4 world_position;

uniform mat4 world;
uniform mat4 camera;
uniform mat4 transform;

void main()
{
    world_position = transform * vec4(position, 1.0);
    gl_Position = world * camera * world_position;
}
