package ai.nd.abap;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;

public class Settings {
	
	private static final String PLUGIN_ID = "ai.nd.abap.helper";
	public static final String Version = "0.1 beta, release 240426";

	private static Settings instance;

	private String apiKey = "";
	private String apiURI = "";
	private String selectedModel = "";
	private String maxToken = "";

	private Settings() {
		super();
	}

	public static Settings getInstance() {
		if (instance == null) {
			instance = new Settings();
			instance.loadSettings();
		}
		return instance;
	}

	public void rememberSettings() {

		IPreferenceStore store = new ScopedPreferenceStore(InstanceScope.INSTANCE, PLUGIN_ID);
		store.setValue("apiKey", this.apiKey);
		store.setValue("apiURI", this.apiURI);
		store.setValue("selectedModel", this.selectedModel);
		store.setValue("maxToken", this.getMaxToken());

	}

	public void saveSettings() {
		
		this.rememberSettings();

		IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(PLUGIN_ID);
		preferences.put("apiKey", this.apiKey);
		preferences.put("apiURI", this.apiURI);
		preferences.put("selectedModel", this.selectedModel);
		preferences.put("maxToken", this.getMaxToken());

        try {
			preferences.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		} 	

	}

	public void loadSettings() {
		
		IPreferenceStore store = new ScopedPreferenceStore(InstanceScope.INSTANCE, PLUGIN_ID);
		this.apiKey = store.getString("apiKey");
		this.apiURI = store.getString("apiURI");
		this.selectedModel = store.getString("selectedModel");
		this.setMaxToken(store.getString("maxToken"));
		getDefaultsForEmptyValues();

	}
	
	private void getDefaultsForEmptyValues() {
		if (this.apiURI == "" ) {
			this.apiURI = "https://api.openai.com/v1/chat/completions";
		}
		if (this.selectedModel == "" ) {
			this.selectedModel = "gpt-3.5-turbo-0125";
		}
		if (this.getMaxToken() == "" ) {
			this.setMaxToken("300");
		}
	}
	
	public void initSettings(Boolean setDefaults) {
		apiKey = "";
		apiURI = "";
		selectedModel = "";
		setMaxToken("");		
		if (setDefaults == true) {
			this.getDefaultsForEmptyValues();
		}
	}
	
	public void initSettings() {
		initSettings(false);
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSelectedModel() {
		return selectedModel;
	}

	public void setSelectedModel(String selectedModel) {
		this.selectedModel = selectedModel;
	}

	public String getApiURI() {
		return apiURI;
	}

	public void setApiURI(String apiURI) {
		this.apiURI = apiURI;
	}

	public String getMaxToken() {
		return maxToken;
	}

	public void setMaxToken(String maxToken) {
		this.maxToken = maxToken;
	}
}
