package org.cmas.presentation.entities.user;

/**
 * Типы Событий:
 *  Регистрация в систему
Логин в систему
Изменения ключевых данных:
    email, password

Эти события получаем из финансового лога:
ввод денег


 
Выход из системы - кнопка Logout   
 */
public enum UserEventType {

      REGISTER, LOGIN, EMAIL_CHANGE, PASSWORD_CHANGE

    , MONEY_IN, PURCHASE, RETURN


    , LOGOUT
}
