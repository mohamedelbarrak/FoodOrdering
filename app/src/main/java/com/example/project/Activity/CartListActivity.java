package com.example.project.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Adaptor.CartListAdapter;
import com.example.project.Adaptor.CatregoryAdaptor;
import com.example.project.Helper.ManagementCart;
import com.example.project.Interface.ChangeNumberItemsListener;
import com.example.project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CartListActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewList;
    private ManagementCart managementCart;
    TextView totalFeeTxt, taxTxt, deliveryTxt, totalTxt, emptyTxt;
    private double tax;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        managementCart = new ManagementCart(this);

        initView();
        initList();
        CalculateCart();
        BottomNavigation();
        CheckOut();
    }

    private void CheckOut() {
        TextView CheckOut;
        CheckOut = findViewById(R.id.textView16);
        CheckOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(totalFeeTxt.getText().equals("0.0 Dh")){

                    Toast toast1 = Toast.makeText(getApplicationContext(), "Votre panier est vide.", Toast.LENGTH_SHORT);
                    toast1.setGravity(Gravity.BOTTOM,0,130);
                    toast1.show();
                }else{

                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(CartListActivity.this);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {//setNegativeButton annuler
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //set what would happen when positive button is clicked
                            Toast toast2 = Toast.makeText(getApplicationContext(),"Merci de magasiner avec nous.",Toast.LENGTH_LONG);
                            toast2.setGravity(Gravity.BOTTOM,0,130);
                            toast2.show();
                            //finish();
                            managementCart.deleteAll();
                            startActivity(new Intent(CartListActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                    builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title).show();
                }
            }
        });
    }

    private void BottomNavigation() {
        //FloatingActionButton floatingActionButton = findViewById(R.id.cartBtn);
        LinearLayout homeBtn = findViewById(R.id.home_Btn);
        /*
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartListActivity.this, CartListActivity.class));
            }
        });
        */

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartListActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void initView() {
        recyclerViewList = findViewById(R.id.recyclerView);
        totalFeeTxt = findViewById(R.id.totalFeeTxt);
        taxTxt = findViewById(R.id.taxTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        totalTxt = findViewById(R.id.totalTxt);
        emptyTxt = findViewById(R.id.emptyTxt);
        scrollView = findViewById(R.id.scrollView3);
        recyclerViewList = findViewById(R.id.cartView);
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        adapter = new CartListAdapter(managementCart.getListCart(),this,new ChangeNumberItemsListener() {
            @Override
            public void changed() {
                CalculateCart();
            }
        });

        recyclerViewList.setAdapter(adapter);
        if (managementCart.getListCart().isEmpty()) {
            //emptyTxt.setVisibility(View.VISIBLE);
            //scrollView.setVisibility(View.GONE);
        } else {
            //emptyTxt.setVisibility(View.GONE);
            //scrollView.setVisibility(View.VISIBLE);
        }
        emptyTxt.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
    }

    private void
    CalculateCart() {
        double percentTax = 0.02;
        double delivery = 10;

        tax = Math.round((managementCart.getTotalFee() * percentTax) * 100) / 100;
        double total = Math.round((managementCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(managementCart.getTotalFee() * 100) / 100;

        totalFeeTxt.setText(itemTotal + " Dh");
        taxTxt.setText(tax + " Dh");
        deliveryTxt.setText(delivery + " Dh");
        totalTxt.setText(total + " Dh");

    }
}