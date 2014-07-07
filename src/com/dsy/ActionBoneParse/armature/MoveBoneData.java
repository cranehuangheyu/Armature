package com.dsy.ActionBoneParse.armature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MoveBoneData {
	public String name;
	public float dl;
	public List<FrameData> frameDataVector;
	
	public BoneData boneData;
	
	public MoveBoneData()
	{
		frameDataVector = new ArrayList<FrameData>();
	}
}
