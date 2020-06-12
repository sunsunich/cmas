var multiple_fileUpload_controller = {

    addImageElem: null,
    photoListContainer: null,
    errorElem: null,
    addImageCallback: null,

    maxUpLoadSizeBytes: 10 * 1024 * 1024,
    errorMessageCode: "validation.multipleImagesSize",
    maxFilesToAttach: 2,

    files: {},
    base64Strings : {},
    availableIds: [],

    init: function () {
        var i;
        for (i = 0; i < this.maxFilesToAttach; i++) {
            var fileId = i + 1;
            this.availableIds.push(fileId);
            this.setupInput(fileId);
        }
        var self = this;
        this.addImageElem.click(function () {
            self.selectPhotoFromDrive();
        });

    },

    setupInput: function (fileId) {
        this.photoListContainer.append(
            new EJS({url: '/js/templates/imageFileInput.ejs?v=' + webVersion})
                .render({
                    "fileId": fileId,
                    "webVersion": webVersion
                })
        );
        var self = this;
        $('#fileInput_' + fileId).change(function () {
            self.loadFileToPreview(this);
        });
    },

    selectPhotoFromDrive: function () {
        var nextFileId = this.availableIds[0];
        $('#fileInput_' + nextFileId).trigger('click');
    },

    loadFileToPreview: function (input) {
        var fileId = this.availableIds.shift();
        if (this.availableIds.length == 0) {
            this.addImageElem.hide();
        }
        var file = input.files[0];
        this.files[fileId] = file;
        try {
            if (!this.validateFile(input)) {
                this.removeFile(fileId);
                return;
            }
            var self = this;
            var reader = new FileReader();
            reader.onload = function (e) {
                let result = e.target.result;
                self.base64Strings[fileId] = result;
                self.photoListContainer.append(
                    new EJS({url: '/js/templates/imageFilePreview.ejs?v=' + webVersion})
                        .render({
                            "fileId": fileId,
                            "src": result,
                            "webVersion": webVersion
                        })
                );
                $('#deletePhoto_' + fileId).click(function () {
                    self.removeFile(fileId);
                });

                self.execImageChangeCallback();
            };
            reader.readAsDataURL(file);
        } catch (err) {
            this.removeFile(fileId);
            this.errorElem.html(error_codes["validation.imageFormat"]);
            console.error("Exception thrown", err.stack);
        }
    },

    validateFile: function (input) {
        var errorElem = this.errorElem;
        errorElem.html('');
        if (!(input.files && input.files[0])) {
            errorElem.html(error_codes["validation.emptyField"]);
            return false;
        }
        var file = input.files[0];
        if (file.name.length < 1) {
            errorElem.html(error_codes["validation.emptyField"]);
            return false;
        }
        if (file.type != 'image/png' && file.type != 'image/jpg' && file.type != 'image/gif' && file.type != 'image/jpeg') {
            errorElem.html(error_codes["validation.imageFormat"]);
            return false;
        }
        var totalSize = 0;
        for (var key in this.files) {
            if (this.files.hasOwnProperty(key)) {
                totalSize += this.files[key].size;
                if (totalSize > this.maxUpLoadSizeBytes) {
                    errorElem.html(error_codes[this.errorMessageCode]);
                    return false;
                }
            }
        }
        return true;
    },

    removeFile: function (fileId) {
        var photoContainerElem = $('#photoContainer_' + fileId);
        if (photoContainerElem && photoContainerElem[0]) {
            photoContainerElem.remove();
        }
        $('#fileInput_' + fileId).remove();
        this.setupInput(fileId);
        delete this.files[fileId];
        delete this.base64Strings[fileId];
        this.availableIds.push(fileId);
        this.availableIds.sort();

        this.addImageElem.show();
        this.execImageChangeCallback();
    },

    execImageChangeCallback: function () {
        if (this.addImageCallback) {
            var self = this;
            setTimeout(function () {
                self.addImageCallback();
            }, 500);
        }
    }
};
