package smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.tos;


import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.annotations.KSoapComplexField;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.annotations.KSoapField;

@KSoapComplexField
public class BEArrivalDeparture implements Persistable
{

    private int planId;
    private int moveId;
    private String originDestinationIndicator;
    private String arriveDepartIndicator;
    private String activityDate;
    private String activityTime;
    private String activityTimeZone;
    private String customerSignerName;
    private Integer signature = new Integer(0);
    private String lateReasonCode;
    private String externalMoveId;

    @KSoapField("ExternalMoveId")
    public String getExternalMoveId() {
        return externalMoveId;
    }

    public void setExternalMoveId(String externalMoveId) {
        this.externalMoveId = externalMoveId;
    }

    /**
     * @return the planId
     */
    public int getPlanId()
    {
        return planId;
    }

    /**
     * @param planId the planId to set
     */
    public void setPlanId(int planId)
    {
        this.planId = planId;
    }

    @KSoapField("MoveId")
    public int getMoveId()
    {
        return moveId;
    }

    public void setMoveId(int moveId)
    {
        this.moveId = moveId;
    }

    @KSoapField("OriginDestinationIndicator")
    public String getOriginDestinationIndicator()
    {
        return originDestinationIndicator;
    }

    public void setOriginDestinationIndicator(String originDestinationIndicator)
    {
        this.originDestinationIndicator = originDestinationIndicator;
    }

    @KSoapField("ArriveDepartIndicator")
    public String getArriveDepartIndicator()
    {
        return arriveDepartIndicator;
    }

    public void setArriveDepartIndicator(String arriveDepartIndicator)
    {
        this.arriveDepartIndicator = arriveDepartIndicator;
    }

    @KSoapField("ActivityDate")
    public String getActivityDate()
    {
        return activityDate;
    }

    public void setActivityDate(String activityDate)
    {
        this.activityDate = activityDate;
    }

    @KSoapField("ActivityTime")
    public String getActivityTime()
    {
        return activityTime;
    }

    public void setActivityTime(String activityTime)
    {
        this.activityTime = activityTime;
    }

    @KSoapField("ActivityTimeZone")
    public String getActivityTimeZone()
    {
        return activityTimeZone;
    }

    public void setActivityTimeZone(String activityTimeZone)
    {
        this.activityTimeZone = activityTimeZone;
    }

    @KSoapField("CustomerSignerName")
    public String getCustomerSignerName()
    {
        return customerSignerName;
    }

    public void setCustomerSignerName(String customerSignerName)
    {
        this.customerSignerName = customerSignerName;
    }

    @KSoapField("Signature")
    public Integer getSignature()
    {
        return signature;
    }

    public void setSignature(Integer signature)
    {
        this.signature = signature;
    }

    /**
     * @return the lateReasonCode
     */
    @KSoapField("LateReasonCode")
    public String getLateReasonCode()
    {
        return lateReasonCode;
    }

    /**
     * @param lateReasonCode the lateReasonCode to set
     */
    public void setLateReasonCode(String lateReasonCode)
    {
        this.lateReasonCode = lateReasonCode;
    }

    public String toString()
    {
        StringBuffer ret = new StringBuffer();
        ret.append(moveId).append(',');
        ret.append(activityDate).append(',');
        ret.append(activityTime).append(',');
        ret.append(activityTimeZone).append(',');
        ret.append(arriveDepartIndicator).append(',');
        ret.append(originDestinationIndicator).append(',');
        ret.append(customerSignerName).append(',');
        ret.append(signature);
        return ret.toString();
    }

}
