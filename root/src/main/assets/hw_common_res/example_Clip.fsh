
#ifdef GL_ES
precision lowp float;
#endif

varying vec4 v_fragmentColor;
varying vec2 v_texCoord;

uniform sampler2D tex_mask;

uniform int isPng;

void main()
{
  vec4 c = texture2D(CC_Texture0, v_texCoord);
  vec4 cMask = texture2D(tex_mask, v_texCoord);
  if(isPng!=0) {
    cMask.rgb = cMask.aaa;
    gl_FragColor = c*cMask*v_fragmentColor;
  }
  else {
	  gl_FragColor.r = c.r * v_fragmentColor.r;
	  gl_FragColor.g = c.g * v_fragmentColor.g;
	  gl_FragColor.b = c.b * v_fragmentColor.b;
	  gl_FragColor.a = c.a * cMask.a * v_fragmentColor.a;
	}
}



