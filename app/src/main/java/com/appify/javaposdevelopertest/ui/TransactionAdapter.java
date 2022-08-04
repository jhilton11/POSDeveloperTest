package com.appify.javaposdevelopertest.ui;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appify.javaposdevelopertest.R;
import com.appify.javaposdevelopertest.databinding.TransactionRowLayoutBinding;
import com.appify.javaposdevelopertest.model.Transaction;
import com.appify.javaposdevelopertest.utils.UtilClass;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.Holder>{
    private List<Transaction> transactions;
    private static final String NAIRA = "₦";
    private Context context;

    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        TransactionRowLayoutBinding binding = TransactionRowLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Transaction transaction = transactions.get(position);

        if (transaction.isCredit()) {
            holder.icon.setImageResource(R.drawable.ic_action_credit);
            holder.icon.setBackgroundColor(ContextCompat.getColor(context, R.color.light_green));
        } else {
            holder.icon.setImageResource(R.drawable.ic_action_debit);
            holder.icon.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red));
        }

        holder.transactionType.setText(transaction.getTransactionTypeName());

        String amount = NAIRA+ transaction.getAmount();
        if (!transaction.isCredit()) {
            amount = "-"+amount;
        }
        holder.amount.setText(amount);

        String formattedDate = UtilClass.getDate(transaction.getTransactionDate());
        holder.date.setText(formattedDate);

        String status = transaction.getStatusName();
        if (status.equalsIgnoreCase("Successful")) {
            holder.status.setTextColor(Color.GREEN);
        } else if (status.equalsIgnoreCase("Failed")) {
            holder.status.setTextColor(Color.RED);
        }
        holder.status.setText(status);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    public void setFilterList(List<Transaction> filteredList) {
        transactions = filteredList;
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView transactionType;
        TextView amount;
        TextView date;
        TextView status;

        public Holder(@NonNull TransactionRowLayoutBinding binding) {
            super(binding.getRoot());
            icon = binding.transactionTypeIcon;
            transactionType = binding.transactionTypeName;
            amount = binding.amount;
            date = binding.transactionDate;
            status = binding.status;
        }
    }
}
