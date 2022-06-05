package com.example.nfc;

import android.content.Context;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
import android.app.DialogFragment;

import com.skyfishjy.library.RippleBackground;

import java.io.IOException;

public class NFCReadFragment extends DialogFragment {
    public static final String TAG = NFCReadFragment.class.getSimpleName();

    public static NFCReadFragment newInstance() {

        return new NFCReadFragment();
    }

    private TextView mTvMessage;
    private Listener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_nfcread_fragment, container, false);
        initViews(view);

        // animation
        final RippleBackground rippleBackground = (RippleBackground) view.findViewById(R.id.animation);
        rippleBackground.setVisibility(View.VISIBLE);
        rippleBackground.startRippleAnimation();
        return view;
    }

    private void initViews(View view) {

        mTvMessage = (TextView) view.findViewById(R.id.tv_message);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (MainActivity) context;
        mListener.onDialogDisplayed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onDialogDismissed();
    }

    public void onNfcDetected(Ndef ndef) {

        readFromNFC(ndef);
    }

    private void readFromNFC(Ndef ndef) {

        try {
            ndef.connect();
            NdefMessage ndefMessage = ndef.getNdefMessage();
            if (ndefMessage != null) {
                String message = new String(ndefMessage.getRecords()[0].getPayload());
                if (message.trim().length() != 0) {

                    if (message.contains("%")) {

                        String[] itemData = message.split("\\%");

                        Log.d(TAG, "readFromNFC: " + message);
                        mTvMessage.setText("SKU " + itemData[0] + " " + itemData[1]);
                    } else {
                        Log.d(TAG, "readFromNFC: " + message);
                        mTvMessage.setText(message);
                    }
                }
            } else {
                mTvMessage.setText("No Data in the Tag..");
            }
            ndef.close();

        } catch (IOException | FormatException e) {
            e.printStackTrace();

        }
    }
}
