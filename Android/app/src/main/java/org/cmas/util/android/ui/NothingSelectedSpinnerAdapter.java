package org.cmas.util.android.ui;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import androidx.annotation.LayoutRes;

/**
 * Created with IntelliJ IDEA.
 * User: 1
 * Date: 08.02.14
 * Time: 15:19
 * To change this template use File | Settings | File Templates.
 */
public class NothingSelectedSpinnerAdapter implements SpinnerAdapter, ListAdapter
{

    protected static final int EXTRA = 1;
    protected final SpinnerAdapter adapter;
    protected final Context context;
    @LayoutRes
    protected final int nothingSelectedLayout;
    @LayoutRes
    protected final int nothingSelectedDropdownLayout;
    protected final LayoutInflater layoutInflater;
    protected final String message;

    /**
     * Use this constructor to have NO 'Select One...' item, instead use
     * the standard prompt or nothing at all.
     * @param spinnerAdapter wrapped Adapter.
     * @param nothingSelectedLayout layout for nothing selected, perhaps
     * you want text grayed out like a prompt...
     * @param context
     */
    public NothingSelectedSpinnerAdapter(SpinnerAdapter spinnerAdapter,
                                         @LayoutRes int nothingSelectedLayout,
                                         Context context,
                                         String msg)
    {

        this(spinnerAdapter, nothingSelectedLayout, -1, context, msg);
    }

    /**
     * Use this constructor to Define your 'Select One...' layout as the first
     * row in the returned choices.
     * If you do this, you probably don't want a prompt on your spinner or it'll
     * have two 'Select' rows.
     * @param spinnerAdapter wrapped Adapter. Should probably return false for isEnabled(0)
     * @param nothingSelectedLayout layout for nothing selected, perhaps you want
     * text grayed out like a prompt...
     * @param nothingSelectedDropdownLayout layout for your 'Select an Item...' in
     * the dropdown.
     * @param context
     */
    public NothingSelectedSpinnerAdapter(SpinnerAdapter spinnerAdapter,
                                         @LayoutRes int nothingSelectedLayout,
                                         @LayoutRes int nothingSelectedDropdownLayout,
                                         Context context,
                                         String msg)
    {
        this.adapter = spinnerAdapter;
        this.context = context;
        this.nothingSelectedLayout = nothingSelectedLayout;
        this.nothingSelectedDropdownLayout = nothingSelectedDropdownLayout;
        layoutInflater = LayoutInflater.from(context);
        this.message = msg;
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            TextView view = (TextView) getNothingSelectedView(parent);
            view.setText(message);
            return view;
        }
        return adapter.getView(position - EXTRA, null, parent); // Could re-use
        // the convertView if possible.
    }

    /**
     * View to show in Spinner with Nothing Selected
     * Override this to do something dynamic... e.g. "37 Options Found"
     * @param parent
     * @return
     */
    protected View getNothingSelectedView(ViewGroup parent) {
        return layoutInflater.inflate(nothingSelectedLayout, parent, false);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // BUG! Vote to fix!! http://code.google.com/p/android/issues/detail?id=17128 -
        // Spinner does not support multiple view types
        if (position == 0) {
            return nothingSelectedDropdownLayout == -1 ?
                    new View(context) :
                    getNothingSelectedDropdownView(parent);
        }

        // Could re-use the convertView if possible, use setTag...
        return adapter.getDropDownView(position - EXTRA, null, parent);
    }

    /**
     * Override this to do something dynamic... For example, "Pick your favorite
     * of these 37".
     * @param parent
     * @return
     */
    protected View getNothingSelectedDropdownView(ViewGroup parent) {
        return layoutInflater.inflate(nothingSelectedDropdownLayout, parent, false);
    }

    @Override
    public int getCount() {
        int count = adapter.getCount();
        return count == 0 ? 0 : count + EXTRA;
    }

    @Override
    public Object getItem(int position) {
        return position == 0 ? null : adapter.getItem(position - EXTRA);
    }

    @Override
    public int getItemViewType(int position) {
        // Doesn't work!! Vote to Fix! http://code.google.com/p/android/issues/detail?id=17128 -
        // Spinner does not support multiple view types
        // This method determines what is the convertView, this should
        // return 1 for pos 0 or return 0 otherwise.
//        return position == 0 ?
//                getViewTypeCount() - EXTRA :
//                adapter.getItemViewType(position - EXTRA);
        return adapter.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return adapter.getViewTypeCount();// + EXTRA;
    }

    @Override
    public long getItemId(int position) {
        return adapter.getItemId(position - EXTRA);
    }

    @Override
    public boolean hasStableIds() {
        return adapter.hasStableIds();
    }

    @Override
    public boolean isEmpty() {
        return adapter.isEmpty();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        adapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        adapter.unregisterDataSetObserver(observer);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return position != 0; // Don't allow the 'nothing selected'
        // item to be picked.
    }
}