package org.cmas.presentation.controller.mobile.dictionary;

import org.cmas.presentation.service.mobile.DictionaryDataService;
import org.cmas.remote.ErrorCodes;
import org.cmas.util.json.gson.GsonViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

@Controller
public class DictionaryController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @Autowired
    private DictionaryDataService dictionaryDataService;

    @RequestMapping("/getRoles.html")
    public View getRoles(@RequestParam("maxVersion") long maxVersion) {
        try {
            return gsonViewFactory.createGsonView(
                    dictionaryDataService.getRoles(maxVersion)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return gsonViewFactory.createGsonView(ErrorCodes.ERROR);
        }
    }

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
}
