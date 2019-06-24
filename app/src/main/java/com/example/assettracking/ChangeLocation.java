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

public class ChangeLocation extends DialogFragment {

    private EditText eLocation;
    private TextView tId;
    private TextView tcurrentLocation;
    private Button bchange;
    private String id;
    private String location;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle margs = getArguments();
        id = margs.getString("Id");
        location=margs.getString("Location");
        Log.e("mess","hello "+id);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.change_location,null);

        eLocation =view.findViewById(R.id.eLocation);
        bchange=view.findViewById(R.id.bChange);
        tcurrentLocation=view.findViewById(R.id.tcurrentLocation);
        tId=view.findViewById(R.id.tId);

        builder.setView(view);

        bchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eLocation.getText().toString().isEmpty())
                {
                    eLocation.setError("Cannot be empty");
                }
                FirebaseDatabase.getInstance().getReference("Assets").child(id).child("Location").setValue(eLocation.getText().toString());
                dismiss();
            }
        });



        tId.append(id);
        tcurrentLocation.append(location);

        return builder.create();

    }
}
