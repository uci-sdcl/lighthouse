package edu.uci.lighthouse.lighthouseqandathreads;

import java.util.Observer;

import org.eclipse.swt.widgets.Composite;

public interface IController<T> extends Observer{

	public IPersistAndUpdate getPersisterAndUpdater();
	public void setPersisterAndUpdater(IPersistAndUpdate persisterAndUpdater);
	public Composite getView();
	public T getModel();
	public void init();
}
