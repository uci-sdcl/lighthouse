package edu.uci.lighthouse.model.util;

import java.io.OutputStream;
import java.io.PrintWriter;

import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseEvent;

public class DeltaWriter {

	public PrintWriter out;

	public DeltaWriter(OutputStream stream) {
		this.out = new PrintWriter(stream);
	}	
	
	public void flush() {
		out.flush();
	}

	public void close() {
		out.close();
	}
	
	public void write(LighthouseDelta delta) {
		out.println("Events:");
		for (LighthouseEvent event: delta.getEvents()) {
			out.println(event);
		}
	}
	
}
