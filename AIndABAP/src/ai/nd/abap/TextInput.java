//package ai.nd.abap;
package ai.nd.abap;

//import org.eclipse.ui.IEditorPart;
//import org.eclipse.ui.PlatformUI;
//import org.eclipse.ui.part.MultiPageEditorPart;
//import org.eclipse.ui.texteditor.ITextEditor;
//import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
//
//import com.sap.adt.tools.abapsource.ui.sources.editors.IAbapSourcePage;
//import com.sap.adt.tools.core.ui.editors.IAdtFormEditor;

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;
import com.sap.adt.tools.abapsource.ui.sources.editors.IAbapSourcePage;
import com.sap.adt.tools.core.ui.editors.IAdtFormEditor;

@SuppressWarnings("restriction")
public class TextInput {

	public String getSelectedText() {
		IAbapSourcePage textEditor = null;
		ISelectionProvider selectionProvider = null;
		String selectedText = "";

		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		if (editor instanceof ITextEditor) {
			ITextEditor defaultTextEditor = (ITextEditor) editor;
			selectionProvider = defaultTextEditor.getSelectionProvider();
		} else if (editor instanceof IAbapSourcePage) {
			textEditor = (IAbapSourcePage) editor;
			selectionProvider = textEditor.getSelectionProvider();
		} else if (editor instanceof MultiPageEditorPart) {
			MultiPageEditorPart multiPageEditor = (MultiPageEditorPart) editor;
			IEditorPart activePage = (IEditorPart) multiPageEditor.getSelectedPage();
			if (activePage instanceof IAbapSourcePage) {
				textEditor = (IAbapSourcePage) activePage;
			} else if (multiPageEditor instanceof IAdtFormEditor) {
				IEditorPart ed = ((IAdtFormEditor) multiPageEditor).getActiveEditor();
				if (ed instanceof IAbapSourcePage) {
					textEditor = (IAbapSourcePage) ed;
				}
			}
			if (textEditor != null) {
				selectionProvider = textEditor.getSelectionProvider();
			}
		}

		if (selectionProvider != null) {
			ISelection selection = selectionProvider.getSelection();
			if (selection instanceof ITextSelection) {
				ITextSelection textSelection = (ITextSelection) selection;
				selectedText = textSelection.getText();
			}
		}

		return selectedText;
	}

}

//package ai.nd.abap;
//
//import org.eclipse.ui.IEditorPart;
//import org.eclipse.ui.PlatformUI;
//import org.eclipse.ui.texteditor.ITextEditor;
//import org.eclipse.jface.text.ITextSelection;
//import org.eclipse.jface.viewers.ISelection;
//
//public class TextInput {
//	
//	public String getSelectedText() {
//		String selectedText = "";
//		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
//		if (editor instanceof ITextEditor) {
//			ITextEditor textEditor = (ITextEditor) editor;
//			ISelection selection = textEditor.getSelectionProvider().getSelection();
//			if (selection instanceof ITextSelection) {
//				ITextSelection textSelection = (ITextSelection) selection;
//				selectedText = textSelection.getText();
//			}
//		}		
//		return selectedText;
//	}
//
//}
