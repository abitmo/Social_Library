//Books API Key = AIzaSyBHAWIu3KOp89Xbmsm9Nz5RqY_iTYZyJUQ for
/*c:\Program Files\Java\jre7\bin>keytool -exportcert -alias androiddebugkey -keyst
        ore C:\Users\Nitish\.android\debug.keystore -list -v
        Enter keystore password:
        Alias name: androiddebugkey
        Creation date: Jul 8, 2016
        Entry type: PrivateKeyEntry
        Certificate chain length: 1
        Certificate[1]:
        Owner: C=US, O=Android, CN=Android Debug
        Issuer: C=US, O=Android, CN=Android Debug
        Serial number: 1
        Valid from: Fri Jul 08 22:29:02 IST 2016 until: Sun Jul 01 22:29:02 IST 2046
        Certificate fingerprints:
        MD5:  C4:C9:A0:B7:04:37:8E:4B:C4:8C:83:49:7A:23:60:9F
        SHA1: 11:E4:06:90:87:03:84:B2:2B:B7:84:0B:2C:68:C4:C4:8A:CD:E0:E8
        SHA256: 77:4A:7E:93:AD:2C:67:CC:20:E3:57:A9:2F:72:B3:A8:BB:32:A3:06:7C:4B:01:43:25:53:04:36:17:46:1C:DB
        Signature algorithm name: SHA1withRSA
        Version: 1*/

//API call
//GET https://www.googleapis.com/books/v1/volumes?q=isbn:<ISBN>&key=AIzaSyBHAWIu3KOp89Xbmsm9Nz5RqY_iTYZyJUQ
package com.nitishshukla.sociallibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener{
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    private Button scanBtn;
    private TextView formatTxt, contentTxt, isbnJSON;
    private String strISBN, strURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBtn = (Button)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);

        scanBtn.setOnClickListener(this);
    }

    public void onClick(View v){
        //respond to clicks
        if(v.getId()==R.id.scan_button){
            //scan
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
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
            strISBN = contentTxt.getText().toString();

        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /** Called when the user clicks the "Add Book" button */
    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, AddBookActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void getGoogleBook(String strISBN)
    {
        strURL.concat("https://www.googleapis.com/books/v1/volumes?q=isbn:");
        strURL.concat(strISBN);
        strURL.concat("&key=AIzaSyBHAWIu3KOp89Xbmsm9Nz5RqY_iTYZyJUQ");


    }
}
