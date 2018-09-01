package com.gamecodeschool.notetoself;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteAdapter mNoteAdapter;

    private boolean mSound;
    private int mAnimOption;
    private SharedPreferences mPrefs;
    public void createNewNote(Note n){
        mNoteAdapter.addNote(n);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNoteAdapter = new NoteAdapter();
        ListView listNote = (ListView) findViewById(R.id.listView);
        listNote.setAdapter(mNoteAdapter);

        listNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int whichItem, long id) {
                Note tempnote = mNoteAdapter.getItem(whichItem);
                DialogShowNote dialog = new DialogShowNote();
                dialog.sendNoteSelected(tempnote);
                dialog.show(getFragmentManager(),"");
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.action_add){
            DialogNewNote dialog = new DialogNewNote();
            dialog.show(getFragmentManager(),"123");
            return true;
        }
        if(item.getItemId() == R.id.action_settings){
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    public class NoteAdapter extends BaseAdapter{
        private Note.JSONSerializer mSerializer;
        List<Note> noteList = new ArrayList<Note>();
        
        public NoteAdapter(){
            mSerializer = new Note.JSONSerializer("NoteToSelf.json",MainActivity.this.getApplicationContext());
            try {
                noteList = mSerializer.load();
            }catch (Exception e){
                noteList = new ArrayList<Note>();
                Log.e("Error loading notes: ","",e);
            }
        }

        public int getCount(){
            return  noteList.size();
        }

        public Note getItem(int whichItem){
            return noteList.get(whichItem);
        }

        public long getItemId(int whichItem){
            return whichItem;
        }

        public View getView(int whichItem, View view, ViewGroup viewGroup){
            if(view == null){
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.listitem,viewGroup,false);
            }

            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle1);
            TextView txtDescription = (TextView) view.findViewById(R.id.txtDescription1);
            ImageView ivImportant = (ImageView) view.findViewById(R.id.imageViewImportant);
            ImageView ivTodo = (ImageView) view.findViewById(R.id.imageViewTodo);
            ImageView ivIdea = (ImageView) view.findViewById(R.id.imageViewIdea);

            Note tempNote = noteList.get(whichItem);
            if(!tempNote.getmImportant()){
                ivImportant.setVisibility(View.GONE);
            }
            if(!tempNote.getmTodo()){
                ivTodo.setVisibility(View.GONE);
            }
            if(!tempNote.getmIdea()){
                ivIdea.setVisibility(View.GONE);
            }

            txtTitle.setText(tempNote.getmTitle());
            txtDescription.setText(tempNote.getmDescription());
            return view;
        }
        public void addNote(Note n){
            noteList.add(n);
            notifyDataSetChanged();
        }

        public void saveNotes(){
            try {
                mSerializer.save(noteList);
            }catch (Exception e){
                Log.e("Error Saving Notes","",e);
            }
        }
    }

    public void onResume(){
        super.onResume();
        mPrefs = getSharedPreferences("Note to self",MODE_PRIVATE);
        mSound = mPrefs.getBoolean("sound",true);
        mAnimOption = mPrefs.getInt("anim option",SettingsActivity.FAST);
    }

    protected void onPause(){
        super.onPause();
        mNoteAdapter.saveNotes();
    }
}
