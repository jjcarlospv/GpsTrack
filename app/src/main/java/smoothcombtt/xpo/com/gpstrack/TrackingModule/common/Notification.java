package smoothcombtt.xpo.com.gpstrack.TrackingModule.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by jose.paucar on 04/12/2015.
 */
public class Notification {

    public static void messageError(final Context currentActivity, String title, String message,
                                    final boolean closeScreen, final Intent intent) {
        try
        {
            AlertDialog.Builder b = new AlertDialog.Builder(currentActivity);
            final AlertDialog create = b.create();
            create.setTitle(title);
            create.setCancelable(true);
            create.setMessage(message);
            create.setButton("Ok",
                    new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            create.cancel();
                            if (intent != null)
                            {
                                currentActivity.startActivity(intent);
                            }

                            if (closeScreen)
                            {
                                //currentActivity.finish();
                            }
                        }
                    });

            create.setOnKeyListener(

                    new DialogInterface.OnKeyListener()
                    {

                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
                        {

                            if (keyCode == KeyEvent.KEYCODE_BACK)
                            {
                                return true;
                            }

                            return false;
                        }
                    });

            create.show();

        }
        catch (Exception e)
        {

        }

    }
}
