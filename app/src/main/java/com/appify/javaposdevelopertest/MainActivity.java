package com.appify.javaposdevelopertest;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.appify.javaposdevelopertest.databinding.ActivityMainBinding;
import com.appify.javaposdevelopertest.model.Transaction;
import com.appify.javaposdevelopertest.ui.TransactionAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private TabLayout tabLayout;
    private TransactionAdapter adapter;
    private List<Transaction> transactionList;
    private List<Transaction> allTransactions;
    private List<Transaction> creditTransactions;
    private List<Transaction> debitTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Change the status bar icon colours to black
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setupSearchView();

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tabLayout = binding.tabLayout;

        setupTabs();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    Log.d("tab_select", "Total: "+transactionList.size());
                    transactionList = allTransactions;
                } else if (tab.getPosition() == 1) {
                    Log.d("tab_select", "Credit: "+creditTransactions.size());
                    transactionList = creditTransactions;
                } else {
                    Log.d("tab_select", "Debit: "+debitTransactions.size());
                    transactionList = debitTransactions;
                }
                adapter.setTransactions(transactionList);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Transaction> filteredList = new ArrayList<>();

                for (Transaction transaction: transactionList) {
                    if (transaction.getTransactionTypeName().toLowerCase().contains(newText.toLowerCase())) {
                        filteredList.add(transaction);
                    }
                }

                adapter.setFilterList(filteredList);
                return false;
            }
        });

        readFile();
    }

    private void setupTabs() {
        TabLayout.Tab allTab = tabLayout.newTab();
        allTab.setText("All");

        TabLayout.Tab creditTab = tabLayout.newTab();
        creditTab.setText("Credit");

        TabLayout.Tab debitTab = tabLayout.newTab();
        debitTab.setText("Debit");

        tabLayout.addTab(allTab);
        tabLayout.addTab(creditTab);
        tabLayout.addTab(debitTab);
        tabLayout.setSelectedTabIndicatorGravity(TabLayout.INDICATOR_GRAVITY_BOTTOM);

        //Set the tab colors
        LinearLayout tabsContainer = (LinearLayout) tabLayout.getChildAt(0);
        for (int i=0; i<tabLayout.getTabCount(); i++) {
            LinearLayout item = (LinearLayout) tabsContainer.getChildAt(i);
            TextView tv = (TextView) item.getChildAt(1);

            if (i==0)
                tv.setTextColor(Color.BLACK);
            else if (i==1)
                tv.setTextColor(Color.GREEN);
            else
                tv.setTextColor(Color.RED);
        }
    }

    private void setupSearchView() {
        //Change the font sie of the searchview
        SearchView searchView = binding.searchBar;
        LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
        autoComplete.setTextSize(14);
    }

    private void displayTransactions(List<Transaction> transactions) {
        adapter = new TransactionAdapter(transactions);
        binding.recyclerview.setAdapter(adapter);
    }

    private void readFile() {
        Gson gson = new Gson();
        try {
            InputStream raw =  getResources().openRawResource(R.raw.jsonformatter);
            Reader rd = new BufferedReader(new InputStreamReader(raw));
            Transaction[] transactions = gson.fromJson(rd, Transaction[].class);
            transactionList = Arrays.asList(transactions);
            allTransactions = Arrays.asList(transactions);
            displayTransactions(transactionList);

            creditTransactions = new ArrayList<>();
            debitTransactions = new ArrayList<>();
            for (Transaction transaction: transactionList) {
                if (transaction.isCredit()) {
                    creditTransactions.add(transaction);
                } else {
                    debitTransactions.add(transaction);
                }
            }
        } catch (Exception e) {
            Log.d("error", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}