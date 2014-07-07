package com.dsy.ActionBoneParse.armature;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class BoneData {
	public String name;
	public String parent;
	public float x;
	public float y;
	public float z;
	public float cX;
	public float cY;
	public float kX;
	public float kY;
	public float arrow_x;
	public float arrow_y;
	public boolean effectbyskeleton;
	
	public DisplayData displayData;
	
	public BoneData parentBoneData;
	
	public int index;
}
