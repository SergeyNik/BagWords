package com.example.sergey.bagwords;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    //TODO: исправить
    public static final String EXTRA_WORKOUT_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        DetailFragment workoutDetailFragment = (DetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.detail_frag);
        int workoutID = (int) getIntent().getExtras().get(EXTRA_WORKOUT_ID);
        workoutDetailFragment.setWorkout(workoutID);


        //Получение напитка из интента
        int drinkNo = 1;
        //Создание курсора
        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new WordsDatabaseHelper(this);
            SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query ("MY_WORDS",
                    new String[] {"EN_WORD", "RU_WORD"},
                    "_id = ?",
                    new String[] {Integer.toString(drinkNo)},
                    null, null,null);
        //Переход к первой записи в курсоре
        if (cursor.moveToFirst()) {
            //Получение данных напитка из курсора
            String nameText = cursor.getString(0);
            String descriptionText = cursor.getString(1);

            //Заполнение названия напитка
            TextView name = (TextView)findViewById(R.id.text_en_word);
            name.setText(nameText);
            //Заполнение описания напитка
            TextView description = (TextView)findViewById(R.id.text_ru_word);
            description.setText(descriptionText);
            //Заполнение изображения напитка

        }
        cursor.close();
        db.close();
    } catch(SQLiteException e) {
        Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
        toast.show();
    }
    }
}
