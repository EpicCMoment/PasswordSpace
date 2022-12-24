package kozmikoda.passwordspace;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ResetCodeSender {

    final private static String apiID = "";
    final private static String apiKey = "";
    final private static String sender = "";
    final private static String apiUrl = "https://api.vatansms.net/api/v1/1toN";

    public static void sendViaSMS(String message, String phoneNumber) {

        String jsonFormData = "{ \"api_id\": \"%s\", \"api_key\": \"%s\", \"sender\": \"%s\", \"message_type\": \"normal\", \"message\": \"%s\", \"phones\": [ \"%s\" ] }".formatted(
                apiID, apiKey, sender, message, phoneNumber
        );

        sendJSON(jsonFormData);


    }


    private static void sendJSON(String JSONData) {
        try {
            URL url = new URL(apiUrl);

            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setDoOutput(true);
            connect.setConnectTimeout(5000);
            connect.setDoInput(true);
            connect.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connect.setRequestMethod("POST");

            OutputStream prepareFormData = connect.getOutputStream();
            prepareFormData.write(JSONData.getBytes(StandardCharsets.UTF_8));
            prepareFormData.close();

            InputStream inputStream = new BufferedInputStream(connect.getInputStream());
            Scanner s = new Scanner(inputStream).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";

            System.out.println(result);

            inputStream.close();
            connect.disconnect();

        } catch (Exception e) {
            System.out.println("An error occurred while trying to send the SMS: " + e.getMessage());
        }
    }

    public static void sendViaSMS(String message, String... phoneNumbers) {

        StringBuilder targets = new StringBuilder();

        for (var number : phoneNumbers) {
            targets.append(number).append(",");
        }

        String jsonFormData = "{ \"api_id\": \"%s\", \"api_key\": \"%s\", \"sender\": \"%s\", \"message_type\": \"normal\", \"message\": \"%s\", \"phones\": [ \"%s\" ] }".formatted(
                apiID, apiKey, sender, message, targets.toString()
        );

        sendJSON(jsonFormData);
    }


}
