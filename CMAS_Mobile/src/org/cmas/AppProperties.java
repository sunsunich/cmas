package org.cmas;

import java.util.Properties;

public class AppProperties {

    private final boolean isDebug;

    private final String gcmSenderId;

    private final String serverHOST;

    private final String loginURL;

    private final String checkDiverRegistrationURL;
    private final String addCodeURL;
    private final String addEmailURL;

    private final String registerDeviceURL;
    private final String unregisterDeviceURL;

    private final String registerNewProfileURL;
    private final String changeProfileURL;

    private final String getCountriesURL;

    private final String getDiveSpotsURL;
    private final String getDiverLogbookEntriesURL;
    private final String addNewLogbookEntryURL;
    private final String editLogbookEntryURL;
    private final String deleteLogbookEntryURL;

    public AppProperties(Properties properties) {
        gcmSenderId = properties.getProperty("cmas.gcm.senderId");
        isDebug = Boolean.parseBoolean(properties.getProperty("cmas.isDebug"));
        serverHOST = properties.getProperty("cmas.serverHOST");
        loginURL = properties.getProperty("cmas.loginURL");
        checkDiverRegistrationURL = properties.getProperty("cmas.checkDiverRegistrationURL");
        addCodeURL = properties.getProperty("cmas.addCodeURL");
        addEmailURL = properties.getProperty("cmas.addEmailURL");

        registerDeviceURL = properties.getProperty("cmas.registerDeviceURL");
        unregisterDeviceURL = properties.getProperty("cmas.unregisterDeviceURL");

        registerNewProfileURL = properties.getProperty("cmas.registerNewProfileURL");
        changeProfileURL = properties.getProperty("cmas.changeProfileURL");

        getCountriesURL =properties.getProperty("cmas.getCountriesURL");


        getDiveSpotsURL =properties.getProperty("cmas.getDiveSpotsURL");
        getDiverLogbookEntriesURL =properties.getProperty("getDiverLogbookEntriesURL");
        addNewLogbookEntryURL =properties.getProperty("cmas.addNewLogbookEntryURL");
        editLogbookEntryURL =properties.getProperty("cmas.editLogbookEntryURL");
        deleteLogbookEntryURL =properties.getProperty("cmas.deleteLogbookEntryURL");
    }

    public String getAddCodeURL() {
        return addCodeURL;
    }

    public String getAddEmailURL() {
        return addEmailURL;
    }

    public String getRegisterDeviceURL() {
        return registerDeviceURL;
    }

    public String getUnregisterDeviceURL() {
        return unregisterDeviceURL;
    }

    public String getChangeProfileURL() {
        return changeProfileURL;
    }

    public String getRegisterNewProfileURL() {
        return registerNewProfileURL;
    }

    public String getGcmSenderId() {
        return gcmSenderId;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public String getServerHOST() {
        return serverHOST;
    }

    public String getLoginURL() {
        return loginURL;
    }

    public String getCheckDiverRegistrationURL() {
        return checkDiverRegistrationURL;
    }

    public String getGetDiveSpotsURL() {
        return getDiveSpotsURL;
    }

    public String getGetDiverLogbookEntriesURL() {
        return getDiverLogbookEntriesURL;
    }

    public String getAddNewLogbookEntryURL() {
        return addNewLogbookEntryURL;
    }

    public String getEditLogbookEntryURL() {
        return editLogbookEntryURL;
    }

    public String getDeleteLogbookEntryURL() {
        return deleteLogbookEntryURL;
    }

    public String getGetCountriesURL() {
        return getCountriesURL;
    }
}
