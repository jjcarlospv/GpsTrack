/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.commons;

/**
 * @author piero.delaguila
 */
public class PCMCException extends RuntimeException
{
    private Throwable cause;

    public PCMCException(String msg)
    {
        super(msg);
    }

    public PCMCException(Throwable cause, String msg)
    {
        super(msg);
        this.cause = cause;
        LogHelper.logInfo("ERRORMSG:::" + msg);
        if (cause != null)
        {
       //     LogHelper.logInfo("ERRORCAUSE:::" + cause.getMessage());
        }
        else
        {
            LogHelper.logInfo("ERRORCAUSE::: NONDEFINED");

        }
        Helper.stopNotification();
    }

    public Throwable getCause()
    {
        return this.cause;
    }

    public void setCause(Throwable cause)
    {
        this.cause = cause;
    }

}
