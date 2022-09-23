#version 150

in vec3 texcoord;

out vec4 color;

uniform samplerCube skybox;
uniform vec3 sky_color;

void main()
{
    color = texture(skybox, texcoord);
    color = mix(vec4(sky_color, 1), color, texcoord.y < 0 ? 0 : pow(texcoord.y, .5));
}