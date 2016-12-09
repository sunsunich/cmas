package org.cmas.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActionBarActivity;
import org.cmas.mobile.R;
import org.cmas.activities.SecureActivity;
import org.cmas.entities.diver.Diver;
import org.cmas.fragments.settings.SettingsList;

/**
 * User: ABadretdinov
 * Date: 18.12.13
 * Time: 19:22
 */
public class MenuFragment extends SecureFragment {

    public MenuFragment() {
        super(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu_layout, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        view.findViewById(R.id.qr_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecureActivity.showQRCode(getActivity(), (Diver) currentUser);
            }
        });
        view.findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity() instanceof SlidingActionBarActivity) {
                    ((SlidingActionBarActivity) getActivity()).toggle();
                }
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                //если настройки уже открыты,то повторно открывать их не нужно
                if (!(fragmentManager.findFragmentById(R.id.content_frame) instanceof SettingsList)) {
                    // чистим весь стек вплоть до этого класса, чтобы кнопки back в меню работали правильно
                    // при новых вызовах меню
                    fragmentManager.popBackStack(SettingsList.class.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left, R.anim.slide_from_left, R.anim.slide_to_right);
                    transaction.replace(R.id.content_frame, SettingsList.newInstance(null));
                    //чтобы по нажатию кнопки назад, мы возвращались сюда, а не закрывали активити.
                    transaction.addToBackStack(SettingsList.class.getName());
                    transaction.commit();
                }
            }
        });
        view.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loaderLogout();
            }
        });

    }
}
