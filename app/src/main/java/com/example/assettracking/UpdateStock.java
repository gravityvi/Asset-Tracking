package com.example.assettracking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateStock extends AppCompatActivity  {
    private IntentIntegrator qrsScan;
    private EditText eAssetId;
    private TextView tAssetName;
    private TextView tAssetType;
    private TextView tAssetLocation;
    private TextView tAssetStockCount;
    private Button bAddStock;
    private Button bRemoveStock;
    private Button bChangeLocation;

    private String id=null;
    private String name=null;
    private String type=null;
    private String location=null;
    private String stockCount=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_stock);

        qrsScan =  new IntentIntegrator(this);
        eAssetId = findViewById(R.id.eAssetIdToUpdate);
        tAssetName = findViewById(R.id.tAssetName);
        tAssetType=findViewById(R.id.tAssetType);
        tAssetStockCount = findViewById(R.id.tAssetCount);
        tAssetLocation = findViewById(R.id.tAssetLocation);

        bAddStock = findViewById(R.id.bAddStock);
        bRemoveStock=findViewById(R.id.bRemoveStock);
        bChangeLocation = findViewById(R.id.bChangeLocation);






    }

    public void scanImageToUpdate(View view)
    {
        qrsScan.initiateScan();
    }


    public void getDetails(View view)
    {


        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        final ProgressDialog pd = new ProgressDialog(UpdateStock.this);
        pd.setCancelable(false);
        pd.setMessage("Fetching Details");
        pd.show();

        if(eAssetId.getText().toString().isEmpty())
        {
            eAssetId.setError("cannot be empty");
            return;
        }
        final String s = eAssetId.getText().toString().trim();
        FirebaseDatabase.getInstance().getReference("Assets").child(s).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                tAssetName.setText("Name: ");
                tAssetLocation.setText("Location: ");
                tAssetType.setText("Type: ");
                tAssetStockCount.setText("Stock: ");

                if(!dataSnapshot.exists())
                {
                    id=null;
                    name=null;
                    type=null;
                    location=null;
                    stockCount=null;
                    pd.dismiss();
                    Toast.makeText(UpdateStock.this,"Asset with the ID doesn't exist!",Toast.LENGTH_LONG).show();
                    return;
                }
                id=s;
                name = dataSnapshot.child("Name").getValue(String.class);
                location = dataSnapshot.child("Location").getValue(String.class);
                type = dataSnapshot.child("Type").getValue(String.class);
                stockCount = dataSnapshot.child("StockCount").getValue(String.class);

                tAssetName.append(name);
                tAssetLocation.append(location);
                tAssetType.append(type);
                tAssetStockCount.append(stockCount);
                pd.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    public void addStock(View view)
    {
        if(id==null)
        {
            Toast.makeText(UpdateStock.this,"No asset selected", Toast.LENGTH_LONG).show();
            return;
        }

        DialogFragment fragment = new AddStockFragment();

        Bundle args = new Bundle();
        args.putString("Id",id);
        args.putString("StockStatus",stockCount);
        fragment.setArguments(args);
        fragment.show(getSupportFragmentManager(),"addStock");
    }


    public void removeStock(View vIew)
    {
        if(id==null)
        {
            Toast.makeText(UpdateStock.this,"No asset selected", Toast.LENGTH_LONG).show();
            return;
        }

        DialogFragment fragment = new RemoveStockFragment();

        Bundle args = new Bundle();
        args.putString("Id",id);
        args.putString("StockStatus",stockCount);
        fragment.setArguments(args);
        fragment.show(getSupportFragmentManager(),"removeStock");
    }

    public void changeLocation(View view)
    {
        if(id==null)
        {
            Toast.makeText(UpdateStock.this,"No asset selected", Toast.LENGTH_LONG).show();
            return;
        }

        DialogFragment fragment = new ChangeLocation();

        Bundle args = new Bundle();
        args.putString("Id",id);
        args.putString("Location",location);
        fragment.setArguments(args);
        fragment.show(getSupportFragmentManager(),"changeLocation");
    }
}


