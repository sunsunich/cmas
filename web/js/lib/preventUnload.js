function preventBack(){window.history.forward();}

setTimeout(function () {
    preventBack();
}, 0);

window.onunload=function(){null};