package org.cmas.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.ContextThemeWrapper;
import org.cmas.mobile.R;
import org.cmas.fragments.LoaderDialogFragment;

/**
 * User: ABadretdinov
 * Date: 28.03.13
 * Time: 17:30
 */
public class DialogUtils
{
	public static void showError(Context context, Exception e, DialogInterface.OnClickListener listener)
	{
		try
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(context.getString(R.string.error));
			builder.setMessage(Html.fromHtml(e.getLocalizedMessage()));
			builder.setNegativeButton(android.R.string.cancel, listener);
			builder.show();
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}

	public static void showAlert(Context context, String alert, DialogInterface.OnClickListener listener)
	{
		try
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context,R.style.CMAS_Dialog));
			builder.setTitle(context.getString(R.string.cv_alert));
			builder.setMessage(alert);
			builder.setPositiveButton(R.string.cv_next, listener);
			builder.setCancelable(false);
			builder.show();
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}

	public static void showAlertWithAction(Context context, String alert, DialogInterface.OnClickListener alertListener,String actionText,DialogInterface.OnClickListener actionListener)
	{
		try
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(context.getString(R.string.cv_alert));
			builder.setMessage(alert);
			builder.setNegativeButton(R.string.cv_next, alertListener);
			builder.setPositiveButton(actionText, actionListener);
			builder.setCancelable(false);
			builder.show();
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}

	public static void showYesNoDialog(Context context, String msg, String confirmMsg,
									   DialogInterface.OnClickListener yesListener)
	{
		showYesNoDialog(context, msg, confirmMsg, yesListener,new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
	}
	public static void showYesNoDialog(Context context, String msg, String confirmMsg,
									   DialogInterface.OnClickListener yesListener,DialogInterface.OnClickListener noListener)
	{
		try
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(context.getString(R.string.cv_alert));
			builder.setMessage(msg);
			builder.setPositiveButton(confirmMsg, yesListener);
			builder.setNegativeButton(android.R.string.no, noListener);
			builder.show();
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}
	public static void showYesNoCancelDialog(Context context, String msg, String confirmMsg,
											 DialogInterface.OnClickListener yesListener,DialogInterface.OnClickListener noListener)
	{
		try
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(context.getString(R.string.cv_alert));
			builder.setMessage(msg);
			builder.setPositiveButton(confirmMsg, yesListener);
			builder.setNegativeButton(R.string.cv_no, noListener);
			builder.setNeutralButton(android.R.string.cancel,new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			});
			builder.show();
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}
    public static <T> void showLoaderDialog(FragmentManager fragmentManager,ProgressTask<T> progressTask) {
        showLoaderDialog(fragmentManager,progressTask,null);
    }
    public static <T> void showLoaderDialog(FragmentManager fragmentManager,ProgressTask<T> progressTask,String loadingMessage) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag("dialog");
        if (prev != null) {
            //((DialogFragment)prev).dismissAllowingStateLoss();
            ft.remove(prev);//todo вызывать dismiss мб сперва?
        }
        //ft.addToBackStack(null);
        LoaderDialogFragment<T> fragment = new LoaderDialogFragment<T>(progressTask,loadingMessage);
        fragment.show(ft, "dialog");
    }
}
