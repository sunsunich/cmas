package org.cmas.android.ui.file;

import android.graphics.Bitmap;
import org.cmas.ecards.R;
import org.apache.commons.lang3.tuple.Pair;
import org.cmas.util.android.CreateImageThumbnailInput;
import org.cmas.util.android.ImageUtil;
import org.cmas.util.android.StorageUtil;
import org.cmas.util.android.TaskViewModel;

import javax.annotation.Nullable;
import java.io.File;

public class CreateImageThumbnailViewModel extends TaskViewModel<CreateImageThumbnailInput, Void, Pair<Integer, Bitmap>> {

    @Nullable
    @Override
    protected Pair<Integer, Bitmap> runInBackground(CreateImageThumbnailInput arg) {
        String imagePath = arg.path;
        if (!StorageUtil.validateFileSize(imagePath)) {
            return returnError(arg.isPhoto, imagePath, R.string.error_photo_too_big, R.string.error_image_too_big);
        }
        Bitmap bitmap = ImageUtil.createThumbnail(arg);
        if (bitmap == null) {
            return returnError(arg.isPhoto,
                               imagePath,
                               R.string.error_photo_cannot_read,
                               R.string.error_image_cannot_read);
        }
        return Pair.of(null, bitmap);
    }

    private Pair<Integer, Bitmap> returnError(boolean isPhoto,
                                              String imagePath,
                                              int photoErrorCode,
                                              int imageErrorCode) {
        new File(imagePath).delete();
        if (isPhoto) {
            return Pair.of(photoErrorCode, null);
        } else {
            return Pair.of(imageErrorCode, null);
        }
    }
}
