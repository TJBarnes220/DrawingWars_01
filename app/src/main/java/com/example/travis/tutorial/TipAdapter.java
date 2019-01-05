package com.example.travis.tutorial;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.travis.battledisplay.R;

import java.util.ArrayList;
import java.util.Random;

public class TipAdapter extends  RecyclerView.Adapter<TipAdapter.ViewHolder>  {
    private ArrayList<Tip> hints;
    private LayoutInflater mLayoutInflater;

    public TipAdapter(Context context, ArrayList<Tip> data) {
        hints = data;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mLayoutInflater.inflate(R.layout.tip_list, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Tip current = hints.get(position);
        viewHolder.text.setText(current.getText());
        viewHolder.image.setImageResource(current.getImageId());
        Random rNumber = new Random();
        int choice = rNumber.nextInt(4) + 1;
        switch (choice) {
            case (1):
                viewHolder.text.setBackgroundResource(R.drawable.textbox03);
                break;
            case (2):
                viewHolder.text.setBackgroundResource(R.drawable.textbox04);
                break;
            case (3):
                viewHolder.text.setBackgroundResource(R.drawable.textbox05);
                break;
            case (4):
                viewHolder.text.setBackgroundResource(R.drawable.textbox06);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return hints.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.image);
            this.text = itemView.findViewById(R.id.text);
        }
    }
}