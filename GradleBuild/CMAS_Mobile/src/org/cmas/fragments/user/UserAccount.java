package org.cmas.fragments.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.cmas.Globals;
import org.cmas.mobile.R;
import org.cmas.activities.SecureActivity;
import org.cmas.entities.diver.Diver;
import org.cmas.fragments.MenuFragment;
import org.cmas.service.DrawCardService;

import java.text.MessageFormat;
import java.util.Date;

/**
 * User: ABadretdinov
 * Date: 20.12.13
 * Time: 16:34
 */
public class UserAccount extends BaseUserAccountFragment {

    public static UserAccount newInstance(Bundle data) {
        UserAccount fragment = new UserAccount();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_account, null, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupHeader(getString(R.string.my_account_title), MenuFragment.class);
        View fragmentView = getView();
        ((TextView)fragmentView.findViewById(R.id.profile_layout_title)).setText(R.string.user_account_title_caps);


        firstNameText.setText(currentUser.getFirstName());
        lastNameText.setText(currentUser.getLastName());


       // byte[] userpic = user.getUserpicBytes();
       // mUserUserpicImageView.setImageDrawable(getResources().getDrawable(R.drawable.userpic0));
//        if (userpic != null && userpic.length > 0) {
//            Bitmap bmp = BitmapFactory.decodeByteArray(userpic, 0, userpic.length);
//            mUserUserpicImageView.setImageBitmap(bmp);
//            /*mUserpicDelete.setVisibility(View.VISIBLE);
//            mDefaultUserpic.setVisibility(View.GONE);*/
//        } else {
//            /*mDefaultUserpic.setVisibility(View.VISIBLE);
//            mUserpicDelete.setVisibility(View.GONE);*/
//            if (sex==null||sex.equals("M")) {
//                //mDefaultUserpic.setImageDrawable(getResources().getDrawable(R.drawable.userpic0));
//            } else {
//                //mDefaultUserpic.setImageDrawable(getResources().getDrawable(R.drawable.userpic1));
//            }
//        }

        Date dob = currentUser.getDob();
        if(dob != null){
            //birthDateTime=dob;
            dobText.setText(MessageFormat.format(
                    getString(R.string.date_birthday_format),
                    Globals.getDTF().format(dob)
            ));
        }

        final Diver diver = (Diver) currentUser;
        try {
            ImageView cardView = (ImageView)getView().findViewById(R.id.card);
            cardView.setImageDrawable(
                    DrawCardService.drawUserCard(getActivity(), diver)
            );
            cardView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SecureActivity.showQRCode(
                                    getActivity(), diver
                            );
                        }
                    }
            );
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getMessage(), e);
        }


        changePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPassChange();
            }
        });
    }

    private void doPassChange() {
//        collectProfileData();
//
//        final ProfileManagementService profileManagementService
//                        = BaseBeanContainer.getInstance().getProfileManagementService();
//
//
//        EntityManagementAction<EntityEditReply> action = new EntityManagementAction<EntityEditReply>() {
//            @Override
//            protected Pair<EntityEditReply, String> manageEntity() {
//                return  profileManagementService.editProfile(
//                                getActivity(), user
//                        );
//            }
//        };
//        action.doAction(this, "edit user", ViewProfiles.class);
    }
}