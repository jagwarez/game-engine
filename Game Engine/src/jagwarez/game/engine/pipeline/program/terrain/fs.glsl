#version 150

const int MAX_LIGHTS = 10;

in vec3 normal;
in float visibility;
in vec3 to_lights[MAX_LIGHTS];

out vec4 color;

uniform vec3 sky_color;
uniform mat4x3 lights[MAX_LIGHTS];
uniform int light_count;

void main(void) {

    color = vec4(0,0,0,1.0);

    for(int i = 0; i < light_count; i++) {

        mat4x3 light = lights[i];

        vec4 col = vec4(light[0][1], light[1][1], light[2][1], light[3][1]);
        vec3 att = vec3(light[0][2], light[1][2], light[2][2]);
        float intensity = light[3][2];

        float brightness = max(dot(to_lights[i], normal),0);

        color += mix(color, col, brightness);
    }

    color = mix(vec4(sky_color, 1), color, visibility);
}