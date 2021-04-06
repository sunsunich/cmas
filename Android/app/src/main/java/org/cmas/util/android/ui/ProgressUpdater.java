package org.cmas.util.android.ui;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.cmas.cmas_flutter.R;
import org.cmas.android.MainApplication;
import org.cmas.util.TaskProgressUpdate;

public class ProgressUpdater {

    private final TextView updateStepText;
    private final ProgressBar updateProgressBar;

    private ObjectAnimator lastProgressAnimation = null;

    private Integer lastProgressUpdate = 0;

    public ProgressUpdater(final TextView updateStepText,
                           final ProgressBar updateProgressBar) {
        this.updateProgressBar = updateProgressBar;
        this.updateStepText = updateStepText;
    }

    public void reportProgress(final TaskProgressUpdate progressUpdate) {
        int progress = progressUpdate.getProgress();
        String step = progressUpdate.getCurrentStep();
        Context context = MainApplication.getAppContext();
        if (updateProgressBar != null) {
            if (progressUpdate.toHideProgressBar()) {
                updateProgressBar.setVisibility(View.INVISIBLE);
                updateStepText.setTextColor(context.getColor(R.color.text_primary));
            } else {
                if (progress > 0) {
                    updateProgressBar.setMax(TaskProgressUpdate.PROGRESS_VAL_MAX);
                    if (lastProgressAnimation != null) {
                        lastProgressAnimation.cancel();
                    }
                    if (lastProgressUpdate > progress) {
                        lastProgressUpdate = 0;
                    }
                    long durationMsec = progressUpdate.getExpectedDurationMilliSec();
                    lastProgressAnimation = setProgressAnimation(updateProgressBar,
                                                                 new DecelerateInterpolator(),
                                                                 lastProgressUpdate,
                                                                 progress,
                                                                 durationMsec);
                    lastProgressUpdate = progress;
                    updateProgressBar.setVisibility(View.VISIBLE);
                    updateStepText.setTextColor(context.getColor(R.color.text_primary));
                } else {
                    updateProgressBar.setVisibility(View.INVISIBLE);
                    updateStepText.setTextColor(context.getColor(R.color.negative));
                }
            }
        }
        if (updateStepText != null) {
            if (progressUpdate.isErrorDetected()) {
                updateStepText.setTextColor(context.getColor(R.color.negative));
            }
            if (step == null) {
                updateStepText.setVisibility(View.INVISIBLE);
            } else {
                updateStepText.setVisibility(View.VISIBLE);
                updateStepText.setText(step);
            }
        }
    }

    private static final String PROGRESS_KEY = "progress";
    private static final int SMOOTH_ANIMATION_SCALING_FACTOR = 100;

    private static int getScaledProgress(final int rawValue) {
        return rawValue * SMOOTH_ANIMATION_SCALING_FACTOR;
    }

    private static ObjectAnimator setProgressAnimation(final ProgressBar pb,
                                                       final TimeInterpolator interpolator,
                                                       final int progressFrom,
                                                       final int progressTo,
                                                       final long durationMilliSec) {

        ObjectAnimator animation = ObjectAnimator.ofInt(pb,
                                                        PROGRESS_KEY,
                                                        getScaledProgress(progressFrom),
                                                        getScaledProgress(progressTo));
        animation.setDuration(durationMilliSec);
        animation.setInterpolator(interpolator);
        animation.setAutoCancel(true);
        animation.start();
        return animation;
    }
}
