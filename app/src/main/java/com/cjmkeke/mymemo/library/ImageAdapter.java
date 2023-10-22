package com.cjmkeke.mymemo.library;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cjmkeke.mymemo.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<Uri> imageUris;

    public ImageAdapter(List<Uri> imageUris) {
        this.imageUris = imageUris;
    }

    // 뷰 홀더 생성 및 레이아웃 연결
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_images_view, parent, false);
        return new ViewHolder(view);
    }

    // 데이터와 뷰 홀더를 연결하여 데이터 표시
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri imageUri = imageUris.get(position);
        holder.imageViewItem.setImageURI(imageUri);
    }

    // 데이터 아이템 개수 반환
    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    // 뷰 홀더 클래스
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewItem = itemView.findViewById(R.id.iv_images);
        }
    }
}