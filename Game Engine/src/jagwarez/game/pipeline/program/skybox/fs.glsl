#version 150

in vec3 texcoord;

out vec4 color;

uniform samplerCube skybox;

void main()
{
    color = texture(skybox, texcoord);
}
