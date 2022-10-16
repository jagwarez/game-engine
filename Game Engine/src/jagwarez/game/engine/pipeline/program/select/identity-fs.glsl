#version 330

varying vec4 world_position;

out vec4 color;

uniform uint identity;

void main()
{
    color = vec4(float(identity), world_position.xyz);
}

