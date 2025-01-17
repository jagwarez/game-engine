#version 150

const int MAX_LIGHTS = 10;

in vec4 world_position;
in vec3 normal;
in float visibility;
in vec3 to_lights[MAX_LIGHTS];

out vec4 color;

uniform bool fog;
uniform vec3 sky_color;
uniform mat4x3 lights[MAX_LIGHTS];
uniform int light_count;
uniform bool target = false;
uniform vec3 target_position;

void main(void) {

    color = vec4(0,0,0,1);

    for(int i = 0; i < light_count; i++) {

        mat4x3 light = lights[i];
        float distance = length(to_lights[i]);

        vec4 light_color = vec4(light[0][1], light[1][1], light[2][1], light[3][1]);
        vec3 attenuation = vec3(light[0][2], light[1][2], light[2][2]);
        float intensity = light[3][2];

        float att = attenuation.x + (attenuation.y * distance) + (attenuation.z * distance * distance);

        float brightness = clamp(dot(normal,to_lights[i]),0,1);

        color += (brightness * light_color)/att;
    }

    color = max(color, .3);

    if(target) {
        float distance = distance(target_position.xyz, world_position.xyz);
        
        if(distance >= 35 && distance <= 40)
            color = mix(color, vec4(1,0,0,1), 1);
    }

    if(fog)
        color = mix(vec4(sky_color, 1), color, visibility);
}