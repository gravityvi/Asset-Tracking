package com.example.assettracking;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

public class AddStockFragment extends DialogFragment {

    private EditText eStockAdd;
    private TextView tId;
    private TextView tStockStatus;
    private Button bAddStock;
    private String id;
    private String status;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

            Bundle margs = getArguments();
            id = margs.getString("Id");
            status=margs.getString("StockStatus");
            Log.e("mess","hello "+id);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_stock,null);

        eStockAdd =view.findViewById(R.id.eStocktoAdd);
        bAddStock=view.findViewById(R.id.bAdd);
        tStockStatus=view.findViewById(R.id.tStockStatus);
        tId=view.findViewById(R.id.tId);

        builder.setView(view);

        bAddStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eStockAdd.getText().toString().isEmpty())
                {
                    eStockAdd.setError("Cannot be empty");
                }
                int cnt = Integer.valueOf(status);
                cnt+=Integer.valueOf(eStockAdd.getText().toString());
                FirebaseDatabase.getInstance().getReference("Assets").child(id).child("StockCount").setValue(Integer.toString(cnt));
                dismiss();
            }
        });



        tId.append(id);
        tStockStatus.append(status);

        return builder.create();

    }
}
