var fileUpload_controller = {

    model: null,

    uploadForm: null,
    textElem: null,
    inputElem: null,
    progressOuterElem: null,
    progressElem: null,
    errorElem: null,

    reloadInterval: null,
    isLoadingError: false,
    isProcessingFile: false,

    init: function () {
        var self = this;
        this.uploadForm.submit(function (e) {
            self.uploadFile(new FormData(this));
            e.preventDefault();
        });
        if (this.inputElem) {
            this.inputElem.change(function () {
                self.errorElem.empty();
            });
        }
    },

    uploadFile: function (formData) {
        var self = this;
        this.isLoadingError = false;
        this.isProcessingFile = false;
        this.errorElem.empty();
        this.textElem.html(labels["cmas.face.file.uploading"]);
        this.progressOuterElem.show();
        this.model.uploadFile(
            formData
            , function (e) {
                if (e.lengthComputable) {
                    var percentage = Math.ceil(e.loaded / e.total) * 100;
                    self.progressElem.width(percentage + '%');
                }
            }
            , function () {
                self.startFileProcessingProgress();
            }
            , function (json) {
                self.isLoadingError = true;
                var message = error_codes["validation.internal"];
                if (json && json.fieldErrors && json.fieldErrors.file) {
                    message = error_codes[json.fieldErrors.file];
                } else if (json && json.errors && json.errors.length && json.errors.length > 0) {
                    message = error_codes[json.errors[0]];
                }
                self.clearProgress(message);
                self.stopUpdatingFileProcessingProgress();
            }
        );
    },

    resetReloadInterval: function () {
        this.stopUpdatingFileProcessingProgress();
        if (!this.isLoadingError) {
            var self = this;
            this.reloadInterval = setTimeout(function run() {
                self.updateFileProcessingProgress();
            }, 3000);
        }
    },

    stopUpdatingFileProcessingProgress: function () {
        if (this.reloadInterval) {
            clearTimeout(this.reloadInterval);
            this.reloadInterval = null;
        }
    },

    startFileProcessingProgress: function () {
        if (this.isProcessingFile) {
            return;
        }
        this.textElem.html(labels["cmas.face.file.processing"]);
        this.progressElem.width("0%");
        this.isProcessingFile = true;
        this.updateFileProcessingProgress();
    },

    updateFileProcessingProgress: function () {
        var self = this;
        this.model.getFileProcessingProgress(
            function (json) {
                if (json) {
                    self.processTaskState(json);
                } else {
                    self.resetReloadInterval();
                }
            }, function () {
                self.resetReloadInterval();
            }
        );
    },

    processTaskState: function (json) {
        switch (json.taskStatus) {
            case "NOT_EXIST" :
                this.clearProgress("");
                break;
            case "NOT_STARTED" :
                this.textElem.html(labels["cmas.face.file.queued"]);
                this.progressElem.width("0%");
                this.errorElem.empty();
                break;
            case "IN_PROGRESS" :
                this.textElem.html(labels["cmas.face.file.processing"]);
                this.progressElem.width(json.progressPercent + '%');
                this.errorElem.empty();
                break;
            case "ERROR" :
                var message = error_codes["validation.internal"];
                if (json.parseError) {
                    message = error_codes["validation.xlsFileParseError"]
                        .replace("${rowNumber}", json.parseError.rowNumber)
                        .replace("${cause}", json.parseError.cause);
                } else if (json.error) {
                    message = error_codes[json.error];
                }
                this.clearProgress(message);
                this.isLoadingError = true;
                break;
            case "CANCELLED" :
                this.clearProgress(labels["cmas.face.file.cancelled"]);
                this.isLoadingError = true;
                break;
            case "DONE" :
                window.location.reload();
                break;
        }
        this.resetReloadInterval();
    },

    clearProgress: function (errorMessage) {
        this.textElem.empty();
        this.progressOuterElem.hide();
        this.errorElem.html(errorMessage);
    }
};
