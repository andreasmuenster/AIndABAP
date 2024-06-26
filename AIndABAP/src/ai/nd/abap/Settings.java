package ai.nd.abap;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class Settings {

	private static final String PLUGIN_ID = "ai.nd.abap.helper";

	private static Settings instance;

	private String apiKey = "";
	private String apiURI = "";
	private String selectedModel = "";
	private String maxToken = "";
	private String explainPrompt = "";
	private String createPrompt = "";

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

	public String getCurrentVersion() {
		Bundle bundle = FrameworkUtil.getBundle(Settings.class);
		if (bundle != null) {
			String version = bundle.getVersion().toString();
			return version;
		} else {
			return "Unknown";
		}
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
		preferences.put("maxToken", this.maxToken);
		preferences.put("createPrompt", this.createPrompt);
		preferences.put("explainPrompt", this.explainPrompt);

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
		this.maxToken = store.getString("maxToken");
		this.createPrompt = store.getString("createPrompt");
		this.explainPrompt = store.getString("explainPrompt");
		getDefaultsForEmptyValues();

	}

	private void getDefaultsForEmptyValues() {
		if (this.apiURI == "") {
			this.apiURI = "https://api.openai.com/v1/chat/completions";
		}
		if (this.selectedModel == "") {
			this.selectedModel = "gpt-3.5-turbo-0125";
		}
		if (this.getMaxToken() == "") {
			this.setMaxToken("300");
		}
		if (this.createPrompt == "") {
			createPrompt = "Please write ABAP Coding for the following request:";
		}
		if (this.explainPrompt == "") {
			this.explainPrompt = "Please explain the following ABAP-Coding:";
		}

	}

	public void initSettings(Boolean setDefaults) {
		apiKey = "";
		apiURI = "";
		selectedModel = "";
		setMaxToken("");
		setExplainPrompt("");
		setCreatePrompt("");
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

	public String getExplainPrompt() {
		return explainPrompt;
	}

	public void setExplainPrompt(String explainPrompt) {
		this.explainPrompt = explainPrompt;
	}

	public String getCreatePrompt() {
		return createPrompt;
	}

	public void setCreatePrompt(String createPrompt) {
		this.createPrompt = createPrompt;
	}
}
