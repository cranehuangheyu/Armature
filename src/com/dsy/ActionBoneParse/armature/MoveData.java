package com.dsy.ActionBoneParse.armature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MoveData {
	public String name;
	public int dr;
	public boolean lp;
	public int to;
	public int drTW;
	public int twE;
	public float sc;
	
	public List<MoveBoneData> moveDataVector;
	
	public MoveData()
	{
		moveDataVector = new ArrayList<MoveBoneData>();
	}
}
