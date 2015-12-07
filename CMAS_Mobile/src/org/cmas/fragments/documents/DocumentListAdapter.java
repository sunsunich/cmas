package org.cmas.fragments.documents;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.cmas.BaseBeanContainer;
import org.cmas.R;
import org.cmas.dao.doc.DocFileDao;
import org.cmas.entities.doc.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//@SuppressWarnings("MethodOnlyUsedFromInnerClass")
//убрал зависимость, т.к. последний элемент-кнопка.
public class DocumentListAdapter extends BaseAdapter {

    private Activity activity;
    private List<Document> data;
    protected LayoutInflater inflater;

    public DocumentListAdapter(Activity activity,List<Document> data) {
        //super(data, activity, R.layout.profile_row);
        this.activity = activity;
        this.data = data != null ? data : new ArrayList<Document>();
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    protected DocumentRowHolder createHolder(View view) {
        return new DocumentRowHolder(
                (TextView) view.findViewById(R.id.document_name),
                (TextView) view.findViewById(R.id.date_creation)
        );
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Document getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        Document datum = data.get(position);
        DocumentRowHolder holder;
        if (view == null || view.getTag() == null) {
            view = inflater.inflate(R.layout.document_row, null);
            holder = createHolder(view);
            view.setTag(holder);
        } else {
            holder = (DocumentRowHolder) view.getTag();
        }

        holder.documentName.setText(datum.getName());

        DocFileDao docFileDao = BaseBeanContainer.getInstance().getDocFileDao();
        List<File> files = docFileDao.getFiles(activity, datum);
        if (files == null || files.isEmpty()) {
            holder.dateCreation.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right_gray, 0);
        } else {
            holder.dateCreation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.file, 0, R.drawable.arrow_right_gray, 0);
        }

        return view;
    }

    private class DocumentRowHolder {
        public TextView documentName;
        public TextView dateCreation;

        public DocumentRowHolder(TextView documentName, TextView dateCreation) {
            this.documentName = documentName;
            this.dateCreation = dateCreation;
        }
    }
}
