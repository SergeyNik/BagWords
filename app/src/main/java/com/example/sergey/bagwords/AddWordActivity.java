package com.example.sergey.bagwords;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddWordActivity extends AppCompatActivity implements View.OnClickListener {
    EditText sendWord, receivedWord;
    Button btnAddNewWord, btnAddSqlWord;
    String wordAfterSend;
    Boolean key = false;
    String string;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        // TODO: добавить сравнение слов. игнор повтора

        sendWord = (EditText) findViewById(R.id.edit_send_word);
        receivedWord = (EditText) findViewById(R.id.edit_recived_word);
        btnAddNewWord = (Button) findViewById(R.id.btn_add_new_word);
        btnAddSqlWord = (Button) findViewById(R.id.btn_add_word_sql);
        sendWord.setText(TempVariables.string);
        textView = (TextView) findViewById(R.id.textView);


        // get translate
        Intent intent = getIntent();
        String action = intent.getStringExtra(Intent.EXTRA_TEXT);
        // TODO: открыть список
        // if (action.equals(""))
        receivedWord.setText(action);
        // TODO: отправить получить получить еще новое слово положить в новый edit
        // TODO: удалить после нажатия

        btnAddNewWord.setOnClickListener(this);
        btnAddSqlWord.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_add_new_word:
                wordAfterSend = sendWord.getText().toString();
                TempVariables.key = true;
                TempVariables.string = sendWord.getText().toString();
                goToGoogleTranslate(wordAfterSend);
                break;

            case R.id.btn_add_word_sql:

                SQLiteOpenHelper sq = new WordsDatabaseHelper(this);
                SQLiteDatabase qLiteDatabase = sq.getReadableDatabase();
//                String enWord = sendWord.getText().toString();
                String ruWord = receivedWord.getText().toString();
                ContentValues wordValues = new ContentValues();

                // TODO: слово после отправки передать через объект
                wordValues.put("EN_WORD", TempVariables.string);
                wordValues.put("RU_WORD", ruWord);

                qLiteDatabase.insert("MY_WORDS", null, wordValues);

                TempVariables.string = "";
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }

    }

    public void goToGoogleTranslate(String sendWord){
        try {
            Intent intent = new Intent();

            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.setPackage("com.google.android.apps.translate");

            intent.putExtra(Intent.EXTRA_TEXT, sendWord);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);


        } catch (ActivityNotFoundException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplication(), R.string.str_excp_no_google_translate,
                    Toast.LENGTH_SHORT).show();
        }
    }
}