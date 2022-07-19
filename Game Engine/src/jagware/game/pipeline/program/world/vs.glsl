/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
#version 150

uniform mat4 projection;
uniform mat4 perspective;

in vec3 position;

out Vertex {
  float visible;
} vertex;

void main(void) {
  vertex.visible = 1.0;
  gl_Position = projection * perspective * vec4(position, 1.0);
}
