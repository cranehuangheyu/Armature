package com.dsy.ActionBoneParse.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import android.R.string;
import android.graphics.Bitmap;
import android.util.Log;

import com.dsy.ActionBoneParse.plist.FrameData;
import com.dsy.ActionBoneParse.plist.PlistData;
import com.dsy.ActionBoneParse.plist.PlistHandler;
import com.dsy.ActionBoneParse.util.AndroidUtil;
import com.dsy.ActionBoneParse.util.BitmapManager;

public class PlistManager {
		
	public Map<String, Bitmap> bitmapMap;
	
	public PlistData plistData;
	
	public PlistManager(String plistPath, String pngPath)
	{
		bitmapMap = new HashMap<String, Bitmap>();
		
		plistData = new PlistData();
		
		try {
			XMLReader xr;
			xr = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
			
			// ∞≤◊∞ContentHandler£Æ£Æ£Æ 
			xr.setContentHandler( new PlistHandler(plistData) ); 

			// Ω‚ŒˆŒƒº˛£Æ£Æ£Æ 
//			InputStream inputStream = AndroidUtil.getResourceAsStream("DemoPlayer/Export/DemoPlayer/NewAnimation0.plist");
			InputStream inputStream = AndroidUtil.getResourceAsStream("DemoPlayer/Export/DemoPlayer0.plist");

			try {
//				xr.setFeature( "http://xml.org/sax/features/validation", false); 
//				//设置解析器不验证命名空间，(即忽略外部的stylesheet，xls文档)
//				xr.setFeature( "http://xml.org/sax/features/namespaces", false); 
				
				xr.setFeature(
					    "http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
				xr.setFeature(
					    "http://xml.org/sax/features/validation",false);

				xr.parse( new InputSource( 
						inputStream) );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.e("image name size", plistData.frames.size() + "");
			
		for (String key : plistData.frames.keySet()) {
			FrameData frameData = plistData.frames.get(key);
			
			Bitmap image = BitmapManager.getImageFromAssetsFile(pngPath + frameData.name);
			
			Log.e("image name", frameData.name);
			
			bitmapMap.put(frameData.name, image);
		}		
	}
}
