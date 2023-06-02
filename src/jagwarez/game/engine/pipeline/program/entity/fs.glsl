/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
#version 150

in vec2 pass_texcoord;

out vec4 color;

uniform bool useDiffuseMap = false;
uniform vec4 diffuseColor;
uniform sampler2D diffuseMap;

void main(void) {
    color = useDiffuseMap ? texture(diffuseMap, pass_texcoord) : diffuseColor;
}
