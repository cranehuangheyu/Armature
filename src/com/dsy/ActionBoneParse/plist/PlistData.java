package com.dsy.ActionBoneParse.plist;

import java.util.HashMap;
import java.util.Map;

public class PlistData {

	public Map<String, FrameData> frames;
	
	// metadata
	public int format;
	public String textureFileName;
	public String realTextureFileName;
	public String size;
	
	// texture
	public int width;
	public int height;
	
	public PlistData()
	{
		frames = new HashMap<String, FrameData>();
	}
}
