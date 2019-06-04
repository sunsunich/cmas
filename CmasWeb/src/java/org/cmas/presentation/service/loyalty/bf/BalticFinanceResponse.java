package org.cmas.presentation.service.loyalty.bf;

/**
 * Created on Jun 03, 2019
 *
 * @author Alexander Petukhov
 */
public class BalticFinanceResponse {

    public boolean success;

    public String message;

    public String rawJson;

    public String bfId;

    public BalticFinanceResponse() {
    }

    public BalticFinanceResponse(boolean success, String rawJson) {
        this.success = success;
        this.rawJson = rawJson;
    }
}
