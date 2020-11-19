package com.klimbi123.tickettesttask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private EditText productNameField;
    private EditText priceField;
    private Button addTicketButton;

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

        productNameField = findViewById(R.id.productNameField);
        priceField = findViewById(R.id.priceField);
        addTicketButton = findViewById(R.id.addTicketButton);

        addTicketButton.setOnClickListener(view -> {
            String message = String.format("%s - %s", productNameField.getText().toString(), priceField.getText().toString());
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    .setAction("AddTicket", null).show();
            productNameField.setText("");
            priceField.setText("");
        });

        productNameField.addTextChangedListener(inputFieldsFilled);
        priceField.addTextChangedListener(inputFieldsFilled);
    }
}