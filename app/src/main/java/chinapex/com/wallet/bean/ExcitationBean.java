package chinapex.com.wallet.bean;

public class ExcitationBean {
    private boolean isEventNew;
    private String newEventPic;
    private String newEventText;
    private int newEventStatus;
    private int gasLimit;

    public boolean isEventNew() {
        return isEventNew;
    }

    public void setEventNew(boolean eventNew) {
        isEventNew = eventNew;
    }

    public String getNewEventPic() {
        return newEventPic;
    }

    public void setNewEventPic(String newEventPic) {
        this.newEventPic = newEventPic;
    }

    public String getNewEventText() {
        return newEventText;
    }

    public void setNewEventText(String newEventText) {
        this.newEventText = newEventText;
    }

    public int getNewEventStatus() {
        return newEventStatus;
    }

    public void setNewEventStatus(int newEventStatus) {
        this.newEventStatus = newEventStatus;
    }

    public int getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(int gasLimit) {
        this.gasLimit = gasLimit;
    }

    @Override
    public String toString() {
        return "ExcitationBean{" +
                "isEventNew=" + isEventNew +
                ", newEventPic='" + newEventPic + '\'' +
                ", newEventText='" + newEventText + '\'' +
                ", newEventStatus=" + newEventStatus +
                '}';
    }
}
