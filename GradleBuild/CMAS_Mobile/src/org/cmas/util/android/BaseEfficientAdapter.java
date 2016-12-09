package org.cmas.util.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class BaseEfficientAdapter<T, H> extends BaseAdapter {

    protected List<T> data;
    protected LayoutInflater inflater;
    protected int layoutId;

    protected BaseEfficientAdapter(List<T> data, Context context, int layoutId) {
        this.data = data;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    protected abstract H createHolder(View view);

    protected abstract void modifyHolder(H holder, T datum);

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        T datum = data.get(position);
        H holder;
        if (view == null) {
            view = inflater.inflate(layoutId, null);
            holder = createHolder(view);
            view.setTag(holder);
        } else {
            //noinspection unchecked
            holder = (H) view.getTag();
        }
        modifyHolder(holder, datum);

        return view;
    }
}
