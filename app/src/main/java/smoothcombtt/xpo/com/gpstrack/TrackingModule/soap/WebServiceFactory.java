package smoothcombtt.xpo.com.gpstrack.TrackingModule.soap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import java.lang.reflect.Method;

import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.annotations.KSoapServiceProxy;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.commons.CustomHttpTransportSE;

public class WebServiceFactory
{

    private static WebServiceFactory instance;

    public static WebServiceFactory getFactory()
    {
        return instance == null ? instance = new WebServiceFactory() : instance;
    }

    public WSMobileService getDotNetSoapWebservice(Class<? extends WSMobileService> service)
    {
        if (service.equals(ProxyImpl.class))
        {
            return createAnnotatedDotNetProxy(service);
        }
        return null;
    }

    private WSMobileService createAnnotatedDotNetProxy(Class<? extends WSMobileService> service)
    {
        try
        {
            KSoapServiceProxy annotation = service.getAnnotation(KSoapServiceProxy.class);
            WSMobileService instance = service.newInstance();
            CustomHttpTransportSE androidHttpTransport = new CustomHttpTransportSE(annotation.wsdl().toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            Method method = service.getMethod("setAndroidHttpTransport", CustomHttpTransportSE.class);
            method.invoke(instance, androidHttpTransport);
            method = service.getMethod("setDotNetEnvelope", SoapSerializationEnvelope.class);
            method.invoke(instance, envelope);
            return instance;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
