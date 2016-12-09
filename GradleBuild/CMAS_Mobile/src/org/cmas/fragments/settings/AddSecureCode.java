package org.cmas.fragments.settings;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.cocosw.undobar.UndoBarController;
import org.cmas.mobile.R;
import org.cmas.service.RegistrationService;
import org.cmas.util.DialogUtils;
import org.cmas.util.ProgressTask;
import org.cmas.util.StringUtil;

/**
 * User: ABadretdinov
 * Date: 20.12.13
 * Time: 12:35
 */
public class AddSecureCode extends BaseSettingView{

    private final RegistrationService registrationService = beanContainer.getRegistrationService();

    public static AddSecureCode newInstance(Bundle data){
        AddSecureCode fragment=new AddSecureCode();
        fragment.setArguments(data);
        return fragment;
    }

    public AddSecureCode() {
        super(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_secure_code,null,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        TextView title= (TextView) getActivity().getLayoutInflater().inflate(R.layout.title_layout,null,false);
        title.setText(makeHtmlTitle(getString(R.string.settings)));
        actionBar.setCustomView(title);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.settings_white);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button confirmCodeButton = (Button) getView().findViewById(R.id.confirm_code);
        confirmCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirmCode();
            }
        });

        Button clearCodeButton = (Button) getView().findViewById(R.id.clear_code);
        clearCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearCode();
            }
        });

        final TextView codeInput1 = (TextView) getView().findViewById(R.id.code_input1);
        final TextView codeInput2 = ((TextView) getView().findViewById(R.id.code_input2));
        final TextView codeInput3 = ((TextView) getView().findViewById(R.id.code_input3));
        final TextView codeInput4 = ((TextView) getView().findViewById(R.id.code_input4));

        final TextView repeatCodeInput1 = (TextView) getView().findViewById(R.id.repeat_code_input1);
        final TextView repeatCodeInput2 = ((TextView) getView().findViewById(R.id.repeat_code_input2));
        final TextView repeatCodeInput3 = ((TextView) getView().findViewById(R.id.repeat_code_input3));
        final TextView repeatCodeInput4 = ((TextView) getView().findViewById(R.id.repeat_code_input4));

        setInputFocusChange(repeatCodeInput1, codeInput1, codeInput2, codeInput3, codeInput4);

        setInputFocusChange(clearCodeButton, repeatCodeInput1, repeatCodeInput2, repeatCodeInput3, repeatCodeInput4);
    }
    private void onConfirmCode() {


        final TextView codeInput1 = (TextView) getView().findViewById(R.id.code_input1);
        final TextView codeInput2 = ((TextView) getView().findViewById(R.id.code_input2));
        final TextView codeInput3 = ((TextView) getView().findViewById(R.id.code_input3));
        final TextView codeInput4 = ((TextView) getView().findViewById(R.id.code_input4));

        final TextView repeatCodeInput1 = (TextView) getView().findViewById(R.id.repeat_code_input1);
        final TextView repeatCodeInput2 = ((TextView) getView().findViewById(R.id.repeat_code_input2));
        final TextView repeatCodeInput3 = ((TextView) getView().findViewById(R.id.repeat_code_input3));
        final TextView repeatCodeInput4 = ((TextView) getView().findViewById(R.id.repeat_code_input4));

        final String code = evalCode(codeInput1, codeInput2, codeInput3, codeInput4);
        final String repeatCode = evalCode(repeatCodeInput1, repeatCodeInput2, repeatCodeInput3, repeatCodeInput4);

        setCode(code, repeatCode);
    }

    private void onClearCode() {
        setCode("", "");
    }

    private void setCode(final String code, final String repeatCode) {
        final Activity activity=getActivity();
        DialogUtils.showLoaderDialog(getFragmentManager(), new ProgressTask<String>() {

            @Override
            public String doTask(OnPublishProgressListener listener) {
                return registrationService
                        .addCode(activity, currentUsername, code, repeatCode);
            }

            @Override
            public void doAfterTask(String result) {
                if (StringUtil.isTrimmedEmpty(result)) {
                    reportSaveSuccess(SettingsList.class, new Bundle());
                } else {
                    reportError(result);
                }
            }

            @Override
            public void handleError(String error) {
                undoBar=UndoBarController.show(activity,getString(R.string.error_connecting_to_server),new UndoBarController.UndoListener() {
                    @Override
                    public void onUndo(Parcelable token) {
                        setCode(code,repeatCode);
                    }
                });
            }

            @Override
            public String getName() {
                return "addCode";
            }
        });
    }

    private UndoBarController undoBar;

    @Override
    public void onDestroyView() {
        if(undoBar!=null){
            undoBar.hide();
        }
        super.onDestroyView();
    }
}
