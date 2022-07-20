#version 150

const int MAX_JOINTS = 50;//max joints allowed in a skeleton
const int MAX_WEIGHTS = 4;//max number of joints that can affect a vertex

in vec3 position;
in vec3 normal;
in vec2 texcoord;
in ivec4 joints;
in vec4 weights;

out vec3 pass_normal;
out vec2 pass_texcoord;

uniform mat4 transform;
uniform mat4 joint_transforms[MAX_JOINTS];

void main(void){
	
    vec4 final_position = vec4(0.0);
    vec4 final_normal = vec4(0.0);

    for(int i=0;i<MAX_WEIGHTS;i++) {
        
        if(joints[i] != -1) {
            mat4 joint_transform = joint_transforms[joints[i]];
            vec4 new_position = joint_transform * vec4(position, 1.0);
            final_position += new_position * weights[i];

            vec4 world_normal = joint_transform * vec4(normal, 0.0);
            final_normal += world_normal * weights[i];
        }

    }

    gl_Position = transform * vec4(position, 1.0);
    pass_normal = final_normal.xyz;
    pass_texcoord = texcoord;

}