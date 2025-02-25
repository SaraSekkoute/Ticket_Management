package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ApiService {

    public static Employee authenticate(String username, String password) {
        try {
            // Encodage des paramètres
            String encodedUsername = URLEncoder.encode(username, "UTF-8");
            String encodedPassword = URLEncoder.encode(password, "UTF-8");

            // Construire l'URL
            URL url = new URL("http://localhost:8082/api/employees/authenticate?username=" + encodedUsername + "&password=" + encodedPassword);
            System.out.println("Request URL: " + url);

            // Ouvrir la connexion HTTP
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode != 200) {
                System.out.println("Authentication failed: Invalid response code");
                return null;
            }

            // Lire la réponse JSON
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            conn.disconnect();

            System.out.println("Response Body: " + response);

            // Convertir la réponse JSON en objet Employee
            ObjectMapper objectMapper = new ObjectMapper();
            Employee employee = objectMapper.readValue(response.toString(), Employee.class);

            System.out.println("Authenticated Employee: " + employee);
            return employee;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
