package smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.tos;


public abstract class TransferObject implements Persistable
{
    private Integer planId;
    private Integer moveId;
    private String ExternalMoveId;

    public abstract String toString();

    public Integer getPlanId()
    {
        return planId;
    }

    public void setPlanId(Integer planId)
    {
        this.planId = planId;
    }

    public Integer getMoveId()
    {
        return moveId;
    }

    public void setMoveId(Integer moveId)
    {
        this.moveId = moveId;
    }

    public String getExternalMoveId()
    {
        return this.ExternalMoveId;
    }

    public void setExternalMoveId(String ExternalMoveId)
    {
        this.ExternalMoveId = ExternalMoveId;
    }
}
