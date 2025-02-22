package dto;

import java.io.Serializable;

import model.Call;
import model.Event;
import model.Status;

public class CallDTO implements Serializable {
    private Call   call;
    private Event  event;
    private Status status;
    private String formattedOrderTime;

    public CallDTO() {

    }

    public CallDTO(Call call, Event event, Status status, String formattedOrderTime) {
        super();
        this.call               = call;
        this.event              = event;
        this.status             = status;
        this.formattedOrderTime = formattedOrderTime;
    }

    public Call getCall() {
        return call;
    }

    public void setCall(Call call) {
        this.call = call;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getFormattedOrderTime() {
        return formattedOrderTime;
    }

    public void setFormattedOrderTime(String formattedOrderTime) {
        this.formattedOrderTime = formattedOrderTime;
    }

    @Override
    public String toString() {
        return "CallDTO [call=" + call + ", event=" + event + ", status=" + status + ", formattedOrderTime=" + formattedOrderTime + "]";
    }

}
