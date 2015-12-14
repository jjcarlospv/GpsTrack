package smoothcombtt.xpo.com.gpstrack.TrackingModule.soap;

import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.commons.AnnotatedService;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.annotations.KSoapServiceProxy;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.annotations.MiddleTier;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.tos.ResultTO;

@KSoapServiceProxy(wsdl = MiddleTier.UAT, namespace = "http://clauster123-001-site1.btempurl.com/tracking/")
//http://traking.wsServices/
//http://clauster123-001-site1.btempurl.com/tracking/
public class ProxyImpl extends AnnotatedService implements WSMobileService {

    public String target;

    @Override
    public ResultTO authentification(String pUserName, String pPassword,String pMacAdress )
    {
        return (ResultTO) callWS("Authentification", pUserName, pPassword, pMacAdress);
    }
}
