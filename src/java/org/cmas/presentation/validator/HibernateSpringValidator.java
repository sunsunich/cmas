package org.cmas.presentation.validator;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.jetbrains.annotations.Nullable;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class HibernateSpringValidator implements Validator {

    private final Map<Class, ClassValidator> validators = new HashMap<Class, ClassValidator>();
    private final Set<Class> nonValidable = new HashSet<Class>();

    @SuppressWarnings({"SynchronizedMethod"})
    @Nullable
    private synchronized <T>ClassValidator<T> findValidator(Class<T> clazz) {
        if (nonValidable.contains(clazz)) {
            return null;
        }
        ClassValidator<T> result = (ClassValidator<T>)validators.get(clazz);
        if (result == null) {
            //noinspection unchecked
            result = new ClassValidator<T>(clazz);//, ResourceBundle.getBundle("messages", Locale.getDefault()));
            if (result.hasValidationRules()) {
                validators.put(clazz, result);
            } else {
                nonValidable.add(clazz);
            }
        }
        return result;
    }

    @Override
    public boolean supports(Class clazz) {
        return findValidator(clazz) != null;

    }
    
    @Override
    public void validate(Object target, Errors errors) {
        Class clazz = target.getClass();
        ClassValidator validator = findValidator(clazz); //Находим по классу нужный хибернейтовый валидатор
        if (validator != null) {
            InvalidValue[] values = validator.getInvalidValues(target);
            /*
              Перегоняем все собранные хибернейтовые ошибки в спринговые ошибки
            */
            for (InvalidValue value : values) {
                if (value.getPropertyName() == null) {
                    errors.reject(value.getMessage());
                } else {
                    //делает ровно то, что мы раньше делали руками во всех валидаторах,
                    //поэтому value.getMessage() должен быть спринговым кодом ошибки
                    errors.rejectValue(value.getPropertyPath(), value.getMessage());
                }
            }
        }
        /*
          Если класс сам себя валидирует, запускаем и эту валидацию
        */
        if(target instanceof Validatable){
            ((Validatable)target).validate(errors);    
        }
    }
}

