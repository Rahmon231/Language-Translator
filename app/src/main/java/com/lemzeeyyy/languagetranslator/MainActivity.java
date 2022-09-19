package com.lemzeeyyy.languagetranslator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class MainActivity extends AppCompatActivity {
    private Spinner toSpinner, fromSpinner;
    private EditText sourceET;
    private TextView translatedTV;
    private Button translateBtn;

    //Source Array of strings, Spinner data

    String[] fromLanguages = {
            "From","English","Afrikaans","Arabic","Belarusian","Bengali","Catalan","Hindi","Urdu",
            "French"
    };

    String[] toLanguages = {
            "To","English","Afrikaans","Arabic","Belarusian","Bengali","Catalan","Hindi","Urdu",
            "French"
    };

    private static final int REQUEST_CODE = 1;
    String languageCode, fromLanguageCode, toLanguageCode = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();

        //From Spinner Setup
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromLanguageCode = getLanguageCode(fromLanguages[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ArrayAdapter fromAdapter = new ArrayAdapter(this,
                R.layout.spinner_item,fromLanguages);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);

        //To Spinner Setup
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toLanguageCode = getLanguageCode(toLanguages[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter toAdapter = new ArrayAdapter(this,
                R.layout.spinner_item,toLanguages);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translatedTV.setText("");
                if(sourceET.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter a text", Toast.LENGTH_SHORT).show();
                }else if(fromLanguageCode.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter source language", Toast.LENGTH_SHORT).show();
                }else if(toLanguageCode.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter a target language", Toast.LENGTH_SHORT).show();
                }else {
                    translateLanguage(fromLanguageCode,toLanguageCode,sourceET.getText().toString());
                }
            }
        });

    }

    private void translateLanguage(String fromLanguageCode, String toLanguageCode, String sourceText) {
        translatedTV.setText("Downloading Language Model...");
        try {
            TranslatorOptions options = new TranslatorOptions.Builder()
                    .setSourceLanguage(fromLanguageCode)
                    .setTargetLanguage(toLanguageCode)
                    .build();
            Translator translator = Translation.getClient(options);
            DownloadConditions conditions = new DownloadConditions.Builder().build();

            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(unused -> {
                translatedTV.setText("Translating...");

                translator.translate(sourceText)
                        .addOnSuccessListener(s -> translatedTV.setText(s))
                        .addOnFailureListener(e ->
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
            }).addOnFailureListener(e ->
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getLanguageCode(String language) {
        String languageCode ="";
        switch (language){
            case "English":
                languageCode = TranslateLanguage.ENGLISH;
                break;

            case "Afrikaans":
                languageCode = TranslateLanguage.AFRIKAANS;
                break;

            case "Arabic":
                languageCode = TranslateLanguage.ARABIC;
                break;

            case "Belarusian":
                languageCode = TranslateLanguage.BELARUSIAN;
                break;

            case "Bengali":
                languageCode = TranslateLanguage.BENGALI;
                break;

            case "Catalan":
                languageCode = TranslateLanguage.CATALAN;
                break;

            case "Hindi":
                languageCode = TranslateLanguage.HINDI;
                break;

            case "Urdu":
                languageCode = TranslateLanguage.URDU;
                break;

            case "French":
                languageCode = TranslateLanguage.FRENCH;
                break;
        }
        return languageCode;
    }

    private void initWidgets() {
        toSpinner = findViewById(R.id.toSpinnerId);
        fromSpinner = findViewById(R.id.fromSpinnerId);
        sourceET = findViewById(R.id.sourceEditText);
        translateBtn = findViewById(R.id.translateBtn);
        translatedTV = findViewById(R.id.translated_tv);
    }
}