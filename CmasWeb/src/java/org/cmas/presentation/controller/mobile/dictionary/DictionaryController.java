package org.cmas.presentation.controller.mobile.dictionary;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import org.cmas.entities.DeviceType;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.dao.user.DeviceDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.service.mobile.DictionaryDataService;
import org.cmas.presentation.service.mobile.PushServerSettings;
import org.cmas.remote.ErrorCodes;
import org.cmas.util.StringUtil;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.json.gson.GsonViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class DictionaryController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private PushServerSettings pushServerSettings;

    @Autowired
    private DictionaryDataService dictionaryDataService;

    @RequestMapping("/getCountries.html")
    public View getCountries(@RequestParam("maxVersion") long maxVersion) {
        try {
            return gsonViewFactory.createGsonView(
                    dictionaryDataService.getCountries(maxVersion)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return gsonViewFactory.createGsonView(ErrorCodes.ERROR);
        }
    }

    @RequestMapping("/getFederations.html")
    public View getNationalFederations(@RequestParam("maxVersion") long maxVersion) {
        try {
            return gsonViewFactory.createGsonView(
                    dictionaryDataService.getNationalFederations(maxVersion)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return gsonViewFactory.createGsonView(ErrorCodes.ERROR);
        }
    }

    @RequestMapping("/send-message.html")
    public View sendMessage(@RequestParam("email") String email) {
        Diver diver = diverDao.getByEmail(StringUtil.lowerCaseEmail(email));
        if (diver == null) {
            throw new BadRequestException();
        }

        List<String> androidDeviceRegIds = deviceDao.getPushRegIdByUserAndDeviceType(diver, DeviceType.ANDROID);
        if (!androidDeviceRegIds.isEmpty()) {
            Sender sender = new FCMSender(pushServerSettings.getGcmKey());
            Message message = new Message.Builder()
                    .addData("diverId", String.valueOf(diver.getId()))
                    .build();
            try {
                MulticastResult result = sender.send(message, androidDeviceRegIds, 5);
                log.info(result.toString());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                return gsonViewFactory.createGsonView(e.getMessage());
            }
        }


    /*    List<String> macDeviceRegIds = deviceDao.getPushRegIdByUserAndDeviceType(user, DeviceType.IOS);
        if (!macDeviceRegIds.isEmpty()) {
            try {
                int badge = 0;
                for (BackendProfile profile : backendProfiles) {
                    int newDirectiveCnt = directiveDao.getNewDirectiveCntByProfile(profile.getId());
                    badge += newDirectiveCnt;
                }

                Payload payload = PushNotificationPayload.fromJSON(
                        '{' +
                        "    \"aps\" : {" +
                        "    \"alert\" :" +
                        "    { \"loc-key\" : \"NEW_DATA_AVAILABLE\"," +
                        "       \"loc-args\" : \"\"}," +
                        "       \"sound\" : \"default\"," +
                        "       \"badge\":" + badge +
                        "    }" +
                        '}'
                );
                Push.payload(
                        payload,
                        pushServerSettings.getIosKeystorePath(),
                        pushServerSettings.getIosKeystorePssword(),
                        false,
                        macDeviceRegIds
                );
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return gsonViewFactory.createGsonView(e.getMessage());
            }
        }*/
        return gsonViewFactory.createGsonView("ok");
    }

/*    @RequestMapping("/send-push-to-ios-device.html")
    public View sendPushToDevice(
            @RequestParam("pushServiceRegId") String pushServiceRegId,
            @RequestParam("jsonPush") String jsonPush
    ) {
        try {
            Payload payload = PushNotificationPayload.fromJSON(
                    jsonPush
            );
            Push.payload(
                    payload,
                    pushServerSettings.getIosKeystorePath(),
                    pushServerSettings.getIosKeystorePssword(),
                    false,
                    pushServiceRegId
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return gsonViewFactory.createGsonView(e.getMessage());
        }

        return gsonViewFactory.createGsonView("ok");
    }*/
}
