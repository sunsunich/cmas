package org.cmas.android.ui.signin;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import org.cmas.Globals;
import org.cmas.android.MainActivity;
import org.cmas.android.storage.entities.Country;
import org.cmas.android.storage.entities.sport.NationalFederation;
import org.cmas.android.ui.EnumToLabelUtil;
import org.cmas.android.ui.file.FileAdditionFragment;
import org.cmas.android.ui.verify.DiverVerificationFragment;
import org.cmas.android.validation.CheckedBoxValidationView;
import org.cmas.android.validation.TextValidationView;
import org.cmas.android.validation.ValidationHelper;
import org.cmas.android.validation.ValidationItem;
import org.cmas.ecards.R;
import org.cmas.ecards.databinding.RegistrationFragmentBinding;
import org.cmas.entities.diver.AreaOfInterest;
import org.cmas.util.LabelValue;
import org.cmas.util.StringUtil;
import org.cmas.util.android.TaskViewModel;
import org.cmas.util.android.ui.DatePickerFragment;
import org.cmas.util.android.ui.ViewUtils;

import javax.annotation.Nullable;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RegistrationFragment extends Fragment {

    private RegistrationFragmentBinding binding;
    private ArrayAdapter<LabelValue<Country>> countryAdapter;

    private ArrayAdapter<LabelValue<NationalFederation>> nationalFederationAdapter;

    private RegistrationLoadInitDataViewModel registrationLoadInitDataViewModel;
    private ValidationHelper validationHelper;

    @Nullable
    private RegistrationFormObject registrationFormObject;

    public static RegistrationFragment newInstance(RegistrationFormObject registrationFormObject) {
        RegistrationFragment registrationFragment = new RegistrationFragment();
        registrationFragment.registrationFormObject = registrationFormObject;
        return registrationFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        registrationLoadInitDataViewModel = TaskViewModel.safelyInitTask(this,
                                                                         RegistrationLoadInitDataViewModel.class);
        validationHelper = new ValidationHelper(registrationLoadInitDataViewModel.startSingleItemValidation);
        binding = DataBindingUtil.inflate(inflater, R.layout.registration_fragment, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LifecycleOwner viewLifecycleOwner = getViewLifecycleOwner();

        registrationLoadInitDataViewModel.getResult().observe(viewLifecycleOwner, result -> {
            if (result == null) {
                MainActivity.reportError(getActivity(), getString(R.string.fatal_error));
            } else {
                if (registrationFormObject != null) {
                    // must be called before populateData() to restore state properly
                    restoreForm(result, registrationFormObject);
                }
                populateData(result);
                if (registrationFormObject != null) {
                    // fast forward to image picking
                    binding.bntRegister.callOnClick();
                }
            }
        });

        binding.dateOfBirthday.setOnClickListener(view12 -> {
            SimpleDateFormat dtf = Globals.getDTF();
            Date initDate = dtf.parse(binding.dateOfBirthday.getText().toString(), new ParsePosition(0));
            if (initDate == null) {
                //looking for 18 year olds
                initDate = new Date(System.currentTimeMillis() - 18 * Globals.APPROX_ONE_YEAR_IN_MS);
            }
            DialogFragment datePickerFragment = new DatePickerFragment(
                    initDate,
                    (view121, year, monthOfYear, dayOfMonth) -> {
                        Calendar date = Calendar.getInstance();
                        date.set(year, monthOfYear, dayOfMonth);

                        binding.dateOfBirthday.setText(dtf.format(date.getTime()));
                    });
            datePickerFragment.show(requireActivity().getSupportFragmentManager(), "datePicker");
        });

        AreaOfInterest[] areasOfInterests = AreaOfInterest.values();
        List<LabelValue<AreaOfInterest>> elements = new ArrayList<>(areasOfInterests.length);
        for (AreaOfInterest areaOfInterest : areasOfInterests) {
            elements.add(
                    new LabelValue<>(
                            getString(EnumToLabelUtil.getResourceForAreaOfInterest(areaOfInterest)),
                            areaOfInterest
                    )
            );
        }
        ViewUtils.setupAdapter(binding.areaOfInterestSpinner,
                               R.layout.spinner_row_area_of_interest,
                               elements,
                               R.string.area_of_interest_hint
        );
        countryAdapter = ViewUtils.setupAdapter(
                binding.countrySpinner,
                R.layout.spinner_row_country,
                new ArrayList<>(),
                R.string.country_prompt
        );
        nationalFederationAdapter = ViewUtils.setupAdapter(
                binding.federationSpinner,
                R.layout.spinner_row_federation,
                new ArrayList<>(),
                R.string.federation_prompt
        );
        binding.consentCheckboxRow.setOnClickListener(v -> binding.consentCheckbox.toggle());
        binding.consentLink.setMovementMethod(LinkMovementMethod.getInstance());
        setupValidation();

        binding.bntVerify.setOnClickListener(
                view1 -> MainActivity.gotoFragment(requireActivity(), DiverVerificationFragment.newInstance())
        );
        binding.bntRegister.setOnClickListener(
                view1 -> {
                    if (validationHelper.validateAll()) {
                        MainActivity.gotoFragment(requireActivity(), FileAdditionFragment.newInstance(collectForm()));
                    }
                }
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        registrationLoadInitDataViewModel.countrySelectionInitPosition
                = binding.countrySpinner.getSelectedItemPosition();
        registrationLoadInitDataViewModel.federationSelectionInitPosition
                = binding.federationSpinner.getSelectedItemPosition();
        registrationLoadInitDataViewModel.startSingleItemValidation = validationHelper.isStartSingleItemValidation();
    }

    @Override
    public void onResume() {
        super.onResume();
        registrationLoadInitDataViewModel.start(null);
    }

    private void setupValidation() {
        validationHelper.addValidationItem(new ValidationItem(
                new TextValidationView(binding.emailInput), binding.emailNameInputError) {
            @Nullable
            @Override
            public String validateForErrorText() {
                String value = binding.emailInput.getText().toString();
                if (StringUtil.isTrimmedEmpty(value)) {
                    return getString(R.string.error_empty_field);
                }
                if (!value.matches(Globals.SIMPLE_EMAIL_REGEXP)) {
                    return getString(R.string.error_email);
                }
                return null;
            }
        });
        validationHelper.setupEmptyValidation(
                binding.firstNameInput, binding.firstNameInputError
        );
        validationHelper.setupEmptyValidation(
                binding.lastNameInput, binding.lastNameInputError
        );
        validationHelper.setupEmptyValidation(
                binding.dateOfBirthday, binding.dateOfBirthdayError
        );
        validationHelper.setupEmptyValidation(
                binding.countrySpinner, binding.countrySpinnerError, this::getCountryStringValue
        );
        validationHelper.setupEmptyValidation(
                binding.federationSpinner, binding.federationSpinnerError, this::getFederationStringValue
        );
        validationHelper.addValidationItem(new ValidationItem(
                new CheckedBoxValidationView(binding.consentCheckbox), binding.consentCheckboxError) {
            @Nullable
            @Override
            public String validateForErrorText() {
                if (!binding.consentCheckbox.isChecked()) {
                    return getString(R.string.terms_not_agreed);
                }
                return null;
            }
        });
        validationHelper.restoreValidationState();
    }

    private void populateData(RegistrationInitData registrationInitData) {
        List<Country> countries = registrationInitData.countries;
        List<LabelValue<Country>> countriesAndLabels = new ArrayList<>(countries.size() + 1);
        for (Country country : countries) {
            countriesAndLabels.add(new LabelValue<>(country.name, country));
        }
        ViewUtils.populateAdapter(countryAdapter, countriesAndLabels);
        int countrySelectionInitPosition = registrationLoadInitDataViewModel.countrySelectionInitPosition;
        if (countrySelectionInitPosition <= countriesAndLabels.size() && countrySelectionInitPosition != 0) {
            binding.countrySpinner.setSelection(countrySelectionInitPosition);
        }

        List<NationalFederation> nationalFederations = registrationInitData.nationalFederations;
        List<LabelValue<NationalFederation>> federationsAndLabels = new ArrayList<>(nationalFederations.size() + 1);
        for (NationalFederation nationalFederation : nationalFederations) {
            federationsAndLabels.add(new LabelValue<>(nationalFederation.name, nationalFederation));
        }
        ViewUtils.populateAdapter(nationalFederationAdapter, federationsAndLabels);
        int federationSelectionInitPosition = registrationLoadInitDataViewModel.federationSelectionInitPosition;
        if (federationSelectionInitPosition <= federationsAndLabels.size() && federationSelectionInitPosition != 0) {
            binding.federationSpinner.setSelection(federationSelectionInitPosition);
        }
    }

    private void restoreForm(RegistrationInitData registrationInitData,
                             RegistrationFormObject registrationFormObject){
        binding.emailInput.setText(registrationFormObject.email);
        binding.firstNameInput.setText(registrationFormObject.firstName);
        binding.lastNameInput.setText(registrationFormObject.lastName);
        binding.dateOfBirthday.setText(registrationFormObject.dob);

        int federationInitPosition = 0;
        List<NationalFederation> federations = registrationInitData.nationalFederations;
        for (int i = 0; i < federations.size(); i++) {
            NationalFederation federation = federations.get(i);
            if (String.valueOf(federation.id).equals(registrationFormObject.federationId)) {
                federationInitPosition = i;
                break;
            }
        }
        registrationLoadInitDataViewModel.federationSelectionInitPosition = federationInitPosition;

        int countryInitPosition = 0;
        List<Country> countries = registrationInitData.countries;
        for (int i = 0; i < countries.size(); i++) {
            Country country = countries.get(i);
            if (country.code.equals(registrationFormObject.countryCode)) {
                countryInitPosition = i;
                break;
            }
        }
        registrationLoadInitDataViewModel.countrySelectionInitPosition = countryInitPosition;

        AreaOfInterest[] areasOfInterests = AreaOfInterest.values();
        int areaInitPosition = 0;
        for (int i = 0; i < areasOfInterests.length; i++) {
            AreaOfInterest areaOfInterest = areasOfInterests[i];
            if (areaOfInterest.getName().equals(registrationFormObject.areaOfInterest)) {
                areaInitPosition = i;
                break;
            }
        }
        binding.areaOfInterestSpinner.setSelection(areaInitPosition);

        binding.consentCheckbox.setChecked(true);
    }

    private RegistrationFormObject collectForm() {
        return new RegistrationFormObject(
                binding.emailInput.getText().toString(),
                binding.firstNameInput.getText().toString(),
                binding.lastNameInput.getText().toString(),
                binding.dateOfBirthday.getText().toString(),
                getCountryStringValue(),
                getFederationStringValue(),
                getAreaOfInterestStringValue(),
                binding.consentCheckbox.isChecked()
        );
    }

    private String getCountryStringValue() {
        Object country = binding.countrySpinner.getSelectedItem();
        return country == null ? "" : ((LabelValue<Country>) country).value.code;
    }

    private String getFederationStringValue() {
        Object federation = binding.federationSpinner.getSelectedItem();
        return federation == null ? "" : String.valueOf(((LabelValue<NationalFederation>) federation).value.id);
    }

    private String getAreaOfInterestStringValue() {
        Object areaOfInterest = binding.areaOfInterestSpinner.getSelectedItem();
        return areaOfInterest == null ? "" : ((LabelValue<AreaOfInterest>) areaOfInterest).value.name();
    }
}
