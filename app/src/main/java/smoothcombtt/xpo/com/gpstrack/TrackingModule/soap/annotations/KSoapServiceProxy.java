package smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface KSoapServiceProxy
{

    MiddleTier wsdl();

    String namespace();

}
