package ai.nd.abap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ChatAPIGPT extends ChatAPI implements BackgroundActionsIF {

	private String requestString = null;
	private String resultString = null;
	private Settings settings = null;

	@Override
	public void processRequest(String requestString, Settings settings) {

		ChatAPI chatAPI = (ChatAPI) new ChatAPIGPT();

		// inject parameters
		((ChatAPIGPT) chatAPI).setRequestString(StringFormatter.escapeSpecialCharacters(requestString));
		((ChatAPIGPT) chatAPI).setResultString(null);
		((ChatAPIGPT) chatAPI).setSettingsHandler(settings);

		// Run job with chatAPI-Instance / Handler
		BackgroundJob job = new BackgroundJob();
		job.execute((BackgroundActionsIF) chatAPI);

	}

	@Override
	public void runAction() {

		// GPT API endpoint URL
		Settings settings = Settings.getInstance();

		String apiUrl = settings.getApiURI();

		// API key for authorization
		String apiKey = settings.getApiKey();

		// Cleanup Prompt
		String escaped = StringFormatter.escapeSpecialCharacters(getRequestString());

		// Request body (prompt)
		String requestBody = "{\"model\":\"" + settings.getSelectedModel()
				+ "\",\"messages\": [{\"role\": \"user\",\"content\": \"" + escaped
				+ "\"}],\"temperature\": 0.7, \"max_tokens\":" + settings.getMaxToken() + "}";

		// Create URL object
		URL url;

		// Create HTTP connection
		HttpURLConnection connection = null;
		try {
			url = new URL(apiUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", "Bearer " + apiKey);
			connection.setDoOutput(true);
			// Write request body to connection
			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = requestBody.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
		} catch (IOException e) {
			e.printStackTrace();
			String errorText = "Error during request\r\n:" + requestBody;
			System.out.println(errorText);
			setResultString(e.getMessage() + "\r\n" + errorText);
			return;
		}

		// Read response
		if (connection != null) {
			StringBuilder response = new StringBuilder();
			try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}

				// Parse JSON response and extract the completion text
				JSONParser parser = new JSONParser();
				JSONObject jsonResponse = (JSONObject) parser.parse(response.toString());
				JSONObject choices = (JSONObject) ((org.json.simple.JSONArray) jsonResponse.get("choices")).get(0);
				JSONObject gptMessage = (JSONObject) ((org.json.simple.JSONObject) choices.get("message"));
				setResultString(gptMessage.get("content").toString());

			} catch (Exception e) {
				String errorText = "Error in response\r\n:" + extractResponseBody(connection);
				System.out.println(errorText);
				setResultString(e.getMessage() + "\r\n" + errorText);
			}
			connection.disconnect();
		}
	}

	@Override
	public void resultAction() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				try {
					ViewPart activeView = (ViewPart) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage().showView(QueryView.ID);
					QueryView queryView = (QueryView) activeView;
					queryView.setText(getResultString());
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		});

	}

    public String extractResponseBody(HttpURLConnection connection) {
        try {
            int responseCode = connection.getResponseCode();
            InputStream inputStream;
            if (responseCode >= 400) {
                inputStream = connection.getErrorStream();
            } else {
                inputStream = connection.getInputStream();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

	public String getRequestString() {
		return requestString;
	}

	public void setRequestString(String requestString) {
		this.requestString = requestString;
	}

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public Settings getSettingsHandler() {
		return settings;
	}

	public void setSettingsHandler(Settings settings) {
		this.settings = settings;
	}

}
