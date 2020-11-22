package com.klimbi123.tickettesttask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView responseText;
    private EditText productNameField;
    private EditText priceField;
    private Button addTicketButton;
    private TicketClass.Ticket ticket;

    private TextWatcher inputFieldsFilled = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            addTicketButton.setEnabled(
                    productNameField.getText().length() > 0
                    && priceField.getText().length() > 0);
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responseText = findViewById(R.id.responseText);
        productNameField = findViewById(R.id.productNameField);
        priceField = findViewById(R.id.priceField);
        addTicketButton = findViewById(R.id.addTicketButton);

        addTicketButton.setOnClickListener(view -> {
            ticket = TicketClass.Ticket.newBuilder()
                    .setId(5)
                    .setProductName(productNameField.getText().toString())
                    .setPrice(Integer.parseInt(priceField.getText().toString()))
                    .build();

            productNameField.setText("");
            priceField.setText("");

            String shortMessage = String.format("Sending: %s - %s", ticket.getProductName(), ticket.getPrice());
            responseText.setText("Sending");
            Snackbar.make(view, shortMessage, Snackbar.LENGTH_LONG)
                    .setAction("AddTicket", null).show();
            send(ticket);
        });

        productNameField.addTextChangedListener(inputFieldsFilled);
        priceField.addTextChangedListener(inputFieldsFilled);
    }

    private void send(TicketClass.Ticket ticket) {
        System.out.println("Send start");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.8.102:5000/post");
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("POST");
                    http.setDoOutput(true);
                    OutputStream out = http.getOutputStream();
                    ticket.writeTo(out);

                    boolean sentSuccessfully = http.getResponseCode() == 200;
                    String longMessage = String.format("%s\nProduct name: %s\nPrice (cents): %s", sentSuccessfully ? "Sent" : "Failed", ticket.getProductName(), ticket.getPrice());
                    responseText.setText(longMessage);
                    System.out.println(sentSuccessfully ? "Send complete" : "Send failed");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
}