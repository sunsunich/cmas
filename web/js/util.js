//EJS.config({cache: false});

function is_positive_int(value) {
    return (parseFloat(value) == parseInt(value)) && !isNaN(value) && value >0;
}

function round_to_Money(value) {
    return Math.round(value*100)/100;
}

function get_url_param_value(name){
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
      return decodeURIComponent(name[1]);
}

function isStringTrimmedEmpty(str) {
    return !str || (str.length === 0 || !str.trim());
}

function simpleClone(obj){
    if (null == obj || "object" != typeof obj) return obj;
    var copy = {};
    for (var attr in obj) {
        if (obj.hasOwnProperty(attr)) copy[attr] = obj[attr];
    }
    return copy;
}
