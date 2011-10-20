package edu.uci.lighthouse.views.filters;

import java.io.Serializable;

public interface IDataFilter extends IFilter{
	public Serializable getData();
	
	public void setData(Serializable data);
}
