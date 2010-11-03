package edu.uci.lighthouse.views.filters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class FilterBookmark implements Serializable{

	private static final long serialVersionUID = -8538544450864787254L;

	private String name;
	
	private List<Entry> entries = new LinkedList<Entry>();

	public FilterBookmark(String name, Collection<IFilter> filters) {
		this.name = name;
		populate(filters);
	}

	public String getName() {
		return name;
		
	}
	
	public Collection<IFilter> getFilters() {
		List<IFilter> result = new LinkedList<IFilter>();
		for (Entry entry : entries) {
			try {
				IFilter filter = entry.filter.newInstance();
				if (filter instanceof IDataFilter) {
					((IDataFilter) filter).setData(entry.data);
				}
				result.add(filter);
			} catch (Exception e) {
	
			} 
		}
		return result;
	}
	
	private void populate(Collection<IFilter> filters) {
		for (IFilter iFilter : filters) {
			Serializable data = null;
			if (iFilter instanceof IDataFilter){
				data = getCopy(((IDataFilter) iFilter).getData());
			}
			entries.add(new Entry(iFilter.getClass(), data));
		}
	}
	
	private Serializable getCopy(Serializable obj){
		Serializable result = null;
	        try {
	            ByteArrayOutputStream bos = new ByteArrayOutputStream();
	            ObjectOutputStream out = new ObjectOutputStream(bos);
	            out.writeObject(obj);
	            out.flush();
	            out.close();

	            ObjectInputStream in = new ObjectInputStream(
	                new ByteArrayInputStream(bos.toByteArray()));
	            result = (Serializable) in.readObject();
	        }
	        catch(Exception e) {
	        }
	        return result;
	}
	
	private final class Entry implements Serializable{
		private static final long serialVersionUID = 8619988077763868239L;
		Class<? extends IFilter> filter;
		Serializable data;
		public Entry(Class<? extends IFilter> class1, Serializable data) {
			this.filter = class1;
			this.data = data;
		}
	}
}
