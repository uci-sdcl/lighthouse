package edu.uci.lighthouse.ui.graph;

import java.util.Collection;

import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseField;
import edu.uci.lighthouse.model.LighthouseMethod;

public interface IUmlClass {
	public Collection<LighthouseMethod> getMethods();
	public Collection<LighthouseField> getFields();
	public static enum LEVEL {ONE, TWO, THREE, FOUR};
}
