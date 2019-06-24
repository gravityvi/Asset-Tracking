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

public class RemoveStockFragment extends DialogFragment {

    private EditText eStockRemove;
    private TextView tId;
    private TextView tStockStatus;
    private Button bRemoveStock;
    private String id;
    private String status;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle margs = getArguments();
        id = margs.getString("Id");
        status=margs.getString("StockStatus");


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.remove_stock,null);

        eStockRemove =view.findViewById(R.id.eStockToRemove);
        bRemoveStock=view.findViewById(R.id.bRemove);
        tStockStatus=view.findViewById(R.id.tStockStatus);
        tId=view.findViewById(R.id.tId);

        builder.setView(view);

        bRemoveStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eStockRemove.getText().toString().isEmpty())
                {
                    eStockRemove.setError("Cannot be empty");
                }
                int cnt = Integer.valueOf(status);
                cnt-=Integer.valueOf(eStockRemove.getText().toString());
                cnt=(int)Math.max(0,cnt);
                FirebaseDatabase.getInstance().getReference("Assets").child(id).child("StockCount").setValue(Integer.toString(cnt));
                dismiss();
            }
        });



        tId.append(id);
        tStockStatus.append(status);
        return builder.create();

    }
}
