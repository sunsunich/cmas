package org.cmas.fragments.settings;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.cocosw.undobar.UndoBarController;
import org.cmas.fragments.BaseResultViewFragment;
import org.cmas.mobile.R;
import org.cmas.service.RegistrationService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: ABadretdinov
 * Date: 20.12.13
 * Time: 12:30
 */
public class SettingsList extends BaseResultViewFragment implements AdapterView.OnItemClickListener {

    public static SettingsList newInstance(Bundle data){
        SettingsList fragment=new SettingsList();
        fragment.setArguments(data);
        return fragment;
    }
    public SettingsList(){
        super(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings,null,false);
    }

    private ListView listView;

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

        listView= (ListView) getView().findViewById(android.R.id.list);
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        String[] items = getResources().getStringArray(R.array.settings_items);
        for (String item : items)
        {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("title", item);
            data.add(map);
        }
        listView.setAdapter(new SimpleAdapter(getActivity(), data, R.layout.settings_row, new String[]{"title"}, new int[]{R.id.text}));
        listView.setOnItemClickListener(this);
    }
    private final RegistrationService registrationService = beanContainer.getRegistrationService();

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                replaceCurrentMainFragment(getId(), AddSecureCode.newInstance(null), true);
                break;
        }
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
