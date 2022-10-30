#version 150

layout (points) in;
layout (triangle_strip, max_vertices = 4) out;

in float has_light[1];
in vec4 pass_color[1];

out vec4 light_color;

void main(void) {

    light_color = pass_color[0];

    if(has_light[0] == 1) {

        vec3 position = gl_in[0].gl_Position.xyz;

        gl_Position = vec4(position.x+100, position.y+100, position.z, 1);
        EmitVertex();

        gl_Position = vec4(position.x+100, position.y-100, position.z, 1);
        EmitVertex();

        gl_Position = vec4(position.x-100, position.y+100, position.z, 1);
        EmitVertex();

        gl_Position = vec4(position.x-100, position.y-100, position.z, 1);
        EmitVertex();
    }
}