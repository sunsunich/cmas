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
import androidx.lifecycle.ViewModelProvider;
import com.cmas.cmas_flutter.R;
import com.cmas.cmas_flutter.databinding.RegistrationFragmentBinding;
import org.cmas.Globals;
import org.cmas.android.MainActivity;
import org.cmas.android.storage.entities.Country;
import org.cmas.android.storage.entities.sport.NationalFederation;
import org.cmas.android.ui.EnumToLabelUtil;
import org.cmas.android.ui.FileAdditionFragment;
import org.cmas.android.ui.verify.DiverVerificationFragment;
import org.cmas.android.validation.CheckedBoxValidationView;
import org.cmas.android.validation.TextValidationView;
import org.cmas.android.validation.ValidationHelper;
import org.cmas.android.validation.ValidationItem;
import org.cmas.entities.diver.AreaOfInterest;
import org.cmas.util.LabelValue;
import org.cmas.util.StringUtil;
import org.cmas.util.android.ui.DatePickerFragment;
import org.cmas.util.android.ui.ViewUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RegistrationFragment extends Fragment {

    private static final String COUNTRY_SPINNER_SELECTION_KEY = "COUNTRY_SPINNER_SELECTION";
    private static final String FEDERATIONS_SPINNER_SELECTION_KEY = "FEDERATIONS_SPINNER_SELECTION";

    private RegistrationFragmentBinding binding;
    private ArrayAdapter<LabelValue<Country>> countryAdapter;
    private int countrySelectionInitPosition = 0;
    private ArrayAdapter<LabelValue<NationalFederation>> nationalFederationAdapter;
    private int federationSelectionInitPosition = 0;

    private RegistrationLoadInitDataViewModel registrationLoadInitDataViewModel;
    private ValidationHelper validationHelper;

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        registrationLoadInitDataViewModel = new ViewModelProvider(this).get(RegistrationLoadInitDataViewModel.class);
        validationHelper = new ValidationHelper(savedInstanceState);
        if (savedInstanceState != null) {
            countrySelectionInitPosition = savedInstanceState.getInt(COUNTRY_SPINNER_SELECTION_KEY, 0);
            federationSelectionInitPosition = savedInstanceState.getInt(FEDERATIONS_SPINNER_SELECTION_KEY, 0);
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.registration_fragment, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LifecycleOwner viewLifecycleOwner = getViewLifecycleOwner();
        registrationLoadInitDataViewModel.init();

        registrationLoadInitDataViewModel.getResult().observe(viewLifecycleOwner, result -> {
            if (result == null) {
                MainActivity.reportError(getActivity(), getString(R.string.fatal_error));
            } else {
                populateData(result);
            }
        });

        binding.dateOfBirthday.setOnClickListener(view12 -> {
            DialogFragment datePickerFragment = new DatePickerFragment(
                    new Date(),
                    (view121, year, monthOfYear, dayOfMonth) -> {
                        Calendar date = Calendar.getInstance();
                        date.set(year, monthOfYear, dayOfMonth);
                        binding.dateOfBirthday.setText(Globals.getDTF().format(date.getTime()));
                    });
            datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
        });

        AreaOfInterest[] areasOfInterests = AreaOfInterest.values();
        List<LabelValue<AreaOfInterest>> elements = new ArrayList<>(areasOfInterests.length + 1);
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
                view1 -> MainActivity.gotoFragment(getActivity(), DiverVerificationFragment.newInstance())
        );
        binding.bntRegister.setOnClickListener(
                view1 -> {
                    if (validationHelper.validateAll()) {
                        MainActivity.gotoFragment(getActivity(), FileAdditionFragment.newInstance(collectForm()));
                    }
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        registrationLoadInitDataViewModel.start();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        validationHelper.saveUiState(savedInstanceState);
        savedInstanceState.putInt(COUNTRY_SPINNER_SELECTION_KEY, binding.countrySpinner.getSelectedItemPosition());
        savedInstanceState.putInt(FEDERATIONS_SPINNER_SELECTION_KEY, binding.federationSpinner.getSelectedItemPosition());
    }

    private void setupValidation() {
        validationHelper.addValidationItem(new ValidationItem(
                new TextValidationView(binding.emailNameInput), binding.emailNameInputError) {
            @Nullable
            @Override
            public String validateForErrorText() {
                String value = binding.emailNameInput.getText().toString();
                if (StringUtil.isTrimmedEmpty(value)) {
                    return getString(R.string.empty_field_error);
                }
                if (!value.matches(Globals.SIMPLE_EMAIL_REGEXP)) {
                    return getString(R.string.email_error);
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
        if (countrySelectionInitPosition < countriesAndLabels.size()) {
            binding.countrySpinner.setSelection(countrySelectionInitPosition);
        }

        List<NationalFederation> nationalFederations = registrationInitData.nationalFederations;
        List<LabelValue<NationalFederation>> federationsAndLabels = new ArrayList<>(nationalFederations.size() + 1);
        for (NationalFederation nationalFederation : nationalFederations) {
            federationsAndLabels.add(new LabelValue<>(nationalFederation.name, nationalFederation));
        }
        ViewUtils.populateAdapter(nationalFederationAdapter, federationsAndLabels);
        if (federationSelectionInitPosition < countriesAndLabels.size()) {
            binding.federationSpinner.setSelection(federationSelectionInitPosition);
        }
    }

    private RegistrationFormObject collectForm() {
        return new RegistrationFormObject(
                binding.emailNameInput.getText().toString(),
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
