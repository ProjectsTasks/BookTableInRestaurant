package com.example.booktab_tablebookinginrestaurantapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImageAdapterViewBooking extends RecyclerView.Adapter <ImageAdapterViewBooking.ImageViewHolder>{
    private Context mContext;
    private List<UserBooking> mUploads;

    public ImageAdapterViewBooking(Context context, List<UserBooking> userUploads){
        mContext= context;
        mUploads= userUploads;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.image_item_view_booking, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        UserBooking uploadCurrent= mUploads.get(position);
        holder.textViewName.setText("Customer Name: "+uploadCurrent.getBookName());
        holder.textViewPhone.setText("Customer Contact Number: "+uploadCurrent.getBookPhone());
        holder.textViewPerson.setText("Reservation is:  "+uploadCurrent.getBookTable());
        holder.textViewDate.setText("Booked Date: "+uploadCurrent.getBookDate());
        holder.textViewTime.setText("Booked Time: "+uploadCurrent.getBookTime());

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public TextView textViewPhone;
        public TextView textViewDate;
        public TextView textViewTime;
        public TextView textViewPerson;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName= itemView.findViewById(R.id.image_view_name);
            textViewPhone= itemView.findViewById(R.id.image_view_phone);
            textViewPerson= itemView.findViewById(R.id.image_view_person);
            textViewDate= itemView.findViewById(R.id.image_view_date);
            textViewTime= itemView.findViewById(R.id.image_view_time);


        }


    }
}
