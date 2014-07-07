package javax.microedition.rms;
import static android.provider.BaseColumns._ID;

import com.dsy.ActionBoneParse.ArmatureActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RecordStore {
	public static final int AUTHMODE_ANY = 1;
	public static final int AUTHMODE_PRIVATE = 0;
	private SQLiteDatabase db ;  //鏁版嵁搴�
	private String table_name;
//	private static RecordStore rs = null;
	/**
	 * 鏋勯�鏂规硶
	 */
	private RecordStore(){
		
	}
	
	/**
	 * 鍗曚緥妯″紡
	 * @return
	 */
//	private static RecordStore getInstance(){
//		if(rs == null){
//			rs = new RecordStore();
//		}
//		return rs;
//	}

	public int addRecord(byte[] data, int offset, int numBytes)
		throws RecordStoreNotOpenException,RecordStoreException,RecordStoreFullException
	{	
		int result = -1;
		
		byte[] datarecord = new byte[numBytes];
		for(int i=offset,j=0;i<numBytes+offset;i++,j++){
			datarecord[j]= data[i];
		}
		ContentValues values = new ContentValues();
		values.put("content", datarecord);
		
		result = (int)db.insert(table_name, null, values);
		
		return result;
	}
	
	public void closeRecordStore()
		throws RecordStoreNotOpenException, RecordStoreException
	{
		if(db != null && db.isOpen()){
			db.close();
		}else{
			new RecordStoreNotOpenException("RecordStore is not open"); 
		}
	}
	
	public void deleteRecord(int recordId)
		throws RecordStoreNotOpenException,InvalidRecordIDException,RecordStoreException
	{
		String sql = "delete from "+table_name+" where _ID="+recordId;
		db.execSQL(sql);
		
	}
	
	public static void deleteRecordStore(String recordStoreName)
		throws RecordStoreException,RecordStoreNotFoundException
	{
		ArmatureActivity.getContextInstance().deleteDatabase(recordStoreName);
	}
	
	public RecordEnumeration enumerateRecords(RecordFilter filter, RecordComparator comparator ,boolean keepUpdated)
		throws RecordStoreNotOpenException
	{
		RecordEnumeration record_enumeration = new RecordEnumeration();

		Cursor cursor = db.query(table_name, null, null, null, null, null, null);
		ArmatureActivity.getInstance().startManagingCursor(cursor);
		record_enumeration.cursor = cursor;
		record_enumeration.old_cursor = cursor;

		return record_enumeration;
	}

	public String getName()
		throws RecordStoreNotOpenException
	{
		return table_name;
	}
	
	public int getNextRecordID()
		throws RecordStoreNotOpenException , RecordStoreException
	{
		int result = -1;
		Cursor cursor = db.query(table_name, null, null, null, null, null, null);
		ArmatureActivity.getInstance().startManagingCursor(cursor);
		if(cursor.moveToLast()){
			result = cursor.getInt(0)+1;
		}
		return result;
		
	}
	
	public int getNumRecords()
		throws RecordStoreNotOpenException
	{
		int result = 0;
		Cursor cursor = db.query(table_name, null, null, null, null, null, null);
		ArmatureActivity.getInstance().startManagingCursor(cursor);
		result = cursor.getCount();
		return result;
	}
	
	public byte[] getRecord(int recordId)
		throws RecordStoreNotOpenException,InvalidRecordIDException,RecordStoreException
	{
		Cursor cursor = db.query(table_name, null, _ID+"="+recordId, null, null, null, null);
		ArmatureActivity.getInstance().startManagingCursor(cursor);
		if(cursor.moveToNext()){
			byte[] data = cursor.getBlob(1);
			return data;
		}
		
		return null;
	}
	
	public int getRecord(int recordId, byte[] buffer, int offset)
		throws RecordStoreNotOpenException,InvalidRecordIDException,RecordStoreException
	{
		int result = -1;
		Cursor cursor = db.query(table_name, null, _ID+"="+recordId, null, null, null, null);
		ArmatureActivity.getInstance().startManagingCursor(cursor);
		if(cursor.moveToNext()){
			byte[] data = cursor.getBlob(1);
			for(int i=offset ,j=0;i<data.length;i++,j++){
				buffer[j] = data[i];
			}
			if(data!= null){
				result = recordId;
			}
		}
		
		return result;
	}
	
	public int getRecordSize(int recordId)
		throws RecordStoreNotOpenException,InvalidRecordIDException,RecordStoreException
	{
		int result = -1;
		Cursor cursor = db.query(table_name, null, _ID+"="+recordId, null, null, null, null);
		ArmatureActivity.getInstance().startManagingCursor(cursor);
		if(cursor.moveToNext()){
			byte[] data = cursor.getBlob(1);
			result = data.length;
		}

		return result;
	}

	
	/**
	 * 鎵撳紑RMS  
	 * @param recordStoreName
	 * @param createIfNecessary:=true,鏈夊垯鎵撳紑锛屾病鏈夊氨鍒涘缓;=false ,鏈夊氨鎵撳紑锛屾病鏈夋姏鍑篟ecordStoreNotFoundException
	 * @return
	 */
	public static RecordStore openRecordStore(String recordStoreName, boolean createIfNecessary)
		throws RecordStoreException,RecordStoreFullException,RecordStoreNotFoundException
	{
		/**
		 * j2me涓�
		 *  public static RecordStore rs;
		 *  rs = RecordStore.openRecordStore("aa", false);
		 *  rs = RecordStore.openRecordStore("bb", false);
		 *  姝ゆ椂锛屼笂闈㈢殑aa浼氳嚜鍔ㄥ叧闂紝鎵�互android涓妸RecordStore璁剧疆涓哄崟渚嬫ā寮�
		 */
//		RecordStore rs = getInstance();  
//		if(rs.db != null && rs.db.isOpen()){
//			rs.db.close();
//		}
		RecordStore rs = new RecordStore();
		rs.table_name = recordStoreName;
		rs.db = ArmatureActivity.getContextInstance().openOrCreateDatabase(recordStoreName, Context.MODE_PRIVATE, null);
		
		try{
			rs.db.query(recordStoreName, null, null, null, null, null, null);
		}catch(Exception e){
			if(!createIfNecessary){
				if(rs.db.isOpen()){
					rs.db.close();
				}
				throw new RecordStoreException();
			}else{
				//鍒涘缓琛�
				Log.d("RMS","no table and create table");
				String sql = "create table "+ recordStoreName +"("+_ID		
							+" integer primary key autoincrement,content text not null);";
				rs.db.execSQL(sql);
				return rs;
			}
			
		}
		return rs;
	}
	
	public void setRecord(int recordId, byte[] newData, int offset, int numBytes)
		throws RecordStoreNotOpenException,InvalidRecordIDException,RecordStoreException,RecordStoreFullException
	{
		if(getRecord(recordId) == null){
			throw new InvalidRecordIDException("recordId is invalid"); //璁板綍涓嶅瓨鍦�
		}else{
			byte[] datarecord = new byte[numBytes];
			for(int i=offset,j=0;i<numBytes+offset;i++,j++){
				datarecord[j]= newData[i]; 
			}
			ContentValues values = new ContentValues();
			values.put("content", datarecord);
			db.update(table_name, values, " _ID="+recordId, null);
		}
	}
	
//	public void addRecordListener(RecordListener listener){
//	
//}
	
//	public long getLastModified()
//	throws RecordStoreNotOpenException
//{
//	return 0;
//}
	
//	public int getSize()
//	throws RecordStoreNotOpenException
//{
//	return 0;
//}
	
	
	/**
	 * 娴嬭瘯db.getMaximumSize() 杩斿洖long绫诲瀷锛屽�涓�099511626752 瓒呰繃浜唅nt鏈�ぇ鑼冨洿2147483647
	 */
	private static final long INT_MAX = 2147483647;
	public int getSizeAvailable()
		throws RecordStoreNotOpenException
	{
		long sizeavailable = db.getMaximumSize();
		if(sizeavailable > INT_MAX){
			return (int)INT_MAX;
		}else{
			return (int)sizeavailable;
		}
	}
	
	
//	public int getVersion()
//		throws RecordStoreNotOpenException
//	{
//		return db.getVersion();
//	}
	
//	public static String[] listRecordStores(){
//		return null;
//	}
	
//	public static RecordStore openRecordStore(String recordStoreName, boolean createIfNecessary, int authmode, boolean writable)
//	throws RecordStoreException,RecordStoreFullException,RecordStoreNotFoundException
//{
//	return null;
//}
//
//public static RecordStore openRecordStore(String recordStoreName, String vendorName, String suiteName)
//	throws RecordStoreException, RecordStoreNotFoundException
//{
//	return null;
//}
//
//public void removeRecordListener(RecordListener listener){
//	
//}
//
//public void setMode(int authmode, boolean writable)
//	throws RecordStoreException
//{
//	
//}
	
}
