package com.example.assettracking;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class AlreadyExistsDialog extends DialogFragment {

    public interface  AlreadyExistsDialogListner{
        public void onDialogPositiveClick(DialogFragment dialogFragment);
        public void onDialogNegativeClick(DialogFragment dialogFragment);

    }

    AlreadyExistsDialogListner listner;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try
        {
            listner = (AlreadyExistsDialogListner)context;

        }catch (ClassCastException e)
        {
            throw new ClassCastException(getActivity().toString() + "must implement AlreadyexitsDialogListner");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.already_exists)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        listner.onDialogPositiveClick(AlreadyExistsDialog.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        listner.onDialogNegativeClick(AlreadyExistsDialog.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();


    }
}
