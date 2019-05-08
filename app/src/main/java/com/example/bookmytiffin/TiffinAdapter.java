package com.example.bookmytiffin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class TiffinAdapter extends RecyclerView.Adapter<TiffinAdapter.MyViewHolder> {

    private Context mContext;
    private List<TiffinCentre> tiffinCentreList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public  void setOnItemClickListner(OnItemClickListener listener){
        mListener = listener;
    }

    public TiffinAdapter(Context mContext, List<TiffinCentre> tiffinCentreList) {
        this.mContext = mContext;
        this.tiffinCentreList = tiffinCentreList;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tiffin_card, viewGroup, false);

        return new MyViewHolder(itemView,mListener);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int i) {

        TiffinCentre tiffinCentre = tiffinCentreList.get(i);
        holder.name.setText(tiffinCentre.getName());
        holder.address.setText(tiffinCentre.getAddress());

        // loading album cover using Glide library
        Glide.with(mContext).load(tiffinCentre.getThumbnail()).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return tiffinCentreList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {            public ImageView thumbnail;
        public TextView name, address;
        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            name =  itemView.findViewById(R.id.title);
            address =  itemView.findViewById(R.id.count);
            thumbnail =  itemView.findViewById(R.id.title2);

            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);

                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);

                        }
                    }
                }
            });

        }


    }

}
