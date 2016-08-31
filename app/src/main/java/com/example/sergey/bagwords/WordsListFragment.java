package com.example.sergey.bagwords;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */

public class WordsListFragment extends ListFragment {

    TextView textView;
    EditText sendWordToGoogle;
    Button btnSendToGoogle;
    SimpleCursorAdapter userAdapter;
    public static long id;

    private SQLiteOpenHelper wordsDatabaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;


    static interface WordsListListener{
        void itemClicked(long id);
    }

    private WordsListListener listListener;


    public WordsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listListener = (WordsListListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public void onResume() {
        super.onResume();

        sendWordToGoogle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.w("onTextChanged","Here");
                id++;
                textView.setText(Integer.toString((int) id));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if(listListener != null){
            listListener.itemClicked(id);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        if (view != null) {
            sendWordToGoogle = (EditText) view.findViewById(R.id.search_word);
            btnSendToGoogle = (Button) view.findViewById(R.id.btn_send);
            textView = (TextView) view.findViewById(R.id.text_for_test);
        }
        registerForContextMenu(getListView());
        connectDB();
        updateList();
    }

    @Override
    public void onStart() {
        super.onStart();

       updateList();

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
