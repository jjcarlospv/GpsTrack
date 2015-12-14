package smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.commons;

public class LogHelper extends Helper
{

    private static boolean configured = false;
    private static long APP_ID;

    // public static final int TYPE_VIEWER_STRING = EventLogger.VIEWER_STRING;

    private LogHelper()
    {
    }

    public static void configure(long appId, String appName, int viewerType)
    {
        // EventLogger.register(appId, appName, viewerType);
        LogHelper.APP_ID = appId;
        configured = true;
    }

    public static void logError(String msg)
    {
        if (configured)
        {
            System.out.println(msg);
            // EventLogger.logEvent(LogHelper.APP_ID, (msg).getBytes(),
            // EventLogger.ERROR);
        }
    }

    public static void logDebug(String msg)
    {
        if (configured)
        {
            System.out.println(msg);
            // EventLogger.logEvent(LogHelper.APP_ID, (msg).getBytes(),
            // EventLogger.DEBUG_INFO);
        }
    }

    public static void logInfo(String msg)
    {
        if (configured)
        {
            System.out.println(msg);
            // EventLogger.logEvent(LogHelper.APP_ID, (msg).getBytes(),
            // EventLogger.INFORMATION);
        }
    }

}
