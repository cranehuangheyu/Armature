package com.dsy.ActionBoneParse;

public interface UserData {
	// 点击按钮后的缩放
	public static final float BUTTON_SCALE = 0.9f;
	
	// 角色索引
	public static final byte ROLE_NPC_0 = 0;//npc0
	public static final byte ROLE_NPC_1 = 1;//npc1
	public static final byte ROLE_NPC_2 = 2;//	npc2
	public static final byte ROLE_NPC_3 = 3;//	npc3
	public static final byte ROLE_NPC_4 = 4;//	npc4
	public static final byte ROLE_NPC_5 = 5;//	npc5
	public static final byte ROLE_BOSS_THROW_0 = 6;//	boss0投
	public static final byte ROLE_BOSS_THROW_1 = 7;//	boss1投
	public static final byte ROLE_BOSS_THROW_2 = 8;//	boss2投
	public static final byte ROLE_BOSS_THROW_3 = 9;//	boss3投
	public static final byte ROLE_BOSS_THROW_4 = 10;//	boss4投
	public static final byte ROLE_CHICK = 11;//	鸡
	public static final byte ROLE_BOSS_PENG_0 = 12;//	boss0撞
	public static final byte ROLE_BOSS_PENG_1 = 13;//boss1撞
	public static final byte ROLE_BOSS_PENG_2 = 14;//boss2撞
	public static final byte ROLE_BOSS_PENG_3 = 15;//boss3撞
	public static final byte ROLE_BOSS_PENG_4 = 16;//boss4撞
	public static final byte ROLE_CATERAN = 17;//劫匪
	public static final byte ROLE_THIEF = 18;//小偷
	public static final byte ROLE_POLICE = 19;//警察
	public static final byte ROLE_CAR = 20;//上方警车
	public static final byte ROLE_THIEF_NPC = 21;//上方贼
	public static final byte ROLE_CATERAN_NPC = 22;//上方劫匪

	// 角色出现位置
	public static final int[][] STAGE_ROLE_POSITION = {
		{200,ROLE_BOSS_THROW_0},
		{240,ROLE_NPC_1},
		{280,ROLE_NPC_2},
		{320,ROLE_NPC_3},
		{360,ROLE_NPC_4},
		{400,ROLE_NPC_5},
		{440,ROLE_BOSS_PENG_0},
		{500,ROLE_CHICK},
		{550,ROLE_CHICK},
		{540,ROLE_CHICK},
		{560,ROLE_CHICK},
		{580,ROLE_CHICK},
		{1000,ROLE_NPC_1},
		{1040,ROLE_NPC_1},
		{1080,ROLE_NPC_3},
		{1120,ROLE_NPC_3},
		{1160,ROLE_NPC_3},
		{1200,ROLE_NPC_5},
		{1240,ROLE_NPC_3},
		{1280,ROLE_NPC_5},
		{1320,ROLE_NPC_5},
		{1360,ROLE_NPC_3},
		{1400,ROLE_NPC_4},
		{1440,ROLE_NPC_3},
		{1480,ROLE_NPC_4},
		{1500,ROLE_THIEF},
		{2000,ROLE_CHICK},
		{2020,ROLE_CHICK},
		{2040,ROLE_CHICK},
		{3000,ROLE_POLICE},
		{3300,ROLE_NPC_0},
		{3350,ROLE_CHICK},
		{4000,ROLE_THIEF},
		{4050,ROLE_THIEF},
		{4500,ROLE_CHICK},
		{4520,ROLE_CHICK},
		{4540,ROLE_CHICK},
		{4560,ROLE_CHICK},
		{4580,ROLE_CHICK},
		{5000,ROLE_CAR},
		{6000,ROLE_THIEF},
		{6060,ROLE_THIEF},
		{6090,ROLE_THIEF},
		{7000,ROLE_CHICK},
		{7020,ROLE_CHICK},
		{7040,ROLE_CHICK},
		{7060,ROLE_CHICK},
		{7080,ROLE_CHICK},
		{8500,ROLE_POLICE},
		{9000,ROLE_BOSS_PENG_0},
		{9500,ROLE_NPC_0},
		{10000,ROLE_BOSS_THROW_0},
		{11000,ROLE_CHICK},
		{11020,ROLE_CHICK},
		{11040,ROLE_CHICK},
		{11060,ROLE_CHICK},
		{12000,ROLE_THIEF},
		{12050,ROLE_CATERAN},
		{13000,ROLE_NPC_0},
		{13300,ROLE_POLICE},
		{13500,ROLE_CHICK},
		{13520,ROLE_CHICK},
		{13540,ROLE_CHICK},
		{13560,ROLE_CHICK},
		{13580,ROLE_CHICK},
		{13600,ROLE_CHICK},
		{13620,ROLE_CHICK},
		{14000,ROLE_CAR},
		{15000,ROLE_CATERAN},
		{15050,ROLE_CATERAN},
		{15100,ROLE_CATERAN},
		{15150,ROLE_CATERAN},
		{16000,ROLE_CHICK},
		{16020,ROLE_CHICK},
		{16040,ROLE_CHICK},
		{16060,ROLE_CHICK},
		{16080,ROLE_CHICK},
		{16100,ROLE_CHICK},
		{16120,ROLE_CHICK},
		{17000,ROLE_CATERAN},
		{17090,ROLE_THIEF},
		{17140,ROLE_THIEF},
		{18000,ROLE_POLICE},
		{19000,ROLE_BOSS_PENG_0},
		{19050,ROLE_CHICK},
		{19070,ROLE_CHICK},
		{19090,ROLE_CHICK},
		{19110,ROLE_CHICK},
		{19130,ROLE_CHICK},
		{19500,ROLE_NPC_0},
		{20000,ROLE_BOSS_THROW_0},
	};
	
