package org.cmas;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * User: 1
 * Date: 13.12.13
 * Time: 18:20
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener
{
    private Date selected = null;
    private DatePickerDialog.OnDateSetListener listener;
    public DatePickerFragment(Date initDate,DatePickerDialog.OnDateSetListener listener)
    {
        this.selected= initDate!=null?initDate:new Date();
        this.listener=listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(selected);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day)
    {
        if(listener!=null){
            listener.onDateSet(view,year,month,day);
        }
    }
}
