package edu.uci.lighthouse.model.util;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;

import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseEvent.TYPE;

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
		out.println("Entities:");
		for (LighthouseEvent event: delta.getEvents()) {
			Integer id = event.getId();
			TYPE type = event.getType();
			Object artifact = event.getArtifact();
			Date time = event.getTimestamp();
			LighthouseAuthor user = event.getAuthor();
			System.out.println("type: " + type + " artifact: " + artifact.toString());
			//System.out.println("id: " + id + " type: " + type + " artifact: " + artifact.toString() + "time: " + time + "user: " + user);
		}
	}
	
}
