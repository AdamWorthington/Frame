package com.frame.app.View;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.frame.app.R;

import java.io.ByteArrayOutputStream;
import org.json.JSONException;
import org.json.JSONObject;

public class Text_post extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_post);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_text_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeToMain(View view){
        Intent intent;
        intent = new Intent(this,MainPage.class);
        this.startActivity(intent);

    }

    public void changeToMainAfterPosting(View view){
        Intent intent;
        intent = new Intent(this,MainPage.class);
        EditText editText =(EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        System.out.print(message + "\n");
        /*JSONObject json = TextToJson(message,null,null,null,null,null);
        StringRepresentation stringRep = new StringRepresentation(
                obj.toString());
        stringRep.setMediaType(MediaType.APPLICATION_JSON);

        try {
            res.post(stringRep).write(System.out);
        } catch (ResourceException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        this.startActivity(intent);

    }



    public static JSONObject TextToJson(String text, Double lat, Double lon, String user, String date, String[] tags)
    {
        JSONObject jo = new JSONObject();

        try {
            jo.put("Text", text);
            jo.put("Lat",lat);
            jo.put("Lon",lon);
            jo.put("User", user);
            jo.put("Date", date);
            jo.put("Tags", tags);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jo;
    }
}
