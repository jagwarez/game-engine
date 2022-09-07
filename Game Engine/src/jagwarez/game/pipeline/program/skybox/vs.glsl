#version 150

in vec3 position;

out vec3 texcoord;

uniform mat4 transform;

void main()
{
    texcoord = position/384;
    gl_Position = transform * vec4(position, 1.0);
}
