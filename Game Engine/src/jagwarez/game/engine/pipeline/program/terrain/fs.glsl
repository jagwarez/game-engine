#version 150

in vec3 camera_vec;
in float visibility;
in float height;

out vec4 color;

uniform vec3 sky_color;
uniform vec3 patch_color;

void main(void) {
    color = vec4(0, (patch_color.x+height)/100, 0, 1.0);
    color = mix(vec4(sky_color, 1), color, visibility);
}
