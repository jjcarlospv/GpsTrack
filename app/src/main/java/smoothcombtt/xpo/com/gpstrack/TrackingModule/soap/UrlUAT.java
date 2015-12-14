package smoothcombtt.xpo.com.gpstrack.TrackingModule.soap;

public class UrlUAT extends MiddleTierURL
{

    public String toString()
    {
        String ret = super.toString();
        if (ret == null)
        {
            return "http://smoothcom-uat.pacer.com/PCMC.WebServices/WSMobileService.asmx";
        }
        return ret;
    }
}
