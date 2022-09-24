package org.cmas.android.ui.file;

import android.graphics.Bitmap;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import org.cmas.Globals;
import org.cmas.util.android.CreateImageThumbnailInput;
import org.cmas.util.android.ImageUtil;
import org.cmas.util.android.TaskViewModel;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PathToThumbnailStorage extends TaskViewModel<Integer, Void, List<Bitmap>> implements ImagesAdapter.ImageDeletionListener {

    private final List<ImagePathAndSize> images = new ArrayList<>();

    @Nullable
    private String lastAddedImagePath;

    private long totalFilesSizeBytes;

    @Nullable
    @Override
    @WorkerThread
    protected List<Bitmap> runInBackground(Integer thumbnailMaxWidth) {
        List<Bitmap> result = new ArrayList<>(images.size());
        for (ImagePathAndSize image : images) {
            result.add(ImageUtil.createThumbnail(
                    new CreateImageThumbnailInput(false, image.path, thumbnailMaxWidth, thumbnailMaxWidth)
            ));
        }
        return result;
    }

    @UiThread
    public void resetImages(List<String> imagePaths) {
        images.clear();
        for (String imagePath : imagePaths) {
            addImage(imagePath);
        }
    }

    @UiThread
    public void addImage(String absolutePath) {
        ImagePathAndSize item = new ImagePathAndSize(absolutePath);
        images.add(item);
        totalFilesSizeBytes += item.fileSizeBytes;
    }

    @UiThread
    public List<ImagePathAndSize> getImages() {
        return images;
    }

    @UiThread
    @Override
    public void onDeleteImage(int position) {
        if (position < images.size()) {
            ImagePathAndSize item = images.remove(position);
            totalFilesSizeBytes -= item.fileSizeBytes;
        }
    }

    @UiThread
    public boolean validateTotalSize() {
        return totalFilesSizeBytes < Globals.MAX_IMAGE_SIZE;
    }

    @UiThread
    public void setLastAddedImagePath(@Nullable String lastAddedImagePath) {
        this.lastAddedImagePath = lastAddedImagePath;
    }

    @UiThread
    @Nullable
    public String getLastAddedImagePath() {
        return lastAddedImagePath;
    }
}
