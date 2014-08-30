package com.dsy.ActionBoneParse.manager;

import java.util.HashMap;
import java.util.Map;

import com.dsy.ActionBoneParse.armature.Armature;
import com.dsy.ActionBoneParse.armature.BoneData;
import com.dsy.ActionBoneParse.armature.DisplayData;
import com.dsy.ActionBoneParse.armature.FrameData;
import com.dsy.ActionBoneParse.armature.MoveBoneData;
import com.dsy.ActionBoneParse.armature.MoveData;
import com.dsy.ActionBoneParse.armature.SkinData;
import com.dsy.ActionBoneParse.armature.TextureData;
import com.dsy.ActionBoneParse.util.AndroidUtil;
import com.dsy.ActionBoneParse.util.BitmapManager;
import com.dsy.ActionBoneParse.util.Util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import android.graphics.Bitmap;
import android.util.Log;

public class AnimationManager {

	public static Map<String, Armature> armatureMap = new HashMap<String, Armature>();

	public AnimationManager(String jsonPath, String plistPath, String pngPath) {

	}

	public static void addArmature(String jsonPath) {
		int temp1 = jsonPath.lastIndexOf("/");
		int temp2 = jsonPath.lastIndexOf(".");
		String tempFolderPath = jsonPath.substring(0, temp1 + 1);
		String tempShortName = jsonPath.substring(temp1 + 1, temp2);

		String plistPath = tempFolderPath + tempShortName + ".plist";
		String pngPath = tempFolderPath + "Resources/";

		PlistManager plistManager = new PlistManager(plistPath, pngPath);

		Armature armature = new Armature();

		// add armature to armatureMap.
		armatureMap.put(jsonPath, armature);

		String JsonContext = AndroidUtil.ReadFile(jsonPath);
		// JSONArray jsonArray = JSONArray.fromObject(JsonContext);
		// int size = jsonArray.size();
		// System.out.println("Size: " + size);
		// for (int i = 0; i < size; i++) {
		// JSONObject jsonObject = jsonArray.getJSONObject(i);

		JSONObject jsonObject = JSONObject.fromObject(JsonContext);

		Log.e("json--output", "" + jsonObject.getDouble("content_scale"));
		armature.content_scale = (float) jsonObject.getDouble("content_scale");

		JSONArray armatureArray = jsonObject.getJSONArray("armature_data");
		for (int j = 0; j < armatureArray.size(); j++) {
			JSONObject armatureObj = armatureArray.getJSONObject(j);
			Log.e("json--output--name", "" + armatureObj.getString("name"));

			armature.armatureData.name = armatureObj.getString("name");
			armature.armatureData.strVersion = armatureObj
					.getString("strVersion");
			armature.armatureData.version = (float) armatureObj
					.getDouble("version");

			JSONArray boneDataArray = armatureObj.getJSONArray("bone_data");
			for (int k = 0; k < boneDataArray.size(); k++) {
				JSONObject boneDataObj = boneDataArray.getJSONObject(k);

				BoneData boneData = new BoneData();
				boneData.name = boneDataObj.getString("name");
				armature.armatureData.boneDataArray
						.put(boneData.name, boneData);

				armature.armatureData.boneDatas.add(boneData);

				boneData.parent = boneDataObj.getString("parent");
				boneData.x = (float) boneDataObj.getDouble("x");
				boneData.y = (float) boneDataObj.getDouble("y");
				boneData.z = (float) boneDataObj.getDouble("z");
				boneData.cX = (float) boneDataObj.getDouble("cX");
				boneData.cY = (float) boneDataObj.getDouble("cY");
				boneData.kX = (float) boneDataObj.getDouble("kX");
				boneData.kY = (float) boneDataObj.getDouble("kY");
				boneData.arrow_x = (float) boneDataObj.getDouble("arrow_x");
				boneData.arrow_y = (float) boneDataObj.getDouble("arrow_y");
				boneData.effectbyskeleton = boneDataObj
						.getBoolean("effectbyskeleton");

				JSONArray displayDataArray = boneDataObj
						.getJSONArray("display_data");
				for (int l = 0; l < displayDataArray.size(); l++) {
					JSONObject displayDataObj = displayDataArray
							.getJSONObject(l);

					DisplayData displayData = new DisplayData();

					boneData.displayDataVector.add(displayData);

					displayData.displayType = displayDataObj
							.getInt("displayType");

					if (displayData.displayType == 0) {
						displayData.name = displayDataObj.getString("name");

						Bitmap image = BitmapManager
								.getImageFromAssetsFile(pngPath
										+ displayData.name);

						// Log.e("image name", frameData.name);

						if (image == null) {
							Log.e("Error!!", "Bitmap can not found in foldar!");
						}

						displayData.bitmap = image;

						plistManager.bitmapMap.put(displayData.name, image);

						JSONArray skinDataArray = displayDataObj
								.getJSONArray("skin_data");
						for (int m = 0; m < skinDataArray.size(); m++) {
							JSONObject skinDataObj = skinDataArray
									.getJSONObject(m);

							SkinData skinData = new SkinData();
							// displayData.skinDataVecotor.add(skinData);
							displayData.skinData = skinData;

							skinData.x = (float) skinDataObj.getDouble("x");
							skinData.y = (float) skinDataObj.getDouble("y");
							skinData.cX = (float) skinDataObj.getDouble("cX");
							skinData.cY = (float) skinDataObj.getDouble("cY");
							skinData.kX = (float) skinDataObj.getDouble("kX");
							skinData.kY = (float) skinDataObj.getDouble("kY");
						}
					}
				}
			}
		}

		JSONArray animationDataArray = jsonObject
				.getJSONArray("animation_data");
		for (int j1 = 0; j1 < animationDataArray.size(); j1++) {
			JSONObject animationDataObj = animationDataArray.getJSONObject(j1);

			armature.animationData.name = animationDataObj.getString("name");

			JSONArray moveDataArray = animationDataObj.getJSONArray("mov_data");
			for (int j = 0; j < moveDataArray.size(); j++) {
				JSONObject moveDataObj = moveDataArray.getJSONObject(j);

				MoveData moveData = new MoveData();
				moveData.name = moveDataObj.getString("name");
				armature.animationData.moveDataMap.put(moveData.name,
						moveData);
				armature.animationData.moveDataVector.add(moveData);

				moveData.dr = moveDataObj.getInt("dr");
				moveData.lp = moveDataObj.getBoolean("lp");
				moveData.to = moveDataObj.getInt("to");
				moveData.drTW = moveDataObj.getInt("drTW");
				moveData.twE = moveDataObj.getInt("twE");
				moveData.sc = (float) moveDataObj.getDouble("sc");

				JSONArray moveBoneDataArray = moveDataObj
						.getJSONArray("mov_bone_data");
				for (int k = 0; k < moveBoneDataArray.size(); k++) {
					JSONObject moveBoneDataObj = moveBoneDataArray
							.getJSONObject(k);

					MoveBoneData moveBoneData = new MoveBoneData();
					moveBoneData.name = moveBoneDataObj.getString("name");
					moveData.moveDataVector.add(moveBoneData);

					moveBoneData.dl = (float) moveBoneDataObj.getDouble("dl");

					JSONArray frameDataArray = moveBoneDataObj
							.getJSONArray("frame_data");
					for (int l = 0; l < frameDataArray.size(); l++) {
						JSONObject frameDataObj = frameDataArray
								.getJSONObject(l);

						FrameData frameData = new FrameData();
						moveBoneData.frameDataVector.add(frameData);

						frameData.dI = frameDataObj.getInt("dI");
						frameData.x = (float) frameDataObj.getDouble("x");
						frameData.y = (float) frameDataObj.getDouble("y");
						frameData.z = (float) frameDataObj.getDouble("z");
						frameData.cX = (float) frameDataObj.getDouble("cX");
						frameData.cY = (float) frameDataObj.getDouble("cY");
						frameData.kX = (float) frameDataObj.getDouble("kX");
						frameData.kY = (float) frameDataObj.getDouble("kY");
						frameData.fi = frameDataObj.getInt("fi");
						frameData.twE = frameDataObj.getInt("twE");
						frameData.tweenFrame = frameDataObj
								.getBoolean("tweenFrame");

						if (frameDataObj.containsKey("color")) {
							frameData.isColor = true;
							JSONObject colorJsonObject = frameDataObj
									.getJSONObject("color");
							frameData.alpha = colorJsonObject.getInt("a");
							frameData.red = colorJsonObject.getInt("r");
							frameData.green = colorJsonObject.getInt("g");
							frameData.blue = colorJsonObject.getInt("b");
						} else {
							frameData.isColor = false;
						}
					}
				}
			}
		}

		JSONArray textureDataArray = jsonObject.getJSONArray("texture_data");
		for (int j = 0; j < textureDataArray.size(); j++) {
			JSONObject textureDataObj = textureDataArray.getJSONObject(j);

			TextureData textureData = new TextureData();
			textureData.name = textureDataObj.getString("name");
			armature.textureDataVector.put(textureData.name, textureData);

			textureData.width = (float) textureDataObj.getDouble("width");
			textureData.height = (float) textureDataObj.getDouble("height");
			textureData.pX = (float) textureDataObj.getDouble("pX");
			textureData.pY = (float) textureDataObj.getDouble("pY");
			textureData.plistFile = textureDataObj.getString("plistFile");

			float anchorX = textureData.pX * textureData.width;
			float anchorY = (1 - textureData.pY) * textureData.height;

			if (textureDataObj.containsKey("contour_data")) {
				JSONArray contourDataArray = textureDataObj
						.getJSONArray("contour_data");
				if (contourDataArray.size() > 0) {
					JSONObject contourDataObject = contourDataArray
							.getJSONObject(0);

					JSONArray vertexArray = contourDataObject
							.getJSONArray("vertex");

					textureData.contourData.x = new float[vertexArray.size()];
					textureData.contourData.y = new float[vertexArray.size()];
					for (int j1 = 0; j1 < vertexArray.size(); j1++) {
						JSONObject vertex = vertexArray.getJSONObject(j1);
						// Direct compute vertex real position.
						textureData.contourData.x[j1] = (float) vertex
								.getDouble("x");
						textureData.contourData.y[j1] = (float) vertex
								.getDouble("y");

						textureData.contourData.x[j1] += anchorX;

						textureData.contourData.y[j1] = -textureData.contourData.y[j1];
						textureData.contourData.y[j1] += anchorY;
					}
				}
			}
		}

		String tempString = jsonObject.getString("config_file_path");
		armature.configFilePathVector.add(tempString);

		// add texture data
		for (int i = 0; i < armature.armatureData.boneDatas.size(); i++) {
			BoneData boneData = armature.armatureData.boneDatas.get(i);
			boneData.index = i;

			// add parent bone
			if (!"".equals(boneData.parent)) {
				boneData.parentBoneData = armature.armatureData.boneDataArray
						.get(boneData.parent);
			}

			for (int j = 0; j < boneData.displayDataVector.size(); j++) {
				DisplayData displayData = boneData.displayDataVector.get(j);

				if (displayData.displayType == 0) {
					String pngNameString = displayData.name;
					int lastIndex = pngNameString.lastIndexOf('.');
					pngNameString = pngNameString.substring(0, lastIndex);

					TextureData textureData = armature.textureDataVector
							.get(pngNameString);

					displayData.textureData = textureData;

					// no need
					// displayData.bitmap = plistManager.bitmapMap
					// .get(displayData.name);
				}
			}
		}

		for (String tempString2 : armature.animationData.moveDataMap
				.keySet()) {
			MoveData moveData = armature.animationData.moveDataMap
					.get(tempString2);
			for (int i = 0; i < moveData.moveDataVector.size(); i++) {
				MoveBoneData moveBoneData = moveData.moveDataVector.get(i);
				moveBoneData.boneData = armature.armatureData.boneDataArray
						.get(moveBoneData.name);

				// Add parent move bone data to move bone data.
				if (moveBoneData.boneData.parentBoneData != null) {
					String parentMoveBoneDataName = moveBoneData.boneData.parentBoneData.name;
					for (int j = 0; j < moveData.moveDataVector.size(); j++) {
						MoveBoneData parentMoveBoneData = moveData.moveDataVector
								.get(j);
						if (parentMoveBoneDataName
								.equals(parentMoveBoneData.name)) {
							moveBoneData.parentMoveBoneData = parentMoveBoneData;
							break;
						}
					}
				}
			}
		}
	}
}
