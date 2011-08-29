package edu.uci.lighthouse.lighthouseqandathreads;

import java.util.Observer;

import org.eclipse.swt.widgets.Composite;

public interface IController<T> extends Observer{

	public Composite getView();
	public T getModel();
	public void init();
}
