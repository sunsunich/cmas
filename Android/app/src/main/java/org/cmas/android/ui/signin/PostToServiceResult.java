package org.cmas.android.ui.signin;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;

public class PostToServiceResult implements Serializable {

    public final boolean success;

    @Nullable
    public final String errorCode;

    public final List<String> initialFilePaths;

    public PostToServiceResult(boolean success, @Nullable String errorCode, List<String> initialFilePaths) {
        this.success = success;
        this.errorCode = errorCode;
        this.initialFilePaths = initialFilePaths;
    }
}