	// 过关条件
	public static final int[][] STAGE_PASS_CONDITION = {
		{10000,1,20},
		{20000,2,40},
		{30000,3,60},
		{40000,4,80},
	};
	
	// 撞飞需要的最低速度
	public static final int[][] COLLIDE_FLY_SPEED = {
		{16,18,21,24,60},
		{19,21,24,27,60},
		{21,23,26,29,60},
		{25,27,30,33,60},
		{32,34,37,40,60},
	};
	
	// 距离
	public static final int[] FAR = {
		0, 10000, 30000, 50000, 100000};

//	// 猫数据
	public static final int[][][][] CAT_DATA = {
			// 关卡
			{// 猫咪 0
					{
							// 洞察力 图标显示持续时间 位置 坐标
							{ 0 }, { 1000 }, { 600 }, { 10, 80 }, },
					{ // 洞察力 图标显示持续时间 位置 坐标
					{ 0 }, { 1000 }, { 900 }, { 10, 80 }, { 50, 130 }, },
					{ // 洞察力 图标显示持续时间 位置 坐标
					{ 0 }, { 1000 }, { 1200 }, { 10, 80 }, { 50, 130 },
							{ 100, 80 }, }, { // 洞察力 图标显示持续时间 位置 坐标
					{ 0 }, { 1000 }, { 1700 }, { 10, 80 }, { 50, 130 },
							{ 100, 80 }, { 150, 30 }, }, { // 洞察力 图标显示持续时间 位置 坐标
					{ 0 }, { 1000 }, { 1900 }, { 10, 80 }, { 50, 130 },
							{ 100, 80 }, { 150, 30 }, { 200, 130 }, }, }, {// 猫咪
																			// 1
					{
							// 洞察力 图标显示持续时间 位置 坐标
							{ 0 }, { 1000 }, { 600 }, { 10, 80 }, }, { // 洞察力
																		// 图标显示持续时间
																		// 位置 坐标
					{ 0 }, { 1000 }, { 900 }, { 10, 80 }, { 50, 130 }, }, { // 洞察力
																			// 图标显示持续时间
																			// 位置
																			// 坐标
					{ 0 }, { 1000 }, { 1200 }, { 10, 80 }, { 50, 130 },
							{ 100, 80 }, }, { // 洞察力 图标显示持续时间 位置 坐标
					{ 0 }, { 1000 }, { 1700 }, { 10, 80 }, { 50, 130 },
							{ 100, 80 }, { 150, 30 }, }, { // 洞察力 图标显示持续时间 位置 坐标
					{ 0 }, { 1000 }, { 1900 }, { 10, 80 }, { 50, 130 },
							{ 100, 80 }, { 150, 30 }, { 200, 130 }, }, }, {// 猫咪
																			// 2
					{
							// 洞察力 图标显示持续时间 位置 坐标
							{ 0 }, { 1000 }, { 600 }, { 10, 80 }, }, { // 洞察力
																		// 图标显示持续时间
																		// 位置 坐标
					{ 0 }, { 1000 }, { 900 }, { 10, 80 }, { 50, 130 }, }, { // 洞察力
																			// 图标显示持续时间
																			// 位置
																			// 坐标
					{ 0 }, { 1000 }, { 1200 }, { 10, 80 }, { 50, 130 },
							{ 100, 80 }, }, { // 洞察力 图标显示持续时间 位置 坐标
					{ 0 }, { 1000 }, { 1700 }, { 10, 80 }, { 50, 130 },
							{ 100, 80 }, { 150, 30 }, }, { // 洞察力 图标显示持续时间 位置 坐标
					{ 0 }, { 1000 }, { 1900 }, { 10, 80 }, { 50, 130 },
							{ 100, 80 }, { 150, 30 }, { 200, 130 }, }, }, {// 猫咪
																			// 3
					{
							// 洞察力 图标显示持续时间 位置 坐标
							{ 0 }, { 1000 }, { 600 }, { 10, 80 }, },
					{ // 洞察力 图标显示持续时间 位置 坐标
					{ 0 }, { 1000 }, { 900 }, { 10, 80 }, { 50, 130 }, },
					{ // 洞察力 图标显示持续时间 位置 坐标
					{ 0 }, { 1000 }, { 1200 }, { 10, 80 }, { 50, 130 },
							{ 100, 80 }, }, { // 洞察力 图标显示持续时间 位置 坐标
					{ 0 }, { 1000 }, { 1700 }, { 10, 80 }, { 50, 130 },
							{ 100, 80 }, { 150, 30 }, }, { // 洞察力 图标显示持续时间 位置 坐标
					{ 0 }, { 1000 }, { 1900 }, { 10, 80 }, { 50, 130 },
							{ 100, 80 }, { 150, 30 }, { 200, 130 }, }, }, {// 猫咪
																			// 4
					{
							// 洞察力 图标显示持续时间 位置 坐标
							{ 0 }, { 1000 }, { 600 }, { 10, 80 }, },
					{ // 洞察力 图标显示持续时间 位置 坐标
					{ 0 }, { 1000 }, { 900 }, { 10, 80 }, { 50, 130 }, },
					{ // 洞察力 图标显示持续时间 位置 坐标
					{ 0 }, { 1000 }, { 1200 }, { 10, 80 }, { 50, 130 },
							{ 100, 80 }, }, { // 洞察力 图标显示持续时间 位置 坐标
					{ 0 }, { 1000 }, { 1700 }, { 10, 80 }, { 50, 130 },
							{ 100, 80 }, { 150, 30 }, }, { // 洞察力 图标显示持续时间 位置 坐标
					{ 0 }, { 1000 }, { 1900 }, { 10, 80 }, { 50, 130 },
							{ 100, 80 }, { 150, 30 }, { 200, 130 }, }, }, };

