package app.sunny.authe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by sunday-pc on 11/23/17.
 */

public class Alert  {


    private Context mContext;
    private ProgressDialog nativeDialog;
    private AlertDialog alertDialog;

    public Alert(Context ctx)
    {
        mContext=ctx;
        nativeDialog= new ProgressDialog(mContext);
    }

    public void Dismiss()
    {
        if(nativeDialog!=null && nativeDialog.isShowing())nativeDialog.dismiss();
        if(alertDialog!=null)alertDialog.dismiss();
    }

    public  void Show(String mess)
    {
        Dismiss();
        alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(mess);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public  void Show(String title,String mess)
    {
        Dismiss();
        alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(mess);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void Progress(String title)
    {
        Dismiss();
        try {
            nativeDialog.setCancelable(false);
            nativeDialog.setCanceledOnTouchOutside(false);
            nativeDialog.setMessage(title);
            nativeDialog.show();
        }
        catch (Exception e){}
    }

}
