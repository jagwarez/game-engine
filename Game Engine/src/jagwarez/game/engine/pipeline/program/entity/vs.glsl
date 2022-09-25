/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
#version 150

uniform mat4 transform;

in vec3 position;
in vec3 normal;
in vec2 texcoord;

out vec2 pass_texcoord;

void main(void) {
    gl_Position = transform * vec4(position, 1.0);
    pass_texcoord = texcoord;
}
