package com.dsy.man;

import com.dsy.ActionBoneParse.ArmatureObj;
import com.dsy.ActionBoneParse.util.Graphics;

public class BigGunMan extends ArmatureObj {
	public BigGunMan() {
		super("BigGunMan/DemoPlayer.ExportJson");
		// TODO Auto-generated constructor stub
		
		playWithAnimation("挨打");
	}
	
	public void draw(Graphics g) {
		super.draw(g);
	}
	
	public void logic(int dt) {
		super.logic(dt);
	}
}
