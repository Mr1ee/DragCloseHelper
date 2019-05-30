package com.dragclosehelper.example;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * @Description: ImageAdapter
 * @Author: lihuayong
 * @CreateDate: 2019-05-29 16:16
 * @UpdateUser:
 * @UpdateDate: 2019-05-29 16:16
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private int[] mArray;
    private OnItemClickListener listener;

    public ImageAdapter(int[] array) {
        mArray = array;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mArray[position], position);
    }

    @Override
    public int getItemCount() {
        return mArray == null ? 0 : mArray.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_item);
        }

        void bind(@DrawableRes int resId, int position) {
            imageView.setImageResource(resId);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClick(v, position);
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        listener = l;
    }

    public interface OnItemClickListener {
        /**
         * set RecyclerView item view on click listener
         *
         * @param v        RecyclerView  item view
         * @param position item position
         */
        void onClick(View v, int position);
    }
}
