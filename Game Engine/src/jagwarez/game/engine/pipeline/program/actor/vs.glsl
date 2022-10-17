#version 150

const int MAX_LIGHTS = 10;
const float FOG_DENSITY = 0.003;
const float FOG_GRADIENT = 10;
const int MAX_BONES = 100;//max bones allowed in a skeleton
const int MAX_WEIGHTS = 4;//max number of bones that can affect a vertex

in vec3 position;
in ivec4 bones;
in vec4 weights;
in vec2 texcoord;
in vec3 normal;

out vec3 to_camera;
out float visibility;
out vec3 to_lights[MAX_LIGHTS];
out vec2 pass_texcoord;
out vec3 pass_normal;
out vec4 pass_weights;

uniform mat4 world;
uniform mat4 camera;
uniform mat4 transform;
uniform mat4 bone_transforms[MAX_BONES];
uniform mat4x3 lights[MAX_LIGHTS];
uniform int light_count;

void main(void){
	
    vec4 world_position = vec4(0);
    vec4 world_normal = vec4(0);

    for(int i = 0; i < MAX_WEIGHTS && bones[i] != -1; i++) {
        
        mat4 bone_transform = bone_transforms[bones[i]];
        vec4 bone_position = bone_transform * vec4(position, 1.0);
        world_position += bone_position * weights[i];

        vec4 bone_normal = bone_transform * vec4(normal, 0.0);
        world_normal += bone_normal * weights[i];
        
    }

    world_position = transform * world_position;
    world_normal = transform * world_normal;

    to_camera = (inverse(camera)*vec4(0,0,0,1)).xyz - world_position.xyz;

    for(int i = 0; i < light_count; i++) {
       mat4x3 light = lights[i];
       to_lights[i] = normalize(vec3(light[0][0],light[1][0],light[2][0]) - world_position.xyz);
    }

    float distance = length(to_camera.xz);

    visibility = clamp(exp(-pow(distance*FOG_DENSITY, FOG_GRADIENT)), 0, 1);

    gl_Position = world * camera * world_position;
    pass_normal = normalize((world_normal).xyz);
    pass_texcoord = texcoord;
    pass_weights = weights;
}