#version 330

in vec2 position;

out vec4 world_position;

uniform mat4 world;
uniform mat4 camera;
uniform mat4 transform;

uniform sampler2D height_map;
uniform int map_width;
uniform int map_length;

float map_height(vec2 pos) {
    if(pos.x >= 0 && pos.y >= 0 && pos.x <= map_width && pos.y <= map_length) {
        vec2 st = 1-vec2(pos.x/map_width, pos.y/map_length);
        return texture(height_map, st).r*255;
    } else
        return 0;
}

void main(void) {
    world_position = transform * vec4(position.x, 0, position.y, 1);

    world_position.y = map_height(world_position.xz);

    gl_Position = world * camera * world_position;
}

