var loader_controller = {

    /**
     * Инициализация
     */
    init: function () {
    },

    /**
     * Запускает ожидание, показывает иконку ожидания
     * @public
     */
    startwait: function () {
        $("#loading").show();
    },

    /**
     * Останавливает ожидание, скрывает иконку
     * @public
     */
    stopwait: function () {
        $("#loading").hide();
    }

};

$(document).ready(function() {
    loader_controller.init();
});


