//EJS.config({cache: false});

function compare_big_int(x, y) {
    var lengthDif = x.length - y.length;
    if (lengthDif > 0) {
        for (var i = 0; i < lengthDif; i++) {
            y = "0" + y;
        }
    }
    if (lengthDif < 0) {
        for (i = 0; i < -lengthDif; i++) {
            x = "0" + x;
        }
    }
    return x > y;
}

function is_positive_int(value) {
    return (parseFloat(value) == parseInt(value)) && !isNaN(value) && value > 0;
}

function is_positive_float(value) {
    value = value.replace(',', '.');
    if (isNaN(value)) {
        return false;
    }
    else {
        return parseFloat(value) > 0;
    }
}

function is_non_negative_float(value) {
    value = value.replace(',', '.');
    if (isNaN(value)) {
        return false;
    }
    else {
        return parseFloat(value) >= 0;
    }
}

function is_float(value) {
    value = value.replace(',', '.');
    return isNaN(value);
}

function round_to_Money(value) {
    return Math.round(value * 100) / 100;
}

function get_url_param_value(name) {
    if (name = (new RegExp('[?&]' + encodeURIComponent(name) + '=([^&]*)')).exec(location.search))
        return decodeURIComponent(name[1]);
}

function isStringTrimmedEmpty(str) {
    return !str || (str.length === 0 || !str.trim());
}

function simpleClone(obj) {
    if (null == obj || "object" != typeof obj) return obj;
    var copy = {};
    for (var attr in obj) {
        if (obj.hasOwnProperty(attr)) copy[attr] = obj[attr];
    }
    return copy;
}

function arrayToJsonStr(array) {
    if (null == array || "object" != typeof array) return "[]";
    //var str = "[";
    //for (var i = 0; i < array.length; i++) {
    //    str += array[i];
    //    if (str != array.length - 1) {
    //        str += ',';
    //    }
    //}
    //str += "]";
    //return str;
    return JSON.stringify(array);
}

function removeFromArray(array, elem) {
    if (null == array || "object" != typeof array) return array;
    if (array.indexOf(elem) == -1) {
        return array;
    }
    var newArray = [];
    for (var i = 0; i < array.length; i++) {
        if (array[i] != elem) {
            newArray.push(array[i]);
        }
    }
    return newArray;
}

function mapSize(obj) {
    var size = 0, key;
    for (key in obj) {
        if (obj.hasOwnProperty(key)) size++;
    }
    return size;
}

function removeFromArrayByIndex(array, index) {
    if (null == array || "object" != typeof array) return array;
    if (index < 0 && index >= array.length) {
        return array;
    }
    var newArray = [];
    for (var i = 0; i < array.length; i++) {
        if (i != index) {
            newArray.push(array[i]);
        }
    }
    return newArray;
}

function replaceBackgroundFullWindow(containerId, imgId, deltaHeight) {
    replaceBackground(containerId, imgId);
    $("#" + containerId).css('height', $(window).height() - deltaHeight);
}

function replaceBackground(containerId, imgId) {
    var imgElem = $("#" + imgId).get(0); //retrieve DOM element from background Img
    //test currentSrc support, if not supported, use the old src
    var src = imgElem.currentSrc ? imgElem.currentSrc : imgElem.src;
    $("#" + containerId).css("backgroundImage", 'url("' + src + '")');
}

function adjustTextFont(containerId, compressor, minFont, maxFont) {
    adjustCssProperty('font-size', "#" + containerId, $("#" + containerId).width(), compressor, minFont, maxFont);
}

function adjustTextFontHorizontalViewPort(containerId, compressor, minFont, maxFont) {
    adjustCssProperty('font-size', "#" + containerId, $(window).width(), compressor, minFont, maxFont);
}

function adjustCssProperty(propertyName, jquerySelector, valueToAdjustTo, compressor, minValue, maxValue) {
    $(jquerySelector).css(propertyName, Math.max(
        Math.min(valueToAdjustTo / (10 * compressor), parseFloat(maxValue)), parseFloat(minValue))
    );
}

function reverseAdjustCssProperty(propertyName, jquerySelector, valueToAdjustTo, compressor, minValue, maxValue) {
    $(jquerySelector).css(propertyName, Math.max(
        Math.min((10 * compressor) / valueToAdjustTo, parseFloat(maxValue)), parseFloat(minValue))
    );
}
