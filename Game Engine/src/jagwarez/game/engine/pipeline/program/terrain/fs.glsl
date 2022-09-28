#version 150

const int MAX_LIGHTS = 10;

in vec3 camera_vec;
in float visibility;
in float height;

out vec4 color;

uniform vec3 sky_color;
uniform vec3 map_color;
uniform mat4x3 lights[MAX_LIGHTS];
uniform float light_count;

void main(void) {
    color = vec4((map_color.x+height)/100, 0, 0, 1.0);
    color = mix(vec4(sky_color, 1), vec4(map_color,1), visibility);
    
    //for(int i = 0; i < light_count; i++)
        //color = mix(
}
