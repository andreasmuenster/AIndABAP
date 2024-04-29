package ai.nd.abap;

public class StringFormatter {

	public static String escapeSpecialCharacters(String prompt) {

		if (prompt == null) {
			return prompt;
		}

		// Replace Backslashes
		String escaped = prompt.replaceAll("\\\\", "\\\\\\\\");
		// Replace double quotes
		escaped = escaped.replaceAll("\"", "\\\\\"");
		// Replace newline characters
		escaped = escaped.replaceAll("\n", "\\\\n");
		// Replace carriage return characters
		escaped = escaped.replaceAll("\r", "\\\\r");
		// Replace tabs with spaces
		escaped = escaped.replaceAll("\t", " ");
		return escaped;
	}
}
