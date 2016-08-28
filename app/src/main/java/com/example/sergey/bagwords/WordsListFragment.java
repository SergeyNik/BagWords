package com.example.sergey.bagwords;



import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */

public class WordsListFragment extends ListFragment {

    private SQLiteOpenHelper wordsDatabaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;
    private long wordId;
    private Activity activity;
    CursorAdapter listAdapter;


    public WordsListFragment() {
        // Required empty public constructor
        activity = getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
        connectDB();
        updateList();
    }

    @Override
    public void onStart() {
        super.onStart();

        // TODO: сделать в потоке

    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.edit:
                int delete = sqLiteDatabase.delete("MY_WORDS", "_id = " + Long.toString(info.id), null);
                updateList();
                break;
            default:
                return super.onContextItemSelected(item);
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cursor.close();
        sqLiteDatabase.close();
    }





    // *************************************************************************************************
    public void setWord(long id) {
        this.wordId = id;
    }

    public void updateList(){
        cursor = sqLiteDatabase.query("MY_WORDS",
                new String[]{"_id", "EN_WORD"},
                null, null, null, null, null);


        CursorAdapter listAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{"EN_WORD"},
                new int[]{android.R.id.text1},
                0);
        setListAdapter(listAdapter);
    }

    public void connectDB(){
        wordsDatabaseHelper = new WordsDatabaseHelper(getActivity());
        try {
            sqLiteDatabase = wordsDatabaseHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

}
