#version 150

in vec3 pass_normal;
in vec2 pass_texcoord;
in vec4 pass_weights;

out vec4 color;

uniform sampler2D diffuseMap;

void main(void) {
    color = texture(diffuseMap, pass_texcoord);
}