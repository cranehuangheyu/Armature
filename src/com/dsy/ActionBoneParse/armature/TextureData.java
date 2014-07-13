package com.dsy.ActionBoneParse.armature;

import java.util.Vector;

public class TextureData {
	public String name;
	public float width;
	public float height;
	public float pX;
	public float pY;
	public String plistFile;
	public ContourData contourData;
	
	public TextureData()
	{
		contourData = new ContourData();
	}
}
