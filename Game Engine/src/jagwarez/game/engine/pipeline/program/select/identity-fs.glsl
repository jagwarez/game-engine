#version 330

in vec4 world_position;

out vec4 color;

uniform int identity;

void main()
{
    color = vec4(float(identity), world_position.xyz);
}

