package com.example.assettracking;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class AddAssetActivity extends AppCompatActivity implements AlreadyExistsDialog.AlreadyExistsDialogListner {

    private IntentIntegrator qrsScan;
    private EditText eAssetId;
    private EditText eAssetName;
    private EditText eAssetLocation;
    private EditText eAssetType;
    private EditText eAssetCount;

    String assetId;
    String assetName;
    String assetType;
    String assetLocation;
    String stockCount;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_asset);
        FirebaseApp.initializeApp(this);

        qrsScan =  new IntentIntegrator(this);
        eAssetId = findViewById(R.id.eAssetId);
        eAssetType=findViewById(R.id.eAssetType);
        eAssetLocation=findViewById(R.id.eAssetLocation);
        eAssetName=findViewById(R.id.eAssetName);
        eAssetCount= findViewById(R.id.eAssetCount);

        databaseReference = FirebaseDatabase.getInstance().getReference("Assets");
    }

    public void scanImage(View view)
    {
        /* look for qr code in the image */

            qrsScan.initiateScan();

    }

    public void submit(View view)
    {
        /* upload the details of the asset to database*/
        if(eAssetId.getText().toString().isEmpty())
        {
            eAssetId.setError("cannot be empty");
            return;
        }

        if(eAssetName.getText().toString().isEmpty())
        {
            eAssetId.setError("cannot be empty");
            return;
        }

        if(eAssetLocation.getText().toString().isEmpty())
        {
            eAssetId.setError("cannot be empty");
            return;
        }

        if(eAssetType.getText().toString().isEmpty())
        {
            eAssetId.setError("cannot be empty");
            return;
        }
        if(eAssetCount.getText().toString().isEmpty())
        {
            eAssetCount.setError("cannot be empty");
            return;
        }

        assetId = eAssetId.getText().toString();
        assetName = eAssetName.getText().toString();
        assetType = eAssetType.getText().toString();
        assetLocation = eAssetLocation.getText().toString();
        stockCount = eAssetCount.getText().toString();


        databaseReference.child(assetId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    showNoticeDialog();


                }
                else
                {
                    databaseReference.child(assetId).child("Name").setValue(assetName);
                    databaseReference.child(assetId).child("Location").setValue(assetLocation);
                    databaseReference.child(assetId).child("Type").setValue(assetType);
                    databaseReference.child(assetId).child("StockCount").setValue(stockCount);
                    Toast.makeText(getApplicationContext(),"Asset Added",Toast.LENGTH_SHORT).show();
                    finish();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





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


    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new AlreadyExistsDialog();
        dialog.show(getSupportFragmentManager(), "AlreadyExistDialog");
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment) {
        databaseReference.child(assetId).child("Name").setValue(assetName);
        databaseReference.child(assetId).child("Location").setValue(assetLocation);
        databaseReference.child(assetId).child("Type").setValue(assetType);
        Toast.makeText(this,"Asset Added",Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {

        dialogFragment.dismiss();

    }
}
