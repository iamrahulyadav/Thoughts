package com.r3Tech.Thoughts.Utils;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.r3Tech.Thoughts.R;

// If you like this app
// 0. Be social
// 1. Rate on play store
// 2. Tell your friends
// 3. Give us your feedback
// 4. Buy PRO version


public class dlgDefaultMenuAlert extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder _builder = new AlertDialog.Builder(getActivity());
        _builder.setTitle("If you like this app ...")
                .setItems(R.array.arr_default_menu_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getActivity().getApplicationContext(), String.valueOf(which), Toast.LENGTH_LONG).show();
                        switch (which) {
//                            case 0:    // 0. Be social
//                                clsGeneral.shareAndSupport(getActivity().getApplicationContext(), "social");
//                                break;
                            case 0:    // 1. Rate on play store
                                clsGeneral.shareAndSupport(getActivity().getApplicationContext(), "rate");
                                break;
                            case 1:    // 2. Tell your friends
                                clsGeneral.shareAndSupport(getActivity().getApplicationContext(), "friends");
                                break;
                            case 2:   // 3. Give us your feedback
                                clsGeneral.shareAndSupport(getActivity().getApplicationContext(), "feedback");
                                break;
                        }
                    }
                });
        return _builder.create();
    }
}
