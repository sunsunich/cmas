package org.cmas.android.ui.file;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import org.cmas.ecards.R;
import org.cmas.ecards.databinding.ImageItemBinding;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder> {

    private final List<Bitmap> images = new ArrayList<>();
    @Nullable
    private final ImageDeletionListener listener;

    public ImagesAdapter(@Nullable ImageDeletionListener listener) {
        this.listener = listener;
    }

    public void clear() {
        images.clear();
        notifyDataSetChanged();
    }

    public void addImage(Bitmap image) {
        images.add(image);
        notifyDataSetChanged();
    }

    private void removeImage(int position) {
        if (listener != null) {
            listener.onDeleteImage(position);
        }
        images.remove(position);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ImageItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.image_item, parent, false);
        return new ImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        Bitmap bitmap = images.get(i);
        imageViewHolder.userImage.setImageBitmap(bitmap);
        imageViewHolder.deleteButton.setOnClickListener((v -> {
            removeImage(i);
        }));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        final ImageView deleteButton;
        final ImageView userImage;

        ImageViewHolder(@NonNull ImageItemBinding binding) {
            super(binding.getRoot());
            deleteButton = binding.deleteImage;
            userImage = binding.image;
        }
    }

    public interface ImageDeletionListener {
        void onDeleteImage(int position);
    }
}
