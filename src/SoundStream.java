import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;


public class SoundStream extends FilterInputStream {

	private static final int UNKNOWN_SIZE = -1;
	
	private SoundFilter filter;
	private int remainingSize;
	
	public SoundStream(InputStream stream, SoundFilter filter) {
		super(stream);
		this.filter = filter;
		remainingSize = UNKNOWN_SIZE;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int readed = super.read(b, off, len);
		if (readed > 0) {
			filter.filter(b, off, readed);
			return readed;
		}
		
		if (UNKNOWN_SIZE == remainingSize) {
			remainingSize = filter.getRemainSize();
			remainingSize = remainingSize / 4 * 4;
		}
		
		if (remainingSize > 0) {
			len = Math.min(len, remainingSize);
			for (int i = off; i < off+len; b[i++] = 0) 
				;
			
			filter.filter(b, off, len);
			remainingSize -= len;
			
			return len;
		}
		
		return UNKNOWN_SIZE;
	}
}
