#version 150

in vec2 pass_texcoord;
in vec3 pass_normal;
in vec4 pass_weights;

out vec4 color;

uniform bool useDiffuseMap = false;
uniform vec4 diffuseColor;
uniform sampler2D diffuseMap;

void main(void) {
    color = useDiffuseMap ? texture(diffuseMap, pass_texcoord) : diffuseColor;
}