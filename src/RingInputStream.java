import java.io.ByteArrayInputStream;
import java.io.IOException;


public class RingInputStream extends ByteArrayInputStream {
	private boolean close = false;
	
	public RingInputStream(byte[] buf) {
		super(buf);
		close = false;
	}

	@Override
	public synchronized int read(byte[] b, int off, int len) {
		if (close) return -1;
		
		int count = 0;
		while (count < len) {
			int readed = super.read(b, off+count, len-count);
			
			if (readed > 0) {
				count += readed;
			} else { reset(); }
			
		}
		
		return count;
	}

	@Override
	public void close() throws IOException {
		super.close();
		close = true;
	}

}
