#version 150

const int MAX_BONES = 100;//max bones allowed in a skeleton
const int MAX_WEIGHTS = 4;//max number of bones that can affect a vertex

in vec3 position;
in ivec4 bones;
in vec4 weights;

out vec4 world_position;

uniform mat4 world;
uniform mat4 camera;
uniform mat4 transform;
uniform mat4 bone_transforms[MAX_BONES];

void main(void){
	
    world_position = vec4(0);

    for(int i = 0; i < MAX_WEIGHTS && bones[i] != -1; i++) {
        
        mat4 bone_transform = bone_transforms[bones[i]];
        vec4 bone_position = bone_transform * vec4(position, 1.0);
        world_position += bone_position * weights[i];
       
    }

    world_position = transform * world_position;

    gl_Position = world * camera * world_position;

}
