package ai.nd.abap;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class RefreshCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        try {
			ViewPart activeView = (ViewPart) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(QueryView.ID);
			QueryView queryView = (QueryView) activeView;
			queryView.refresh();
		} catch (PartInitException e) {
			e.printStackTrace();
		}    
		return null;
	}

}
