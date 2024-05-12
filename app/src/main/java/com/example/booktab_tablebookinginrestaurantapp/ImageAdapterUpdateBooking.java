package com.example.booktab_tablebookinginrestaurantapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImageAdapterUpdateBooking extends RecyclerView.Adapter <ImageAdapterUpdateBooking.ImageViewHolder>{
    private Context mContext;
    private List<UserBooking> mUploads;
    private OnItemClickListener mListener;

    public ImageAdapterUpdateBooking(Context context, List<UserBooking> userUploads){
        mContext= context;
        mUploads= userUploads;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.image_item_update_booking, parent, false);
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

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textViewName;
        public TextView textViewPhone;
        public TextView textViewDate;
        public TextView textViewTime;
        public TextView textViewPerson;
        public Button btn_update;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName= itemView.findViewById(R.id.image_view_name2);
            textViewPhone= itemView.findViewById(R.id.image_view_phone2);
            textViewPerson= itemView.findViewById(R.id.image_view_person2);
            textViewDate= itemView.findViewById(R.id.image_view_date2);
            textViewTime= itemView.findViewById(R.id.image_view_time2);
            btn_update= itemView.findViewById(R.id.btn_update);

            btn_update.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }


    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }
}
