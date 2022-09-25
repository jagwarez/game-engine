#version 150

in vec2 position;

out float height;

uniform float twidth;
uniform vec2 offset;

uniform bool use_hmap = false;
uniform sampler2D hmap;

void main(void) {
    
    vec2 st = ((position + offset)/twidth);
    
    height = use_hmap == true && st.x >= 0 && st.x <= twidth && st.y >= 0 && st.y <= twidth ? 100*texture(hmap, st).r : 0;

    gl_Position = vec4(((position.xy*2)/512)-1, -height, 1);
}
