package org.cmas.presentation.entities.billing;

public enum OperationType {
    IN, PURCHASE, RETURN
    ;

    public String getName(){
        return name();
    }
}
