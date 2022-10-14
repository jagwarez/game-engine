#version 150

const int MAX_LIGHTS = 10;

in vec3 normal;
in float visibility;
in vec3 to_lights[MAX_LIGHTS];

out vec4 color;

uniform bool fog;
uniform vec3 sky_color;
uniform mat4x3 lights[MAX_LIGHTS];
uniform int light_count;

void main(void) {

    color = vec4(0,0,0,1.0);

    for(int i = 0; i < light_count; i++) {

        mat4x3 light = lights[i];
        float distance = length(to_lights[i]);

        vec4 light_color = vec4(light[0][1], light[1][1], light[2][1], light[3][1]);
        vec3 attenuation = vec3(light[0][2], light[1][2], light[2][2]);
        float intensity = light[3][2];

        float att = attenuation.x + (attenuation.y * distance) + (attenuation.z * distance * distance);

        float brightness = clamp(dot(normal,to_lights[i]),0,1);

        color += mix(color, light_color * intensity, brightness * att);
    }

    if(fog)
        color = mix(vec4(sky_color, 1), color, visibility);
}