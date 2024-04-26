package ai.nd.abap;

import org.eclipse.core.runtime.jobs.Job;
//import org.eclipse.ui.PartInitException;
//import org.eclipse.ui.PlatformUI;
//import org.eclipse.ui.part.ViewPart;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
//import org.eclipse.jface.dialogs.MessageDialog;
//import org.eclipse.swt.widgets.Display;

public class BackgroundJob {

	String jobResults = "empty";

	public void execute(BackgroundActionsIF jobHandler) {

		Job job = new Job("AIA is gathering data ....") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				jobHandler.runAction();
				jobHandler.resultAction();
				return Status.OK_STATUS;
			}

		};
		job.setUser(true);

		// Start the Job
		job.schedule();
	}

}
