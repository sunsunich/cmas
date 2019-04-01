package org.cmas.presentation.controller.fedadmin;

import org.cmas.Globals;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.fin.PaidFeature;
import org.cmas.presentation.dao.billing.DiverPaymentListDao;
import org.cmas.presentation.dao.billing.PaidFeatureDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.billing.DiverPaymentList;
import org.cmas.presentation.entities.billing.Invoice;
import org.cmas.presentation.entities.billing.InvoiceType;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.billing.BillingService;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.json.gson.GsonViewFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created on Mar 31, 2019
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class FederationAdminBillingController {

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private BillingService billingService;

    @Autowired
    private DiverPaymentListDao diverPaymentListDao;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private PaidFeatureDao paidFeatureDao;

    @RequestMapping("/fed/getDiverPaymentList.html")
    public View getDiverPaymentList() {
        BackendUser<Diver> currentFedAdmin = authenticationService.getCurrentDiver();
        if (currentFedAdmin == null) {
            throw new BadRequestException();
        }
        Diver currentFedAdminUser = currentFedAdmin.getUser();
        DiverPaymentList diverPaymentList = diverPaymentListDao.getByCreator(currentFedAdminUser);
        if (diverPaymentList == null) {
            return gsonViewFactory.createSuccessGsonView();
        } else {
            return gsonViewFactory.createGsonView(diverPaymentList);
        }
    }

    @RequestMapping("/fed/addDiverToPaymentList.html")
    public View addDiverToPaymentList(
            @RequestParam("diverId") Long diverId
    ) {
        BackendUser<Diver> currentFedAdmin = authenticationService.getCurrentDiver();
        if (currentFedAdmin == null) {
            throw new BadRequestException();
        }
        Diver diver = diverDao.getModel(diverId);
        if (diver == null) {
            throw new BadRequestException();
        }
        Diver currentFedAdminUser = currentFedAdmin.getUser();
        if (!diver.getFederation().equals(currentFedAdminUser.getFederation())) {
            throw new BadRequestException();
        }

        DiverPaymentList diverPaymentList = diverPaymentListDao.getByCreator(currentFedAdminUser);
        if (diverPaymentList == null) {
            diverPaymentList = new DiverPaymentList();
            diverPaymentList.setListCreator(currentFedAdminUser);
            diverPaymentList.setDivers(new HashSet<Diver>());
            diverPaymentListDao.save(diverPaymentList);
        }
        Set<Diver> divers = diverPaymentList.getDivers();
        if (divers == null) {
            divers = new HashSet<>();
        }
        divers.add(diver);
        diverPaymentListDao.updateModel(diverPaymentList);

        return gsonViewFactory.createSuccessGsonView();
    }

    @RequestMapping("/fed/removeDiverFromPaymentList.html")
    public View removeDiverFromPaymentList(
            @RequestParam("diverId") Long diverId
    ) {
        BackendUser<Diver> currentFedAdmin = authenticationService.getCurrentDiver();
        if (currentFedAdmin == null) {
            throw new BadRequestException();
        }
        Diver diver = diverDao.getModel(diverId);
        if (diver == null) {
            throw new BadRequestException();
        }
        Diver currentFedAdminUser = currentFedAdmin.getUser();
        if (!diver.getFederation().equals(currentFedAdminUser.getFederation())) {
            throw new BadRequestException();
        }

        DiverPaymentList diverPaymentList = diverPaymentListDao.getByCreator(currentFedAdminUser);
        if (diverPaymentList != null && diverPaymentList.getDivers() != null) {
            diverPaymentList.getDivers().remove(diver);
            diverPaymentListDao.updateModel(diverPaymentList);
        }

        return gsonViewFactory.createSuccessGsonView();
    }

    @RequestMapping("/fed/payForDivers.html")
    public ModelAndView payForDivers() {
        BackendUser<Diver> currentFedAdmin = authenticationService.getCurrentDiver();
        if (currentFedAdmin == null) {
            throw new BadRequestException();
        }

        Diver currentFedAdminUser = currentFedAdmin.getUser();
        DiverPaymentList diverPaymentList = diverPaymentListDao.getByCreator(currentFedAdminUser);
        if (diverPaymentList != null) {
            Set<Diver> divers = diverPaymentList.getDivers();
            if (divers != null && !divers.isEmpty()) {
                List<PaidFeature> features = paidFeatureDao.getByIds(
                        Collections.singletonList(Globals.CMAS_LICENCE_PAID_FEATURE_DB_ID)
                );
                Invoice invoice = billingService.createInvoice(features,
                                                               currentFedAdminUser,
                                                               InvoiceType.SYSTEMPAY,
                                                               divers
                );

                diverPaymentList.setDivers(null);
                diverPaymentListDao.updateModel(diverPaymentList);
                diverPaymentListDao.deleteModel(diverPaymentList);
                ModelMap mm = new ModelMap();
                mm.addAttribute("invoiceId", invoice.getExternalInvoiceNumber());
                return new ModelAndView("redirect:/fed/billing/systempay/accept.html", mm);
            }
            diverPaymentListDao.deleteModel(diverPaymentList);
        }
        return new ModelAndView("redirect:/fed/index.html");
    }
}
