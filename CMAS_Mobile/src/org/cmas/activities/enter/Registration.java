package org.cmas.activities.enter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import org.cmas.DatePickerFragment;
import org.cmas.Globals;
import org.cmas.dao.dictionary.CountryDao;
import org.cmas.entities.Country;
import org.cmas.mobile.R;
import org.cmas.service.RegistrationService;
import org.cmas.service.dictionary.DictionaryDataService;
import org.cmas.util.DialogUtils;
import org.cmas.util.ProgressTask;
import org.cmas.util.StringUtil;
import org.cmas.util.android.NothingSelectedSpinnerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Registration extends BaseEnterView {

    private final RegistrationService registrationService = beanContainer.getRegistrationService();
    private final DictionaryDataService dictionaryDataService = beanContainer.getDictionaryDataService();
    private final CountryDao countryDao = beanContainer.getCountryDao();

    public Registration() {
        super(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        setupHeader(
                getString(R.string.login_register)
                , navigationService.getLoggedOffActivity()
        );

        Spinner countrySpinner = (Spinner) findViewById(R.id.country_spinner);
        List<Country> countries = dictionaryDataService.getAllDictionaryEntities(this, countryDao);
        List<String> countryNames = new ArrayList<>(countries.size());
        for (Country country : countries) {
            countryNames.add(country.getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_row_country,
                countryNames
        );
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(new NothingSelectedSpinnerAdapter(
                dataAdapter,
                R.layout.spinner_row_nothing_selected,
                this,
                getString(R.string.country)));

        setDate(
                (ImageButton) findViewById(R.id.set_date_of_birthday_btn),
                (TextView) findViewById(R.id.date_of_birthday_edit),
                new Date()
        );

        Button regButton = (Button) findViewById(R.id.bnt_register);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickForward();
            }
        });
    }

    protected void setDate(ImageButton selectDateButton, final TextView edit, final Date date) {
        final FragmentActivity context = this;
        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePickerFragment
                        = new DatePickerFragment(date,
                                                 new DatePickerDialog.OnDateSetListener() {
                                                     @Override
                                                     public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                         Calendar
                                                                 date
                                                                 = Calendar.getInstance();
                                                         date.set(year,
                                                                  monthOfYear,
                                                                  dayOfMonth);
                                                         edit.setText(Globals.getDTF()
                                                                             .format(date.getTime()));
                                                     }
                                                 });
                datePickerFragment.show(context.getSupportFragmentManager(), "datePicker");
            }
        });
    }

    public void onClickForward() {

        final FragmentActivity activity = this;
        @SuppressWarnings("OverlyStrongTypeCast")
        final String countryName = ((Spinner) activity.findViewById(R.id.country_spinner)).getSelectedItem().toString();
        final String firstName = ((TextView) activity.findViewById(R.id.first_name_input)).getText().toString();
        final String lastName = ((TextView) activity.findViewById(R.id.last_name_input)).getText().toString();
        final String dob = ((TextView) activity.findViewById(R.id.date_of_birthday_edit)).getText().toString();


        if (StringUtil.isTrimmedEmpty(countryName)) {
            reportError(getString(R.string.country_empty_error));
            return;
        }
        if (StringUtil.isTrimmedEmpty(firstName)) {
            reportError(getString(R.string.firstname_empty_error));
            return;
        }
        if (StringUtil.isTrimmedEmpty(lastName)) {
            reportError(getString(R.string.lastname_empty_error));
            return;
        }
        if (StringUtil.isTrimmedEmpty(dob)) {
            reportError(getString(R.string.dob_empty_error));
            return;
        }

        DialogUtils.showLoaderDialog(activity.getSupportFragmentManager(), new ProgressTask<String>() {

            @Override
            public String doTask(ProgressTask.OnPublishProgressListener listener) {
                listener.onPublishProgress(getString(R.string.registration_attempt));
                return registrationService.registerUser(
                        activity, countryName, firstName, lastName, dob
                );
            }

            @Override
            public void doAfterTask(String result) {
                if(StringUtil.isTrimmedEmpty(result)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage(
                            Html.fromHtml(
                                    "Confirmation received from National Federation. <br/>"
                                    + "Please wait while we send you your password. <br/>"
                                    + "Password will be sent to the email you used when you registered at National Federation. <br/>"
                                    + "Use this email as the login to the mobile application. <br/>"
                            )
                    );
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    });
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            Intent intent = new Intent(
                                    activity
                                    , navigationService.getLoggedOffActivity()
                            );
                            activity.startActivity(intent);
                            activity.finish();
                        }
                    });
                    builder.setCancelable(false);
                    AlertDialog myAlertDialog = builder.create();
                    myAlertDialog.show();
                }
                else{
                    reportError(result);
                }
            }

            @Override
            public void handleError(String error) {
                reportError(error);
            }

            @Override
            public String getName() {
                return "registerUser";
            }
        }, "registerUser");
    }
}
