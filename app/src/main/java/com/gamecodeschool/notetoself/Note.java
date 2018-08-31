package com.gamecodeschool.notetoself;

import android.content.Context;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class Note {
    private String mTitle;
    private String mDescription;
    private Boolean mIdea;
    private Boolean mTodo;
    private Boolean mImportant;

    private static final String JSON_TITLE = "title";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_IDEA = "idea";
    private static final String JSON_TODO = "todo";
    private static final String JSON_IMPORTANT = "important";

    public Note(JSONObject jo) throws JSONException{
        mTitle = jo.getString(JSON_TITLE);
        mDescription = jo.getString(JSON_DESCRIPTION);
        mIdea = jo.getBoolean(JSON_IDEA);
        mTodo = jo.getBoolean(JSON_TODO);
        mImportant = jo.getBoolean(JSON_IMPORTANT);
    }

    public Note(){

    }

    public JSONObject convertToJSON() throws JSONException{
        JSONObject jo = new JSONObject();

        jo.put(JSON_TITLE,mTitle);
        jo.put(JSON_DESCRIPTION,mDescription);
        jo.put(JSON_IDEA,mIdea);
        jo.put(JSON_TODO,mTodo);
        jo.put(JSON_IMPORTANT,mImportant);

        return jo;
    }

    public static class JSONSerializer{
        private String mFilename;
        private Context mContext;

        public JSONSerializer(String fn,Context con){
            mFilename = fn;
            mContext = con;
        }

        public void save(List<Note> notes) throws IOException, JSONException{
            JSONArray jArray = new JSONArray();
            for(Note n : notes){
                jArray.put(n.convertToJSON());
            }
            Writer writer = null;
            try {
                OutputStream out = mContext.openFileOutput(mFilename,mContext.MODE_PRIVATE);
                writer = new OutputStreamWriter(out);
                writer.write(jArray.toString());
            }finally {
                if (writer != null) writer.close();
            }
        }

        public ArrayList<Note> load() throws IOException, JSONException{
            ArrayList<Note> noteList = new ArrayList<Note>();
            BufferedReader reader = null;
            try {
                InputStream in = mContext.openFileInput(mFilename);
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder jsonString = new StringBuilder();
                String line = null;

                while ((line = reader.readLine())!=null){
                    jsonString.append(line);
                }

                JSONArray jArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
                for (int i = 0;i<jArray.length();i++){
                    noteList.add(new Note(jArray.getJSONObject(i)));
                }
            }catch (FileNotFoundException e){

            }finally {
                if(reader!=null) reader.close();
            }
            return noteList;
        }
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Boolean getmIdea() {
        return mIdea;
    }

    public void setmIdea(Boolean mIdea) {
        this.mIdea = mIdea;
    }

    public Boolean getmTodo() {
        return mTodo;
    }

    public void setmTodo(Boolean mTodo) {
        this.mTodo = mTodo;
    }

    public Boolean getmImportant() {
        return mImportant;
    }

    public void setmImportant(Boolean mImportant) {
        this.mImportant = mImportant;
    }
}
