package com.example.nfc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddAmountToTag extends AppCompatActivity implements Listener {

    public static final String TAG = MenuActivity.class.getSimpleName();

    private Button mBtWrite;
    private EditText mEtItemCode;
//    private EditText mEtItemdescription;

    private WriteAmontToTagFragment mwriteAmontToTagFragment;

    private boolean isDialogDisplayed = false;
    private boolean isWrite = false;

    private NfcAdapter mNfcAdapter;

    MenuActivity menuActivity=new MenuActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_amount_to_tag);
//        initViews();
        initNFC();
        showWriteFragment();
    }

    private void initViews() {

//        mEtItemCode = (EditText) findViewById(R.id.newetItemNumber);
//        mBtWrite = (Button) findViewById(R.id.newbtnWriteItemCode);
//        mEtItemdescription=(EditText)findViewById(R.id.etItemDescription);

//        mBtWrite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(mEtItemCode.getText().toString().trim().length()!=0) {
//                    showWriteFragment();
//                }
//                else {
//                    Toast.makeText(AddAmountToTag.this,"Invalid Phone Number",Toast.LENGTH_LONG).show();
//                }
//            }
//        });

    }

    private void initNFC() {

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    private void showWriteFragment() {

        isWrite = true;

        mwriteAmontToTagFragment = (WriteAmontToTagFragment) getFragmentManager().findFragmentByTag(WriteAmontToTagFragment.TAG);

        if (mwriteAmontToTagFragment == null) {

            mwriteAmontToTagFragment = WriteAmontToTagFragment.newInstance();
        }
        mwriteAmontToTagFragment.show(getFragmentManager(), WriteAmontToTagFragment.TAG);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Log.d(TAG, "onNewIntent: " + intent.getAction());

        String getamo = menuActivity.sendAmountToAnotherActivity();
        SharedPreferences shrd= getSharedPreferences("demo",MODE_PRIVATE);
        SharedPreferences.Editor editor=shrd.edit();
        editor.putString("str",getamo);
        editor.apply();

        if (tag != null) {
            Toast.makeText(this, getString(R.string.message_tag_detected), Toast.LENGTH_SHORT).show();
            Ndef ndef = Ndef.get(tag);

            if (isDialogDisplayed) {

                if (isWrite) {

//                    MenuActivity menuActivity = new MenuActivity();
                    String newMessageToWrite = menuActivity.sendAmountToAnotherActivity();
//                    String messageToWrite = newMessageToWrite.trim();
                    String messageToWrite = shrd.getString("str","zero");
//                    String messageToWrite = mEtItemCode.getText().toString().trim();
                    //String messageToWrite[] = {mEtMessage.getText().toString(),"JW RED LABEL"};
                    mwriteAmontToTagFragment = (WriteAmontToTagFragment) getFragmentManager().findFragmentByTag(WriteAmontToTagFragment.TAG);
                    mwriteAmontToTagFragment.onNfcDetected(ndef, messageToWrite);

                }
            }
        }
    }

    @Override
    public void onDialogDisplayed() {
        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {
        isDialogDisplayed = false;
        isWrite = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected, tagDetected, ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);
    }
}