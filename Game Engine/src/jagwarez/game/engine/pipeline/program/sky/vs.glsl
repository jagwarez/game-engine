#version 150

in vec3 position;
in vec2 texcoord;
out vec2 pass_texcoord;

uniform mat4 transform;

void main()
{
    gl_Position = transform * vec4(position, 1.0);
    pass_texcoord = texcoord;
}
