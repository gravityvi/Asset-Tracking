package com.example.assettracking;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class AddAssetActivity extends AppCompatActivity {

    private IntentIntegrator qrsScan;
    private EditText eAssetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_asset);


        qrsScan =  new IntentIntegrator(this);
        eAssetId = findViewById(R.id.eAssetId);
    }

    public void scanImage(View view)
    {
        /* look for qr code in the image */

            qrsScan.initiateScan();

    }

    public void submit(View view)
    {
        /* upload the details of the asset to database*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null)
        {
            if(result.getContents()==null)
            {
                Toast.makeText(this,"Result Not Found ",Toast.LENGTH_LONG).show();
            }
            else
            {
                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    eAssetId.setText(obj.getString("id"));

                }catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();
                }
            }
        }
        else
        {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
}
