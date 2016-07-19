//Books API Key = AIzaSyC4bXzxTCAjWNpRHoh3srFHJlzy6nOnmx0
//API call
//GET "https://www.googleapis.com/books/v1/volumes?q=isbn:9780262029629&key=AIzaSyC4bXzxTCAjWNpRHoh3srFHJlzy6nOnmx0"
package com.nitishshukla.sociallibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements OnClickListener{
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    private Button scanBtn, getBookFromISBNBtn;
    private TextView formatTxt, contentTxt, jsonTxt;
    private ImageView img_book;
    private String strISBN, strURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBtn = (Button)findViewById(R.id.scan_button);
        getBookFromISBNBtn = (Button)findViewById(R.id.btnBookFromISBN);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        //isbnJSON = (TextView)findViewById(R.id.isbn_json);
        img_book = (ImageView) findViewById(R.id.imgBook);

        scanBtn.setOnClickListener(this);
        getBookFromISBNBtn.setOnClickListener(this);
    }

    public void onClick(View v){
        //respond to clicks
        switch (v.getId()) {
            case R.id.scan_button: {
                //scan
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
                break;
            }
            case R.id.btnBookFromISBN: {
                //CONTENT:
                strISBN = contentTxt.getText().toString();
                strISBN = strISBN.substring(9);
                Toast toast = Toast.makeText(getApplicationContext(), strISBN, Toast.LENGTH_LONG);
                toast.show();
                strISBN = "https://www.googleapis.com/books/v1/volumes?q=isbn:".concat(strISBN);
                strISBN = strISBN.concat("&key=AIzaSyC4bXzxTCAjWNpRHoh3srFHJlzy6nOnmx0");
                getGoogleBook(strISBN);
                break;
            }

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void getGoogleBook(String url)
    {
        Toast toast = Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG);
        toast.show();
        jsonTxt = (TextView) findViewById(R.id.isbn_json);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        jsonTxt.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                        String json = null;

                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null){
                            switch(response.statusCode){
                                case 400:
                                    json = new String(response.data);
                                    if(json != null) {
                                        displayMessage(json);
                                        jsonTxt.setText(json);
                                    }
                                    break;
                            }
                            //Additional cases
                        }
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }
    public void onErrorResponse(VolleyError error) {
        String json = null;

        NetworkResponse response = error.networkResponse;
        if(response != null && response.data != null){
            switch(response.statusCode){
                case 400:
                    json = new String(response.data);
                    json = trimMessage(json, "message");
                    if(json != null) displayMessage(json);
                    break;
            }
            //Additional cases
        }
    }

    public String trimMessage(String json, String key){
        String trimmedString = null;

        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }
    public void displayMessage(String toastString){
        Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_LONG).show();
    }
}