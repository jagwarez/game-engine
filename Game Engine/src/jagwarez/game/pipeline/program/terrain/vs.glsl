#version 150

const float density = 0.005;
const float gradient = 1.5;

in vec2 position;

out vec3 camera_vec;
out float visibility;

uniform mat4 camera;
uniform mat4 transform;
uniform bool use_hmap = false;
uniform sampler2D hmap;

void main(void) {
    
    float height = use_hmap == true ? texture(hmap, vec2(position.x/511.0,position.y/511.0)).r : 0;
    vec4 world_pos = transform * vec4(position.x, height*100, position.y, 1.0);
   
    camera_vec = (inverse(camera)*vec4(0,0,0,1)).xyz - world_pos.xyz;

    world_pos = camera * world_pos;

    float distance = length(world_pos.xyz);

    visibility = clamp(exp(-pow(distance*density, gradient)), 0, 1);

    gl_Position = world_pos;
}
