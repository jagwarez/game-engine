#version 150

const float density = 0.004;
const float gradient = 5;

in vec2 position;

out vec3 camera_vec;
out float visibility;
out float height;

uniform mat4 camera;
uniform mat4 transform;
uniform float twidth;
uniform vec2 offset;
uniform bool use_hmap = false;
uniform sampler2D hmap;

void main(void) {
    
    vec2 st = ((position + offset)/twidth);
    
    height = use_hmap == true && st.x >= 0 && st.x <= twidth && st.y >= 0 && st.y <= twidth ? 100*texture(hmap, st).r : 0;
    vec4 world_pos = transform * vec4(position.x, height, position.y, 1.0);
   
    camera_vec = (inverse(camera)*vec4(0,0,0,1)).xyz - world_pos.xyz;

    world_pos = camera * world_pos;

    float distance = length(world_pos.xz);

    visibility = clamp(exp(-pow(distance*density, gradient)), 0, 1);

    gl_Position = world_pos;
}