	// 地图长度
	public static final int GAME_SCREEN_WIDTH = Integer.MAX_VALUE;



	// 计时器周期
	public static final int FLICKER_TIME_DESTRORY = 30; // 消失
	public static final int FLICKER_TIME_FLICKER = 10; // 闪耀
	public static final int FLICKER_CYCLE = 1; // 闪耀周期

	// boss移除屏幕的速度
	public static final int BOSS_ESCAPE_SPEED = 50;

	public static final int TRANS_NONE = 0, TRANS_ROT90 = 5, TRANS_ROT180 = 3,
			TRANS_ROT270 = 6, TRANS_MIRROR = 2, TRANS_MIRROR_ROT90 = 7,
			TRANS_MIRROR_ROT180 = 1, TRANS_MIRROR_ROT270 = 4;

	// 基准屏幕大小
	public static final int screan_width = 800;
	public static final int screan_height = 480;

	// 结束时x的偏移
	public static final int BOSS_FLY_END_OFFSET = 200;

	// ui
	// 回撤按键位置
	public static int[] UI_START_3 = { 51, 28 };
	public static int[] UI_START_1 = { 794, 22 };
	public static int[] UI_START_2 = { 773, 22 };
	public static int[] UI_START_4 = { 52, 444 };
	public static int[] UI_START_5 = { 702, 436 };
	public static int[] UI_START_6 = { 166, 221 };
	public static int[] UI_START_7 = { 413, 221 };
	public static int[] UI_START_8 = { 654, 221 };
	public static int[] UI_START_9 = { 614, 25 };

	public static int[] UI_SELECT_STAGE_1 = { 400, 240 };
	public static int[] UI_SELECT_STAGE_2 = { 400, 343 };
	// 关卡选择的选项数量
	public static int STAGE_TAB_NUMBER = 5;
	
	// 商店数量
	public static int STAGE_GOLD_NUMBER = 6;
	// 帮助的选项数量
	public static int STAGE_HELP_NUMBER = 2;
	// 摩托选择的选项数量
	public static int STAGE_MOTO_NUMBER = 7;
	// 选框之间的距离
	public static int STAGE_TAB_DISTANCE = 530;
	// 点号信息
	public static int[] UI_SELECT_STAGE_DOT = { 400, 434, 28 };
	
	// 暂停界面按钮位置
	public static int[] PAUSE_UI_BUTTON_POS = {400, 122};
	// 暂停界面按钮间隔
	public static int PAUSE_BUTTON_CLOSE = 78;
	
	public static int[] screanRectArray = {0, 0, UserData.screan_width,
			UserData.screan_height};
	
	// 显示物品价格板的位置
	public static short[] DRAW_MONEY_POS = {214, 352};
	
	// 购买按钮偏移
	public static final short DRAW_MONEY_BUTTON_OFFSET = 495 - 214 + 10;
	
	// 帧延时
	public static final byte FRAME_DELAY = 80;
	// 1秒多少帧
	public static final float FPS = 1000f / FRAME_DELAY;
}
