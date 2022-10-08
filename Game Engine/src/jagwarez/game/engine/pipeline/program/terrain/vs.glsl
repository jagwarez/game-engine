#version 150

const int MAX_LIGHTS = 10;
const float FOG_DENSITY = 0.003;
const float FOG_GRADIENT = 8;

in vec2 position;

out vec3 normal;
out float visibility;
out vec3 to_camera;
out vec3 to_lights[MAX_LIGHTS];

uniform mat4 world;
uniform mat4 camera;
uniform mat4 terrain;
uniform int hscale;
uniform int twidth;
uniform int theight;
uniform sampler2D hmap;
uniform mat4x3 lights[MAX_LIGHTS];
uniform int light_count;

float map_height(vec2 pos) {
    if(pos.x >= 0 && pos.y >= 0 && pos.x <= twidth && pos.y <= theight) {
        vec2 st = 1-vec2(pos.x/twidth, pos.y/theight);
        return texture(hmap, st).r*255;
    } else
        return 0;
}

void main(void) {

    vec4 world_pos = terrain * vec4(position.x, 0, position.y, 1);

    world_pos.y = map_height(world_pos.xz);

    to_camera = (inverse(camera)*vec4(0,0,0,1)).xyz - world_pos.xyz;

    for(int i = 0; i < light_count; i++) {
       mat4x3 light = lights[i];
       to_lights[i] = normalize(vec3(light[0][0],light[1][0],light[2][0]) - world_pos.xyz);
    }

    vec3 off = vec3(1, 0, 1);
    float hR = map_height(world_pos.xz - off.xy);
    float hL = map_height(world_pos.xz + off.xy);
    float hB = map_height(world_pos.xz - off.yz);
    float hF = map_height(world_pos.xz + off.yz);
    
    normal.x = hR - hL;
    normal.y = hF - hB;
    normal.z = 1;
    normal = normalize(normal);

    float distance = length(to_camera.xz);

    visibility = clamp(exp(-pow(distance*FOG_DENSITY, FOG_GRADIENT)), 0, 1);

    gl_Position = world * camera * world_pos;
}
