package com.frame.app.View;

import java.io.IOException;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.frame.app.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

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

    public void changeToMainAfterPosting(View view)
    {
    	//Grab the text field
        EditText editText =(EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        System.out.print(message + "\n");
        
        //Make a client resource.
		ClientResource res = new ClientResource("http://1-dot-august-clover-86805.appspot.com/Simple");
		res.setMethod(Method.POST);
        JSONObject json = TextToJson(message, 500.43, 235.12, "Adam", "10/10/2015", null);
        StringRepresentation stringRep = new StringRepresentation(
        		json.toString());
        stringRep.setMediaType(MediaType.APPLICATION_JSON);

        try {
            res.post(stringRep).write(System.out);
        } catch (ResourceException e) {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
        
        //GO back to main page
        Intent intent;
        intent = new Intent(this,MainPage.class);
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
