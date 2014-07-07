package com.dsy.ActionBoneParse.armature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ArmatureData {
	public String strVersion;
	public float version;
	public String name;
	public Map<String, BoneData> boneDataArray;
	public List<BoneData> boneDatas;
	
	public ArmatureData()
	{
		boneDataArray = new HashMap<String, BoneData>();
		boneDatas = new ArrayList<BoneData>();
	}
}
