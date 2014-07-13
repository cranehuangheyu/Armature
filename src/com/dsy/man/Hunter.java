package com.dsy.man;

import android.R.drawable;

import com.dsy.ActionBoneParse.ArmatureObj;
import com.dsy.ActionBoneParse.armature.BoneData;
import com.dsy.ActionBoneParse.armature.DisplayData;
import com.dsy.ActionBoneParse.armature.DrawableData;
import com.dsy.ActionBoneParse.armature.FrameData;
import com.dsy.ActionBoneParse.armature.MoveBoneData;
import com.dsy.ActionBoneParse.armature.SkinData;
import com.dsy.ActionBoneParse.util.Graphics;

public class Hunter extends ArmatureObj {

	public Hunter() {
		super("DemoPlayer/DemoPlayer.ExportJson");
		// TODO Auto-generated constructor stub
		
		playWithAnimation("walk");
		
		bodyScale = 0.15f;		
	}
	
	public void draw(Graphics g) {
		super.draw(g);
	}
	
	public void logic(int dt) {
		super.logic(dt);
	}
}
