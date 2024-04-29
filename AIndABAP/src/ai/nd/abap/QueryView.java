package ai.nd.abap;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class QueryView extends ViewPart {
	
	private static final String welcomeText = "Welcome master*in, please select a text and press the required function to try me out ....";
	private Browser fBrowser;
	public static String ID = "ai.nd.abap.QueryView";

	public QueryView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		fBrowser = new Browser(parent, SWT.WEBKIT);
		fBrowser.setText( this.getHTMLTemplate(welcomeText) );
	}

	@Override
	public void setFocus() {

	}
	
	public void setText(String divContent) {
		fBrowser.setText( getHTMLTemplate(divContent) );	
	}
	
	public void refresh() {
		fBrowser.setText( getHTMLTemplate(welcomeText) );
	}
	
	private String getHTMLTemplate(String pageContent) {
		String htmlTemplate = null;
		try (InputStream inputStream = getClass().getResourceAsStream("QueryView.tpl.html")) {
			htmlTemplate = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (pageContent != null) {
			htmlTemplate = htmlTemplate.replace("<div id=\"content\"/>", "<pre>" + pageContent + "</pre>");
			htmlTemplate = htmlTemplate.replace("<div id=\"header\" class=\"header\"/>", "<div class=\"header\">AIA - (AI)nd(A)BAP - Version " + Settings.getInstance().getCurrentVersion() + "</div>");
		}
		return htmlTemplate;
	}

}
