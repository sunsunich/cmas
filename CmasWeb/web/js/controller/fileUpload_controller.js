var fileUpload_controller = {

    model: null,

    uploadForm: null,
    textElem: null,
    progressOuterElem: null,
    progressElem: null,
    errorElem: null,

    reloadInterval: null,
    isLoadingError: false,

    init: function () {
        var self = this;
        this.uploadForm.submit(function (e) {
            self.uploadFile(new FormData(this));
            e.preventDefault();
        });
    },

    uploadFile: function (formData) {
        var self = this;
        this.isLoadingError = false;
        this.errorElem.empty();
        this.textElem.html(labels["cmas.face.file.uploading"]);
        this.progressOuterElem.show();
        this.model.uploadFile(
            formData
            , function (e) {
                if (e.lengthComputable) {
                    var percentage = Math.ceil(e.loaded / e.total) * 100;
                    self.progressElem.width(percentage + '%');
                    if (percentage >= 100) {
                        self.textElem.html(labels["cmas.face.file.processing"]);
                        self.progressElem.width("0%");
                        self.updateFileProcessingProgress();
                    }
                }
            }
            , function () {
                window.location.reload();
            }
            , function (json) {
                self.isLoadingError = true;
                var message = error_codes["validation.internal"];
                if (json && json.rowNumber) {
                    message = error_codes["validation.xlsFileParseError"]
                        .replace("${rowNumber}", json.rowNumber)
                        .replace("${cause}", json.cause);
                }
                else if (json && json.fieldErrors && json.fieldErrors.file) {
                    message = error_codes[json.fieldErrors.file];
                }
                else if (json && json.errors && json.errors.length && json.errors.length > 0) {
                    message = error_codes[json.errors[0]];
                }
                self.textElem.empty();
                self.progressOuterElem.hide();
                self.errorElem.html(message);
                self.stopUpdatingFileProcessingProgress();
            }
        );
    },

    resetReloadInterval: function () {
        if (this.isLoadingError) {
            this.stopUpdatingFileProcessingProgress();
        } else {
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

    updateFileProcessingProgress: function () {
        var self = this;
        this.model.getFileProcessingProgress(
            function (json) {
                self.progressElem.width(json.progressPercent + '%');
                self.resetReloadInterval();
            }, function () {
                self.resetReloadInterval();
            }
        );
    }
};
