package com.alura.literalura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {

    public String obtenerDatos(String url) {
        HttpClient client = HttpClient.newHttpClient();
        System.out.println("URL solicitada: " + url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            System.err.println("Error de IO: " + e.getMessage());
            e.printStackTrace();
            return "Error de conexión al API";
        } catch (InterruptedException e) {
            System.err.println("Solicitud interrumpida: " + e.getMessage());
            Thread.currentThread().interrupt();
            return "La solicitud fue interrumpida";
        }
        String json = response.body();
        return json;
    }
}
