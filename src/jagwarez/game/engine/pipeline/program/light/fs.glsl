#version 150

in vec4 local_position;

out vec4 color;

uniform vec3 light_color;
uniform float radius;

void main(void) {

    float len = length(local_position.xy*radius);

    color = vec4(light_color, 1-(len/radius));
}