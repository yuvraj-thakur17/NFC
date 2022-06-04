package com.example.nfc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    private TextView jackDanielsPrice;
    private TextView johnnieWalkerPrice;
    private TextView heinekenPrice;

    private TextView totalAmountDisplay;

    private Button jackDanielsBuyBtn;
    private Button johnnieWalkerBuyBtn;
    private Button heinekenBuyBtn;

    private int currentBillAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        jackDanielsPrice=findViewById(R.id.tvjackDanielsPrice);
        johnnieWalkerPrice=findViewById(R.id.tvjohnnieWalkerPrice);
        heinekenPrice=findViewById(R.id.tvheinekenPrice);

        totalAmountDisplay=findViewById(R.id.tvTotalAmountDisplay);

        jackDanielsBuyBtn=(Button) findViewById(R.id.btnjackDanielsBUY);
        johnnieWalkerBuyBtn=(Button) findViewById(R.id.btnjohnnieWalkerBUY);
        heinekenBuyBtn=(Button) findViewById(R.id.btnheinekenBUY);

        jackDanielsBuyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                process(jackDanielsPrice.getText().toString());
            }
        });

        johnnieWalkerBuyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                process(johnnieWalkerPrice.getText().toString());
            }
        });

        heinekenBuyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                process(heinekenPrice.getText().toString());
            }
        });
    }
    public void process(String productPrice){
        try {
            int amount=Integer.parseInt(productPrice);
            currentBillAmount=currentBillAmount+amount;
            totalAmountDisplay.setText("Your total amount is Rs. "+currentBillAmount);
        }catch (NumberFormatException ex){
            ex.printStackTrace();
        }
    }

}