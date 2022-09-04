#version 150

in vec3 position;

out vec3 texcoord;

uniform mat4 transform;

void main()
{
    texcoord = position;
    gl_Position = vec4(position, 1.0).xyww;
}
