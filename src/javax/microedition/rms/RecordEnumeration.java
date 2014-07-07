package javax.microedition.rms;
import android.database.Cursor;

import com.dsy.ActionBoneParse.ArmatureActivity;
public class RecordEnumeration {
	public Cursor cursor = null;
	public Cursor old_cursor = null;
	/**
	 * 閲婃斁宸蹭娇鐢ㄧ殑鍐呴儴璧勬簮
	 */
	public void destroy(){
		checkDestroy();
		cursor = null;
	}

	
	/**
	 * 鑾峰彇鏁版嵁搴撲腑鐨勪笅涓�釜鍙敤id
	 * 杩斿洖鍊�   涓嬩竴涓彲鐢╥d
	 */
	public int nextRecordId(){
		ArmatureActivity.getInstance().startManagingCursor(cursor);
		while(cursor.moveToNext()){
			return (cursor.getInt(0)+1);
		}
		return 0;
	}
	
	/**
	 * 閲嶈enumeration涓哄垵濮嬬姸鎬�
	 */
	public void reset(){
		ArmatureActivity.getInstance().startManagingCursor(cursor);
		ArmatureActivity.getInstance().startManagingCursor(old_cursor);
		checkDestroy();
		if(old_cursor != null){
			cursor = old_cursor;
		}else{
			throw new IllegalStateException();
		}
	}
	//妫�煡鏄惁閲婃斁璧勬簮
	public void checkDestroy(){
		if(cursor == null){
			throw new IllegalStateException("");
		}else{
			return ;
		}
	}
	
//	public boolean hasNextElement(){
//	return false;
//}
//public boolean hasPreviousElement(){
//	return false;
//}
//public boolean isKeptUpdated(){
//	return false;
//}
//public void keepUpdated(boolean keepUpdated){
//	
//}
//public byte[] nextRecord(){
//	return null;
//}
	
//	public int numRecords(){
//		return 0;
//	}
//	public byte[] previousRecord(){
//		return null;
//	}
//	public int previousRecordId(){
//		return 0;
//	}
//	public void rebuild(){
//		
//	}
	
}
