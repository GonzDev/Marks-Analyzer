package com.gonz.upv.marksanalyzer.ui.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gonz.upv.marksanalyzer.R;
import com.gonz.upv.marksanalyzer.ui.activity.ReportActivity;

public class AboutDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.dialog_about_title);
        alertDialogBuilder.setMessage(R.string.dialog_about_message);
        alertDialogBuilder.setPositiveButton(R.string.dialog_about_button_text,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog dialog = alertDialogBuilder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                b.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_primary));
            }
        });

        return dialog;
    }
}
