#version 150

in vec2 pass_texcoord;
in float visibility;

out vec4 color;

uniform vec3 sky_color;

uniform bool useDiffuseMap = false;
uniform vec4 diffuseColor;
uniform sampler2D diffuseMap;

void main()
{
    color = useDiffuseMap ? texture(diffuseMap, pass_texcoord) : diffuseColor;
    color = mix(vec4(sky_color, 1), color, visibility);
}
