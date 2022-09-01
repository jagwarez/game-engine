#version 330

uniform uint draw_index;
uniform uint obj_index;

out vec3 color;

void main()
{
    color = vec3(float(obj_index), float(draw_index),float(gl_PrimitiveID + 1));
}
