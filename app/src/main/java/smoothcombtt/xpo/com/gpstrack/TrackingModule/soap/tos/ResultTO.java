package smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.tos;


import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.annotations.KSoapField;

public class ResultTO extends TransferObject
{
    private int UserId;
    private String Password;
    private String UserName;
    private String Email;

    public int getUserId() {
        return UserId;
    }

    @KSoapField("UserId")
    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getPassword() {
        return Password;
    }

    @KSoapField("Password")
    public void setPassword(String password) {
        Password = password;
    }

    public String getUserName() {
        return UserName;
    }

    @KSoapField("UserName")
    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    @KSoapField("Email")
    public void setEmail(String email) {
        Email = email;
    }

    @Override
    public String toString() {
        return null;
    }
}
