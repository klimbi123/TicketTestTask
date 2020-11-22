package com.klimbi123.tickettesttask;

import android.util.Log;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.RunnableFuture;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private static String readStream(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String nextLine = "";
            while ((nextLine = reader.readLine()) != null) {
                sb.append(nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Test
    public void addition_isCorrect() throws IOException {
        assertEquals(4, 2 + 2);

        TicketClass.Ticket ticket = TicketClass.Ticket.newBuilder()
                .setId(5)
                .setProductName("TicketName")
                .setPrice(Integer.parseInt("512"))
                .build();

        URL url = new URL("http://192.168.8.102:5000/post");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        OutputStream out = http.getOutputStream();
        ticket.writeTo(out);

        System.out.println(http.getResponseCode());
        for (int i = 0; i < 8; i++) {
            System.out.println(http.getHeaderFieldKey(i) + " - " + http.getHeaderField(i));
        }
        String readStream = readStream(http.getInputStream());
        System.out.println(readStream);

        http.disconnect();
    }
}