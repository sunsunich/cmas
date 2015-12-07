package org.cmas.util.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import org.cmas.fragments.SecureFragment;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseEfficientFAdapter<T, H> extends BaseAdapter {

    protected List<T> data;
    protected LayoutInflater inflater;
    protected SecureFragment fragment;
    protected int layoutId;

    protected BaseEfficientFAdapter(List<T> data, SecureFragment fragment, int layoutId) {
        this.data = data!=null?data:new ArrayList<T>();
        this.fragment= fragment;
        this.inflater = (LayoutInflater)fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public void setData(List<T> data){
        this.data=data;
        notifyDataSetChanged();
    }

    @Override
    public T getItem(int position) {
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
