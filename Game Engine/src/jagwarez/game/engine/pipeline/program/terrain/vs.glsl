#version 150

const float density = 0.004;
const float gradient = 3;

in vec2 position;

out vec3 to_camera;
out float visibility;
out float height;

uniform mat4 view;
uniform mat4 model;
uniform float twidth;
uniform vec2 offset;
uniform bool use_hmap = false;
uniform sampler2D hmap;

void main(void) {
    
    vec2 map_pos = position + offset;
    if(map_pos.x >= 0 && map_pos.y >= 0 && map_pos.x <= twidth && map_pos.y <= twidth) {
        vec2 st = map_pos/twidth;
        height = use_hmap == true ? 125*texture(hmap, st).r : 0;
    } else
        height = 0;
        
    vec4 world_pos = model * vec4(position.x, height, position.y, 1.0);
   
    to_camera = (inverse(view)*vec4(0,0,0,1)).xyz - world_pos.xyz;

    world_pos = view * world_pos;

    float distance = length(world_pos.xyz);

    visibility = clamp(exp(-pow(distance*density, gradient)), 0, 1);

    gl_Position = world_pos;
}
