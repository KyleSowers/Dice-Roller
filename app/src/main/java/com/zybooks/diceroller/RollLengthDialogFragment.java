package com.zybooks.diceroller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class RollLengthDialogFragment extends DialogFragment {

    public  interface OnRollLengthSelectedListener {
        void onRollLengthClick(int which);
    }

    private OnRollLengthSelectedListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.pick_roll_length);
        builder.setItems(R.array.length_array, (dialog, which) -> {
            // 'which' is the zero-based index position chosen
            mListener.onRollLengthClick(which);
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (OnRollLengthSelectedListener) context;
    }
}
