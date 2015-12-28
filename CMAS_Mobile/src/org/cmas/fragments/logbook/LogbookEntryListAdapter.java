package org.cmas.fragments.logbook;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.cmas.R;
import org.cmas.entities.logbook.LogbookEntry;

import java.util.ArrayList;
import java.util.List;

//@SuppressWarnings("MethodOnlyUsedFromInnerClass")
//убрал зависимость, т.к. последний элемент-кнопка.
public class LogbookEntryListAdapter extends BaseAdapter {

    private final Activity activity;
    private final List<LogbookEntry> data;
    protected LayoutInflater inflater;

    public LogbookEntryListAdapter(Activity activity, List<LogbookEntry> data) {
        //super(data, activity, R.layout.profile_row);
        this.activity = activity;
        this.data = data != null ? data : new ArrayList<LogbookEntry>();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private static LogbookEntryListAdapter.LogbookEntryRowHolder createHolder(View view) {
        return new LogbookEntryListAdapter.LogbookEntryRowHolder(
                (TextView) view.findViewById(R.id.document_name),
                (TextView) view.findViewById(R.id.date_creation)
        );
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public LogbookEntry getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        LogbookEntry datum = data.get(position);
        LogbookEntryListAdapter.LogbookEntryRowHolder holder;
        if (view == null || view.getTag() == null) {
            view = inflater.inflate(R.layout.logbook_entry_row, null);
            holder = createHolder(view);
            view.setTag(holder);
        } else {
            holder = (LogbookEntryListAdapter.LogbookEntryRowHolder) view.getTag();
        }

        holder.entryName.setText(datum.getName());

//        DocFileDao docFileDao = BaseBeanContainer.getInstance().getDocFileDao();
//        List<File> files = docFileDao.getFiles(activity, datum);
//        if (files == null || files.isEmpty()) {
//            holder.dateCreation.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right_gray, 0);
//        } else {
//            holder.dateCreation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.file, 0, R.drawable.arrow_right_gray, 0);
//        }

        return view;
    }

    private static final class LogbookEntryRowHolder {
        private final TextView entryName;
        private final TextView dateCreation;

        private LogbookEntryRowHolder(TextView entryName, TextView dateCreation) {
            this.entryName = entryName;
            this.dateCreation = dateCreation;
        }
    }
}
