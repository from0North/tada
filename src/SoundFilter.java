
abstract public class SoundFilter {
	
	public static short getSample(byte[] buffer, int position) {
		return (short)(((buffer[position+1] & 0xFF) << 8) |
						(buffer[position] & 0xFF));
	}
	
	public static void setSample(byte[] buffer, int position, short sample) {
		buffer[position] = (byte)(sample & 0xFF);
		buffer[position+1] = (byte)((sample >> 8) & 0xFF);
	}
	
	abstract public void filter(byte[] samples, int offset, int length);
	
	public void filter(byte[] samples) {
		filter(samples, 0, samples.length);
	}
	
	public void reset() {
		
	}
	
	public int getRemainSize() {
		return 0;
	}
	
	
}
