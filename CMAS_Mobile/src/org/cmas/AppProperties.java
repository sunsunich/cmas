package org.cmas;

import java.util.Properties;

public class AppProperties {

    private final boolean isDebug;

    private final String gcmSenderId;

    private final String serverHOST;

    private final String loginURL;

    private final String registerNewUserURL;
    private final String addCodeURL;
    private final String addEmailURL;

    private final String registerDeviceURL;
    private final String unregisterDeviceURL;

    private final String getProfilesURL;
    private final String registerNewProfileURL;
    private final String changeProfileURL;
    private final String bindProfileURL;
    private final String getProfileOwnerCountURL;
    private final String unBindProfileURL;


    private final String getMedInstitutionsURL;
    private final String getMedInstitutionTypesURL;
    private final String getMeasureUnitsURL;
    private final String getUserTypesURL;

    private final String getMedServicesURL;
    private final String getParametersURL;

    private final String getDirectivesURL;
    private final String setDirectiveAsViewedURL;

    private final String getDirectiveURL;

    private final String getClinicsURL;
    private final String getSettlementsURL;
    private final String getMedInstitutionsByGeoURL;
    private final String getDetailedAddressURL;
    private final String getServicesByMedInstitutionURL;
    private final String getResourcesByServiceURL;
    private final String getResourcesScheduleURL;
    private final String createRecordURL;
    private final String createRecordWithContactURL;
    private final String rmisRegisterURL;
    private final String rmisLogoutURL;
    private final String getPastAppointmentsURL;
    private final String getFutureAppointmentsURL;
    private final String deleteAppointmentURL;

    private final String getDocsDictURL;
    private final String getDiverLogbookEntriesURL;
    private final String addNewLogbookEntryURL;
    private final String editLogbookEntryURL;
    private final String deleteLogbookEntryURL;

    private final String regionRegistryWSDL;

    public AppProperties(Properties properties) {
        gcmSenderId = properties.getProperty("egosanum.gcm.senderId");
        isDebug = Boolean.parseBoolean(properties.getProperty("egosanum.isDebug"));
        serverHOST = properties.getProperty("egosanum.serverHOST");
        loginURL = properties.getProperty("egosanum.loginURL");
        registerNewUserURL = properties.getProperty("egosanum.registerNewUserURL");
        addCodeURL = properties.getProperty("egosanum.addCodeURL");
        addEmailURL = properties.getProperty("egosanum.addEmailURL");

        registerDeviceURL = properties.getProperty("egosanum.registerDeviceURL");
        unregisterDeviceURL = properties.getProperty("egosanum.unregisterDeviceURL");

        getProfilesURL = properties.getProperty("egosanum.getProfilesURL");
        registerNewProfileURL = properties.getProperty("egosanum.registerNewProfileURL");
        changeProfileURL = properties.getProperty("egosanum.changeProfileURL");
        bindProfileURL = properties.getProperty("egosanum.bindProfileURL");
        getProfileOwnerCountURL = properties.getProperty("egosanum.getProfileOwnerCountURL");
        unBindProfileURL = properties.getProperty("egosanum.unBindProfileURL");

        getMedInstitutionsURL = properties.getProperty("egosanum.getMedInstitutionsURL");
        getMedInstitutionTypesURL = properties.getProperty("egosanum.getMedInstitutionTypesURL");
        getMeasureUnitsURL = properties.getProperty("egosanum.getMeasureUnitsURL");
        getUserTypesURL = properties.getProperty("egosanum.getUserTypesURL");


        getMedServicesURL = properties.getProperty("egosanum.getMedServicesURL");
        getParametersURL = properties.getProperty("egosanum.getParametersURL");
        getDirectivesURL = properties.getProperty("egosanum.getDirectivesURL");
        getDirectiveURL = properties.getProperty("egosanum.getDirectiveURL");
        //setDirectiveAsViewedURL
        setDirectiveAsViewedURL = properties.getProperty("egosanum.setDirectiveAsViewedURL");

        getClinicsURL=properties.getProperty("rmis.getClinicsURL");
        getSettlementsURL=properties.getProperty("rmis.getSettlementsURL");
        getMedInstitutionsByGeoURL=properties.getProperty("rmis.getMedInstitutionsByGeoURL");
        getDetailedAddressURL=properties.getProperty("rmis.getDetailedAddressURL");
        getServicesByMedInstitutionURL=properties.getProperty("rmis.getServicesByMedInstitutionURL");
        getResourcesByServiceURL=properties.getProperty("rmis.getResourcesByServiceURL");
        getResourcesScheduleURL=properties.getProperty("rmis.getResourcesScheduleURL");
        createRecordURL=properties.getProperty("rmis.createRecordURL");
        createRecordWithContactURL=properties.getProperty("rmis.createRecordWithContactURL");
        rmisRegisterURL =properties.getProperty("rmis.registerURL");
        rmisLogoutURL =properties.getProperty("rmis.logoutURL");
        getPastAppointmentsURL=properties.getProperty("rmis.getPastAppointmentsURL");
        getFutureAppointmentsURL=properties.getProperty("rmis.getFutureAppointmentsURL");
        deleteAppointmentURL=properties.getProperty("rmis.deleteAppointmentURL");

        getDocsDictURL=properties.getProperty("egosanum.getDocsDictURL");
        getDiverLogbookEntriesURL =properties.getProperty("egosanum.getUserDocsURL");
        addNewLogbookEntryURL =properties.getProperty("egosanum.addNewDocURL");
        editLogbookEntryURL =properties.getProperty("egosanum.editDocURL");
        deleteLogbookEntryURL =properties.getProperty("egosanum.deleteDocURL");

        regionRegistryWSDL=properties.getProperty("rmis.regionRegistryWSDL");
    }

