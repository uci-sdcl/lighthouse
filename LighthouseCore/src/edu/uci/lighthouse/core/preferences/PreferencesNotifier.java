package edu.uci.lighthouse.core.preferences;

import java.util.ArrayList;
import java.util.List;

public class PreferencesNotifier {

	private static PreferencesNotifier instance;
	
	private List<IPreferencesChangeListener> listeners = new ArrayList<IPreferencesChangeListener>();

	private PreferencesNotifier() {
	}
	
	public static PreferencesNotifier getInstance() {
		if (instance == null) {
			instance = new PreferencesNotifier();
		}
		return instance;
	}
	
	public void addPreferencesChangeListener(IPreferencesChangeListener listener) {
		listeners.add(listener);
	}
	
	public void removePreferencesChangeListener(IPreferencesChangeListener listener) {
		listeners.remove(listener);
	}
	
	protected void fireUserChanged() {
		for (IPreferencesChangeListener listener : listeners) {
			listener.userChanged();
		}
	}
	
	protected void fireDbSettingsChanged() {
		for (IPreferencesChangeListener listener : listeners) {
			listener.dbSettingsChanged();
		}
	}
}
