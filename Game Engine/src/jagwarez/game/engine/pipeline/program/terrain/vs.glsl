#version 150

const int MAX_LIGHTS = 10;
const float FOG_DENSITY = 0.004;
const float FOG_GRADIENT = 3;

in vec2 position;

out vec3 normal;
out float visibility;
out vec3 to_camera;
out vec3 to_lights[MAX_LIGHTS];

uniform mat4 view;
uniform mat4 model;
uniform float hscale;
uniform float twidth;
uniform float theight;
uniform vec2 offset;
uniform sampler2D hmap;
uniform mat4x3 lights[MAX_LIGHTS];
uniform int light_count;

float map_height(vec2 pos) {
    if(pos.x >= 0 && pos.y >= 0 && pos.x <= twidth && pos.y <= twidth) {
        vec2 st = vec2(pos.x/twidth, pos.y/theight);
        return hscale*texture(hmap, vec2(1-st.x, st.y)).r;
    } else
        return 0;
}

void main(void) {

    vec2 map_pos = position + offset;
    
    float height = map_height(map_pos);

    vec4 world_pos = model * vec4(map_pos.x, height, map_pos.y, 1);
    vec4 scene_pos = model * vec4(position.x, height, position.y, 1);
   
    to_camera = (inverse(view)*vec4(0,0,0,1)).xyz - scene_pos.xyz;

    for(int i = 0; i < light_count; i++) {
       mat4x3 light = lights[i];
       to_lights[i] = normalize(vec3(light[0][0], light[1][0], light[2][0]) - world_pos.xyz);
    }

    scene_pos = view * scene_pos;

    float distance = length(scene_pos.xyz);

    visibility = clamp(exp(-pow(distance*FOG_DENSITY, FOG_GRADIENT)), 0, 1);

    gl_Position = scene_pos;
    
    vec3 off = vec3(1.0, 1.0, 0.0);
    float hL = map_height(map_pos.xy - off.xz);
    float hR = map_height(map_pos.xy + off.xz);
    float hD = map_height(map_pos.xy - off.zy);
    float hU = map_height(map_pos.xy + off.zy);
    
    normal.x = hL - hR;
    normal.y = hD - hU;
    normal.z = 1;
    normal = normalize(normal);
}
