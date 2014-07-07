package com.dsy.ActionBoneParse.plist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class PlistHandler extends DefaultHandler {
	
	public static final int STATE_NONE = 0;
	public static final int STATE_FRAME = 1;
	public static final int STATE_IN_FRAME = 2;
	public static final int STATE_MAPNAME = 3;
	public static final int STATE_CONTENT1 = 4;
	public static final int STATE_CONTENT2 = 5;
	public static final int STATE_OUT_FRAME = 6;
	
	private LinkedList<Object> list = new LinkedList<Object>();
	
	private boolean isRootElement = false;
	
	private boolean keyElementBegin = false;
	
	private String key;
	
	private StringBuffer value = new StringBuffer();
	
	private boolean valueElementBegin = false;
	
//	private Map<String, Map<String, String>> framesMap = new HashMap<String, Map<String,String>>();
	
	private Object root;
	
	public int state = STATE_NONE;
	public static final int STATE1_NONE = 0;
	public static final int STATE1_FRAMES = 1;
	public static final int STATE1_METADATA = 2;
	public static final int STATE1_TEXTURE = 3;
	
//	public String mapName;
//	public Map<String, String> mapTemp = new HashMap<String, String>();
//	public String str1;
//	public String str2;
	
	public PlistData plistData;
	
	public int state1 = 0;
	
	public String tempString = "";
	public String tempString1 = "";
	
	public FrameData frameData;
		
	public PlistHandler(PlistData plistData)
	{
		this.plistData = plistData;
	}
			
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		Log.e("sax--handler", "start-" + localName);
		
		int length = attributes.getLength();
		for (int i = 0; i < length; i++) {
			Log.e("sax--handler", attributes.getLocalName(i) + "-" + attributes.getValue(i));
		}
		
//		if (state1 == STATE1_FRAMES) {
//			if (state == STATE_FRAME) {
//				if ("dict".equals(localName)) {
//					state = STATE_IN_FRAME;
//				}
//			}
//			
//			if (state == STATE_IN_FRAME) {
//				if ("key".equals(localName)) {
//					state = STATE_MAPNAME;
//				}
//			}
//			
//			if (state == STATE_MAPNAME) {
//				if ("dict".equals(localName)) {
//					state = STATE_CONTENT1;
//				}
//			}
//		}
	}

	//@SuppressWarnings("unchecked")
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		value.delete(0, value.length());
		
		if (length > 0) {
			value.append(ch, start, length);
		}
		
		Log.e("sax--characters", value.toString());
		
		if (value.toString().lastIndexOf('\n') > -1 ||
				value.toString().lastIndexOf('\t') > -1) {
			return ;
		}
		
		if (state1 == STATE1_FRAMES) {
			if ("".equals(tempString)) {
				// get png name
				tempString = value.toString();
				frameData = new FrameData();
				frameData.name = tempString;
			} else {
				if ("".equals(tempString1)) {
					tempString1 = value.toString();
				} else if ("width".equals(tempString1)) {
					frameData.width = Integer.parseInt(value.toString());
					tempString1 = "";
				} else if ("height".equals(tempString1)) {
					frameData.height = Integer.parseInt(value.toString());
					tempString1 = "";
				} else if ("originalWidth".equals(tempString1)) {
					frameData.originalWidth = Integer.parseInt(value.toString());
					tempString1 = "";
				} else if ("originalHeight".equals(tempString1)) {
					frameData.originalHeight = Integer.parseInt(value.toString());
					tempString1 = "";
				} else if ("x".equals(tempString1)) {
					frameData.x = Integer.parseInt(value.toString());
					tempString1 = "";
				} else if ("y".equals(tempString1)) {
					frameData.y = Integer.parseInt(value.toString());
					tempString1 = "";
				} else if ("offsetX".equals(tempString1)) {
					frameData.offsetX = Integer.parseInt(value.toString());
					tempString1 = "";
				} else if ("offsetY".equals(tempString1)) {
					frameData.offsetY = Integer.parseInt(value.toString());
					tempString1 = "";
					
					plistData.frames.put(tempString, frameData);
					tempString = "";
					frameData = null;
				}
			}
		}
		
		if (state1 == STATE1_METADATA) {
			if ("".equals(tempString)) {
				tempString = value.toString();
			} else if ("format".equals(tempString)) {
				plistData.format = Integer.parseInt(value.toString());
				tempString = "";
			} else if ("textureFileName".equals(tempString)) {
				plistData.textureFileName = value.toString();
				tempString = "";
			} else if ("realTextureFileName".equals(tempString)) {
				plistData.realTextureFileName = value.toString();
				tempString = "";
			} else if ("size".equals(tempString)) {
				plistData.size = value.toString();
				tempString = "";
			}
		}
		
		if (state1 == STATE1_TEXTURE) {
			if ("".equals(tempString)) {
				tempString = value.toString();
			} else if ("width".equals(tempString)) {
				plistData.width = Integer.parseInt(value.toString());
				tempString = "";
			} else if ("height".equals(tempString)) {
				plistData.height = Integer.parseInt(value.toString());
				tempString = "";
			}
		}
		
		if ("frames".equals(value.toString())) {
			state1 = STATE1_FRAMES;
			tempString = "";
		} else if ("metadata".equals(value.toString())) {
			state1 = STATE1_METADATA;
			tempString = "";
		} else if ("texture".equals(value.toString())) {
			state1 = STATE1_TEXTURE;
			tempString = "";
		}
						
//		Log.e("sax--characters", ch.toString());  
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
		Log.e("sax--handler", "end-" + localName);
		
//		if (state1 == STATE1_FRAMES) {
//			if (state == STATE_CONTENT1) {
//				if ("dict".equals(localName)) {
//					state = STATE_MAPNAME;
//					framesMap.put(mapName, mapTemp);
//				}
//			}
//			
//			if (state == STATE_MAPNAME) {
//				if ("dict".equals(localName)) {
//					state = STATE_NONE;
//				}
//			}
//		}
		
	}
}
