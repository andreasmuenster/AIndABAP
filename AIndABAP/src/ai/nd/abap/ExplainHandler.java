package ai.nd.abap;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

public class ExplainHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ChatAPI chatAPI = (ChatAPI) new ChatAPIGPT();
		Settings settings = Settings.getInstance();
		Settings settingsHandler = Settings.getInstance();
		TextInput textEditor = new TextInput();

		String selectedText = textEditor.getSelectedText();
		if (selectedText == "") {
			Display.getDefault().asyncExec(() -> {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Popup Action",
						"Please select something...");
			});
			return null;
		}

		System.out.println("Selected Text:" + selectedText);

		chatAPI.processRequest(settings.getExplainPrompt() + selectedText, settingsHandler);

		return null;
	}

}
