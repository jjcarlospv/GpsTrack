package smoothcombtt.xpo.com.gpstrack.TrackingModule.soap;

import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.annotations.KSoapDotNetService;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.annotations.KSoapServiceParam;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.tos.ResultTO;

public interface WSMobileService {

    /**
     * Authentification
     */
    @KSoapDotNetService(methodName = "Authentification")/////////////////////
    public ResultTO authentification(

            @KSoapServiceParam("pUserName") String pUserName,
            @KSoapServiceParam("pPassword") String pPassword,
            @KSoapServiceParam("pMacAdress") String pMacAdress) ;

}
