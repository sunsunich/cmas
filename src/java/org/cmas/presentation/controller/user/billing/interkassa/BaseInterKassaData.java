package org.cmas.presentation.controller.user.billing.interkassa;

public abstract class BaseInterKassaData {
 
    protected String ik_shop_id;
    protected String ik_payment_amount;
    protected String ik_payment_id;
    protected String ik_payment_desc;
    protected String ik_paysystem_alias;

    protected String ik_baggage_fields;

    protected BaseInterKassaData() {
    }

    public String getIk_paysystem_alias() {
        return ik_paysystem_alias;
    }

    public void setIk_paysystem_alias(String ik_paysystem_alias) {
        this.ik_paysystem_alias = ik_paysystem_alias;
    }

    public String getIk_baggage_fields() {
        return ik_baggage_fields;
    }

    public void setIk_baggage_fields(String ik_baggage_fields) {
        this.ik_baggage_fields = ik_baggage_fields;
    }

    public String getIk_shop_id() {
        return ik_shop_id;
    }

    public void setIk_shop_id(String ik_shop_id) {
        this.ik_shop_id = ik_shop_id;
    }

    public String getIk_payment_amount() {
        return ik_payment_amount;
    }

    public void setIk_payment_amount(String ik_payment_amount) {
        this.ik_payment_amount = ik_payment_amount;
    }

    public String getIk_payment_id() {
        return ik_payment_id;
    }

    public void setIk_payment_id(String ik_payment_id) {
        this.ik_payment_id = ik_payment_id;
    }

    public String getIk_payment_desc() {
        return ik_payment_desc;
    }

    public void setIk_payment_desc(String ik_payment_desc) {
        this.ik_payment_desc = ik_payment_desc;
    }

    @Override
    public String toString() {
        return "BaseInterKassaData{" +
                "ik_shop_id='" + ik_shop_id + '\'' +
                ", ik_payment_amount='" + ik_payment_amount + '\'' +
                ", ik_payment_id='" + ik_payment_id + '\'' +
                ", ik_payment_desc='" + ik_payment_desc + '\'' +
                ", ik_paysystem_alias='" + ik_paysystem_alias + '\'' +
                ", ik_baggage_fields='" + ik_baggage_fields + '\'' +
                '}';
    }
}