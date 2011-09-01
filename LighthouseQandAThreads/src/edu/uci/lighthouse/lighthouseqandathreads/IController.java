package edu.uci.lighthouse.lighthouseqandathreads;

import java.util.Observer;

import org.eclipse.swt.widgets.Composite;

public interface IController<T> extends Observer{

	public PersistAndUpdate getPersisterAndUpdater();
	public void setPersisterAndUpdater(PersistAndUpdate persisterAndUpdater);
	public Composite getView();
	public T getModel();
	public void init();
}