    public String getSetDirectiveAsViewedURL() {
        return setDirectiveAsViewedURL;
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

    public String getGetProfilesURL() {
        return getProfilesURL;
    }

    public String getChangeProfileURL() {
        return changeProfileURL;
    }

    public String getBindProfileURL() {
        return bindProfileURL;
    }

    public String getGetProfileOwnerCountURL() {
        return getProfileOwnerCountURL;
    }

    public String getUnBindProfileURL() {
        return unBindProfileURL;
    }

    public String getGetDirectiveURL() {
        return getDirectiveURL;
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

    public String getGetMedInstitutionsURL() {
        return getMedInstitutionsURL;
    }

    public String getGetDirectivesURL() {
        return getDirectivesURL;
    }

    public String getRegisterNewUserURL() {
        return registerNewUserURL;
    }

    public String getGetClinicsURL() {
        return getClinicsURL;
    }

    public String getGetMedInstitutionTypesURL() {
        return getMedInstitutionTypesURL;
    }

    public String getGetMeasureUnitsURL() {
        return getMeasureUnitsURL;
    }

    public String getGetUserTypesURL() {
        return getUserTypesURL;
    }

    public String getGetMedServicesURL() {
        return getMedServicesURL;
    }

    public String getGetParametersURL() {
        return getParametersURL;
    }

    public String getGetDocsDictURL() {
        return getDocsDictURL;
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

    public String getRegionRegistryWSDL() {
        return regionRegistryWSDL;
    }

    public String getGetSettlementsURL() {
        return getSettlementsURL;
    }

    public String getGetMedInstitutionsByGeoURL() {
        return getMedInstitutionsByGeoURL;
    }

    public String getGetDetailedAddressURL() {
        return getDetailedAddressURL;
    }

    public String getGetServicesByMedInstitutionURL() {
        return getServicesByMedInstitutionURL;
    }

    public String getGetResourcesByServiceURL() {
        return getResourcesByServiceURL;
    }

    public String getGetResourcesScheduleURL() {
        return getResourcesScheduleURL;
    }

    public String getCreateRecordURL() {
        return createRecordURL;
    }

    public String getCreateRecordWithContactURL() {
        return createRecordWithContactURL;
    }

    public String getRmisRegisterURL() {
        return rmisRegisterURL;
    }

    public String getRmisLogoutURL() {
        return rmisLogoutURL;
    }

    public String getGetPastAppointmentsURL() {
        return getPastAppointmentsURL;
    }

    public String getGetFutureAppointmentsURL() {
        return getFutureAppointmentsURL;
    }

    public String getDeleteAppointmentURL() {
        return deleteAppointmentURL;
    }
}
