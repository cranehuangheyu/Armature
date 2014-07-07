package com.dsy.ActionBoneParse.armature;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class AnimationData {
	public String name;
	public Map<String, MoveData> moveDataVector;
	
	public AnimationData()
	{
		moveDataVector = new HashMap<String, MoveData>();
	}
}
