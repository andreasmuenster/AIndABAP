package ai.nd.abap;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
//import org.eclipse.core.runtime.preferences.IEclipsePreferences;
//import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.MessageDialog;
//import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.handlers.HandlerUtil;
//import org.osgi.service.prefs.BackingStoreException;
import org.eclipse.swt.widgets.Shell;

public class SettingsHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		SettingsDialog dialog = new SettingsDialog(HandlerUtil.getActiveShell(event));
		dialog.open();
		return null;
	}

	private static class SettingsDialog extends MessageDialog {
		private Text apiKeyText;
		private Text apiURI;
		private Text modelText;
		private Text maxToken;

		protected SettingsDialog(Shell parentShell) {
			super(parentShell, "Settings", null, "Edit Settings", MessageDialog.INFORMATION,
					new String[] { "OK", "Cancel", "Reset to defaults" }, 0);
		}

		@Override
		protected Control createCustomArea(Composite parent) {
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(2, false));
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			Label apiURILabel = new Label(composite, SWT.NONE);
			apiURILabel.setText("API URI:");
			apiURI = new Text(composite, SWT.BORDER);
			apiURI.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

			Label apiKeyLabel = new Label(composite, SWT.NONE);
			apiKeyLabel.setText("API Key:");
			apiKeyText = new Text(composite, SWT.BORDER);
			apiKeyText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			apiKeyText.setEchoChar('*'); 

			Label modelLabel = new Label(composite, SWT.NONE);
			modelLabel.setText("Model:");
			modelText = new Text(composite, SWT.BORDER);
			modelText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			

			Label maxTokenLabel = new Label(composite, SWT.NONE);
			maxTokenLabel.setText("Max. Token:");
			maxToken = new Text(composite, SWT.BORDER);
			maxToken.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));			
			
			Settings settings = Settings.getInstance();
			settings.loadSettings();

			apiURI.setText(settings.getApiURI());
			apiKeyText.setText(settings.getApiKey());
			modelText.setText(settings.getSelectedModel());
			maxToken.setText(settings.getMaxToken());

			return composite;
		}

		@Override
		protected void buttonPressed(int buttonId) {
			if (buttonId == 0) { // OK button pressed
				Settings settings = Settings.getInstance();
				settings.setApiKey(apiKeyText.getText());
				settings.setApiURI(apiURI.getText());
				settings.setSelectedModel(modelText.getText());
				settings.setMaxToken(maxToken.getText());
				
				settings.saveSettings();
				super.buttonPressed(buttonId);
			} else if (buttonId == 2) { // Default values
				Settings settings = Settings.getInstance();
				settings.initSettings(true);
				apiURI.setText(settings.getApiURI());
				apiKeyText.setText(settings.getApiKey());
				modelText.setText(settings.getSelectedModel());
				maxToken.setText(settings.getMaxToken());
			} else {
				super.buttonPressed(buttonId);
			}
		}

	}
}