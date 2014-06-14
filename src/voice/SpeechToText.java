package voice;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpeechToText {

	// https://console.developers.google.com/project
	
	public static String execute(String stringPath, String token) {

		try {
			String request = "https://www.google.com/speech-api/v2/recognize?output=json&lang=en-us&key="
					+ token;
			URL url = new URL(request);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-type",
					"audio/l16; rate=16000;");
			connection.setConnectTimeout(60000);
			connection.setUseCaches(false);

			Path path = Paths.get(stringPath);
			byte[] data = Files.readAllBytes(path);

			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.write(data);
			wr.flush();
			wr.close();
			connection.disconnect();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String decodedString;
			StringBuffer message = new StringBuffer();
			while ((decodedString = in.readLine()) != null) {
				message.append(decodedString);
			}

			return getTranscript(message.toString());

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
	// version maquette.... il faut absolument ameliorer
	public static String getTranscript(String res) {
		
		Pattern pattern = Pattern.compile("(.*?)(transcript.*?\"\\:\")(.*?)(\")");
		Matcher matcher = pattern.matcher(res);
		// check all occurance
		if (matcher.find()) {
			return matcher.group(3);
		} else 
			return "";
		
	}


}
