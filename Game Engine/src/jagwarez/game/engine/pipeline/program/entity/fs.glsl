/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
#version 150

out vec4 color;

uniform vec4 diffuse;

void main(void) {
  color = diffuse;
}
