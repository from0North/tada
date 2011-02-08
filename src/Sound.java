import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


public class Sound {

	private AudioFormat format;
	private byte[] samples;
	
	public Sound(final String fileName) {
		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(new File(fileName));
			format = stream.getFormat();
			samples = getSamples(stream);
		} catch (Exception e) { e.printStackTrace(); }
		
	}
	
	private byte[] getSamples(AudioInputStream stream) {
		int length = (int)(stream.getFrameLength() * stream.getFormat().getFrameSize());
		
		byte[] samples = new byte[length];
		DataInputStream dataStream = new DataInputStream(stream);
		
		try {
			dataStream.readFully(samples);
		} catch (IOException e) { e.printStackTrace(); }
		
		return samples;
	}
	
	public void play() {
		int size = format.getFrameSize()*(int)(format.getSampleRate()/10);
		byte[] buffer = new byte[size];
		
		SourceDataLine line;
		try {
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(format, size);
		} catch (LineUnavailableException e) { e.printStackTrace(); return; }
		
		InputStream stream = new ByteArrayInputStream(samples);
		
		line.start();
		
		try {
			int count = 0;
			while (count != -1) {
				count = stream.read(buffer, 0, buffer.length);
				if (count != -1) {
					line.write(buffer, 0, count);
				}
			}
		} catch (IOException e) { e.printStackTrace(); }
		
		line.drain();
		line.close();
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: Sound <file-name>");
			System.exit(0);
		}
		
		Sound sound = new Sound(args[0]);
		sound.play();
		
		System.out.println("DONE.");
		System.exit(0);
	}

}
