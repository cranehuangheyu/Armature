package javax.microedition.rms;
public interface RecordComparator {
	static int EQUIVALENT = 0;
	static int FOLLOWS = 1;
	static int PRECEDES = -1;
	public int compare(byte[] rec1, byte[] rec2);
}
