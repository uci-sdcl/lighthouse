package edu.uci.lighthouse.model.util;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import edu.uci.lighthouse.model.LighthouseAbstractModel;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseField;
import edu.uci.lighthouse.model.LighthouseMethod;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.LighthouseRelationship.TYPE;

public class ModelWriter {

	public PrintWriter out;
	public LighthouseAbstractModel model;

	public ModelWriter(OutputStream stream) {
		this.out = new PrintWriter(stream);
	}

	public void write(LighthouseAbstractModel model) {
		this.model = model;
		for (LighthouseClass c : model.getAllClasses()) {
			write(c);
			Collection<LighthouseEntity> ma = model
					.getMethodsAndAttributesFromClass(c);
			// print all fields
			out.println("fields:");
			for (LighthouseEntity e : ma) {
				if (e instanceof LighthouseField) {
					write(e);
				}
			}
			// print all methods
			out.println("methods:");
			for (LighthouseEntity e : ma) {
				if (e instanceof LighthouseMethod) {
					write(e);
				}
			}
			out.println();
		}
	}
	
	public void writeEvents(LighthouseModel model) {
		out.println("Events:");
		for (LighthouseEvent event: model.getListEvents()) {
			edu.uci.lighthouse.model.LighthouseEvent.TYPE type = event.getType();
			Object artifact = event.getArtifact();
			Date time = event.getTimestamp();
			LighthouseAuthor user = event.getAuthor();
			out.println("type: " + type + " | artifact: " + artifact.toString() + " | time: " + time + " | user: " + user.getName());
		}
	}

	private void write(LighthouseEntity e) {
		if (e instanceof LighthouseClass) {
			write((LighthouseClass) e);
		} else if (e instanceof LighthouseField) {
			write((LighthouseField) e);
		} else if (e instanceof LighthouseMethod) {
			write((LighthouseMethod) e);
		}
	}

	private void write(LighthouseClass aClass) {
		String fqn = aClass.getFullyQualifiedName();
		String modifiers = getModifiers(aClass);
		out.println("| " + modifiers + " " + fqn + " |");
		List<LighthouseRelationship> listRelationships = getRelationships(aClass);
		for (LighthouseRelationship lighthouseRelationship : listRelationships) {
			write(lighthouseRelationship);
		}
	}

	private void write(LighthouseField field) {
		String fqn = field.getFullyQualifiedName();
		String modifiers = getModifiers(field);
		String type = getType(field);
		out.println("\t " + modifiers + " " + type + " " + fqn);
		List<LighthouseRelationship> listRelationships = getRelationships(field);
		for (LighthouseRelationship lighthouseRelationship : listRelationships) {
			write(lighthouseRelationship);
		}
	}

	private void write(LighthouseMethod method) {
		String fqn = method.getFullyQualifiedName();
		String modifiers = getModifiers(method);
		String type = getType(method);
		out.println("\t " + modifiers + " " + type + " " + fqn);
		List<LighthouseRelationship> listRelationships = getRelationships(method);
		for (LighthouseRelationship lighthouseRelationship : listRelationships) {
			write(lighthouseRelationship);
		}
	}

	private void write(LighthouseRelationship r) {
		String from = r.getFromEntity().getFullyQualifiedName();
		TYPE typeOfRelationship = r.getType();
		String to = r.getToEntity().getFullyQualifiedName();
		
		out.println("\t\t " + from + " " + typeOfRelationship + " " + to);
	}
	
	public void flush() {
		out.flush();
	}

	public void close() {
		out.close();
	}

	private List<LighthouseRelationship> getRelationships(LighthouseEntity entity) {
		return (List<LighthouseRelationship>) model.getRelationshipsFrom(entity);
	}

	private String getModifiers(LighthouseEntity entity) {
		String fqn = entity.getFullyQualifiedName();
		List<LighthouseRelationship> listRelationship = (List<LighthouseRelationship>) model.getRelationships();
		String result = "";
		for (LighthouseRelationship lighthouseRelationship : listRelationship) {
			if (TYPE.MODIFIED_BY == lighthouseRelationship.getType()) {
				LighthouseEntity entityFrom = lighthouseRelationship
						.getFromEntity();
				if (fqn.equals(entityFrom.getFullyQualifiedName())) {
					LighthouseEntity entityTo = lighthouseRelationship
							.getToEntity();
					result += entityTo.getFullyQualifiedName() + " ";
				}
			}
		}
		return result;
	}

	private String getType(LighthouseEntity entity) {
		String fqn = entity.getFullyQualifiedName();

		List<LighthouseRelationship> listRelationship = (List<LighthouseRelationship>) model.getRelationships();
		for (LighthouseRelationship lighthouseRelationship : listRelationship) {
			LighthouseEntity entityFrom = lighthouseRelationship
					.getFromEntity();
			if (fqn.equals(entityFrom.getFullyQualifiedName())) {				
				TYPE type = lighthouseRelationship.getType();
				if ((type == TYPE.HOLDS) || (type == TYPE.RETURN)) {
					LighthouseEntity entityTo = lighthouseRelationship
							.getToEntity();
					return entityTo.getFullyQualifiedName();
				}
			}
		}
		return null;
	}

	
}
