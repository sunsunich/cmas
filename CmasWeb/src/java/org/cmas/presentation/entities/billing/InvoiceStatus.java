package org.cmas.presentation.entities.billing;


public enum InvoiceStatus {

      PAID("Оплачен")
    , NOT_PAID("Не оплачен")
    , ERROR("ошибка")
    ;

    private String localName;

    InvoiceStatus(String localName){
        this.localName = localName;
    }

    public String getLocalName() {
        return localName;
    }
}
