package org.cmas.util;

public class TaskProgressUpdate {

    public static final int PROGRESS_VAL_MAX = 100;

    private static final long DEFAULT_DURATION_MSEC = 1000L;
    private static final int PROGRESS_VAL_TO_HIDE_PROGRESS_BAR = -1;

    // Note: -1 would hide the progress bar
    private final int progress;

    private final String currentStep;

    // for smooth animation to target progress step.
    private final long expectedDurationMilliSec;

    private final boolean errorDetected;

    public static TaskProgressUpdate getProgressCompleteErrorInstance(final String errorMessage) {
        return new TaskProgressUpdate(PROGRESS_VAL_MAX, errorMessage, DEFAULT_DURATION_MSEC, true);
    }

    public static TaskProgressUpdate getProgressCompleteInstance(final String message) {
        return new TaskProgressUpdate(PROGRESS_VAL_MAX, message, DEFAULT_DURATION_MSEC);
    }

    public static TaskProgressUpdate getErrorWithNoProgressInstance(final String errorMessage) {
        return new TaskProgressUpdate(PROGRESS_VAL_TO_HIDE_PROGRESS_BAR, errorMessage, DEFAULT_DURATION_MSEC);
    }

    public TaskProgressUpdate(final int progress, final String currentStep) {
        this(progress, currentStep, DEFAULT_DURATION_MSEC, false);
    }

    public TaskProgressUpdate(final int progress,
                              final String currentStep,
                              final long expectedDurationMilliSec) {
        this(progress, currentStep, expectedDurationMilliSec, false);
    }


    public TaskProgressUpdate(final int progress,
                              final String currentStep,
                              final long expectedDurationMilliSec,
                              final boolean errorDetected) {
        this.progress = progress;
        this.currentStep = currentStep;
        this.expectedDurationMilliSec = expectedDurationMilliSec;
        this.errorDetected = errorDetected;
    }

    public boolean toHideProgressBar() {
        return this.progress == PROGRESS_VAL_TO_HIDE_PROGRESS_BAR;
    }

    public int getProgress() {
        return progress;
    }

    public String getCurrentStep() {
        return currentStep;
    }

    public long getExpectedDurationMilliSec() {
        return expectedDurationMilliSec;
    }

    public boolean isErrorDetected() {
        return errorDetected;
    }
}
