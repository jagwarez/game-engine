#version 150

const int MAX_BONES = 150;//max bones allowed in a skeleton
const int MAX_WEIGHTS = 4;//max number of bones that can affect a vertex

in vec3 position;
in vec2 texcoord;
in vec3 normal;
in ivec4 bones;
in vec4 weights;

out vec2 pass_texcoord;
out vec3 pass_normal;
out vec4 pass_weights;

uniform mat4 transform;
uniform mat4 bone_transforms[MAX_BONES];

void main(void){
	
    vec4 final_position = vec4(0.0);
    vec4 final_normal = vec4(0.0);

    for(int i = 0; i < 4 && bones[i] != -1; i++) {
        
        mat4 bone_transform = bone_transforms[bones[i]];
        vec4 new_position = bone_transform * vec4(position, 1.0);
        final_position += new_position * weights[i];

        vec4 world_normal = bone_transform * vec4(normal, 0.0);
        final_normal += world_normal * weights[i];
        
    }

    gl_Position = transform * final_position;
    pass_normal = final_normal.xyz;
    pass_texcoord = texcoord;
    pass_weights = weights;
}