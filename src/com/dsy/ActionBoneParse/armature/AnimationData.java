package com.dsy.ActionBoneParse.armature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class AnimationData {
	public String name;
	public Map<String, MoveData> moveDataMap;
	public List<MoveData> moveDataVector;
	
	public AnimationData()
	{
		moveDataMap = new HashMap<String, MoveData>();
		moveDataVector = new ArrayList<MoveData>();
	}
}
