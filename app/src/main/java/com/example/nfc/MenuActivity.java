package com.example.nfc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity implements Listener {

    public static final String TAG = MenuActivity.class.getSimpleName();

    private TextView jackDanielsPrice;
    private TextView johnnieWalkerPrice;
    private TextView heinekenPrice;

    private TextView totalAmountDisplay;

    private Button jackDanielsBuyBtn;
    private Button johnnieWalkerBuyBtn;
    private Button heinekenBuyBtn;

    private int currentBillAmount;

    private WriteAmontToTagFragment mwriteAmontToTagFragment;

    private boolean isDialogDisplayed = false;
    private boolean isWrite = false;

    private NfcAdapter mNfcAdapter;

    private String finalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initViews();
        initNFC();
    }

    private void initViews() {


        jackDanielsPrice = findViewById(R.id.tvjackDanielsPrice);
        johnnieWalkerPrice = findViewById(R.id.tvjohnnieWalkerPrice);
        heinekenPrice = findViewById(R.id.tvheinekenPrice);

        totalAmountDisplay = findViewById(R.id.tvTotalAmountDisplay);

        jackDanielsBuyBtn = (Button) findViewById(R.id.btnjackDanielsBUY);
        johnnieWalkerBuyBtn = (Button) findViewById(R.id.btnjohnnieWalkerBUY);
        heinekenBuyBtn = (Button) findViewById(R.id.btnheinekenBUY);

        jackDanielsBuyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = Integer.parseInt(jackDanielsPrice.getText().toString());
                currentBillAmount = currentBillAmount + amount;
                Intent intent=new Intent(MenuActivity.this,AddAmountToTag.class);
                startActivity(intent);
            }
        });

        johnnieWalkerBuyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                process(johnnieWalkerPrice.getText().toString());
            }
        });

        heinekenBuyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                process(heinekenPrice.getText().toString());
            }
        });
    }


    public void process(String productPrice) {
        try {
            int amount = Integer.parseInt(productPrice);
            currentBillAmount = currentBillAmount + amount;
            totalAmountDisplay.setText("Your total amount is Rs. " + currentBillAmount);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    public String sendAmountToAnotherActivity(){

        return Integer.toString(currentBillAmount);

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

        if (tag != null) {
            Toast.makeText(this, getString(R.string.message_tag_detected), Toast.LENGTH_SHORT).show();
            Ndef ndef = Ndef.get(tag);

            if (isDialogDisplayed) {

                if (isWrite) {

                    String messageToWrite = Integer.toString(currentBillAmount);
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