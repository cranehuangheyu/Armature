package com.dsy.ActionBoneParse.armature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Armature {
	public float content_scale;
	public ArmatureData armatureData;
	public AnimationData animationData;
	public Map<String, TextureData> textureDataVector;
	public List<String> configFilePathVector;
	
	public Armature()
	{
		armatureData = new ArmatureData();
		animationData = new AnimationData();
		
		textureDataVector = new HashMap<String, TextureData>();
		configFilePathVector = new ArrayList<String>();
	}
}
