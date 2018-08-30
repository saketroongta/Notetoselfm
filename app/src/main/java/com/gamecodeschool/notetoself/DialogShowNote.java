package com.gamecodeschool.notetoself;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogShowNote extends android.app.DialogFragment {
    private Note mNote;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_show_note,null);

        TextView txtTitle = (TextView) dialogView.findViewById(R.id.txtTiltle);
        TextView txtDescription = (TextView) dialogView.findViewById(R.id.txtDescription);
        txtTitle.setText(mNote.getmTitle());
        txtDescription.setText(mNote.getmDescription());

        ImageView ivimportant = (ImageView) dialogView.findViewById(R.id.imageViewImportant);
        ImageView ivTodo = (ImageView) dialogView.findViewById(R.id.imageViewTodo);
        ImageView ivIdea = (ImageView) dialogView.findViewById(R.id.imageViewIdea);

        if(!mNote.getmImportant()){
            ivimportant.setVisibility(View.GONE);
        }
        if (!mNote.getmTodo())
        {
            ivTodo.setVisibility(View.GONE);
        }
        if(!mNote.getmIdea())
        {
            ivIdea.setVisibility(View.GONE);
        }

        Button btnOK = (Button) dialogView.findViewById(R.id.btnOK);
        builder.setView(dialogView).setMessage("Your Note");
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }

    public void sendNoteSelected(Note noteSelected)
    {
        mNote = noteSelected;
    }


}
