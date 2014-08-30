package com.dsy.ActionBoneParse.armature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	
	public List<DisplayData> displayDataVector;
	
	public BoneData parentBoneData;
	
	public List<DrawableData> drawableDataVector;
	
	public int index;
	
	public BoneData() {
		drawableDataVector = new ArrayList<DrawableData>();
		displayDataVector = new ArrayList<DisplayData>();
	}
}
