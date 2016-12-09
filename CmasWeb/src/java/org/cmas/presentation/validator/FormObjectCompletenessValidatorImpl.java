package org.cmas.presentation.validator;

import java.lang.reflect.Method;

public class FormObjectCompletenessValidatorImpl implements FormObjectCompletenessValidator{

	@Override
	public boolean isComplete(Object formObject) {
		Class clazz = formObject.getClass();
		for (Method method : clazz.getMethods()) {
			if (method.getName().startsWith("get")){
				try{
					Object result = method.invoke(formObject);
					if(result == null){
						return false;
					}
					else if(result instanceof String){
						if(((String)result).isEmpty()){
							return false;
						}
					}
				}
				catch (Exception ignored){
				}
			}
		}
		return true;
	}
}
