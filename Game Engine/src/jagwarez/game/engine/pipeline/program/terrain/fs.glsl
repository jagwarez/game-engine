#version 150

in vec3 to_camera;
in float visibility;
in float height;

out vec4 color;

uniform vec3 sky_color;
uniform vec3 map_color;

void main(void) {
    color = vec4((map_color.x+height)/100, 0, 0, 1.0);
    color = mix(vec4(sky_color, 1), color, visibility);
}