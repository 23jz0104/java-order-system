package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Call implements Serializable {
    private int           id;
    private int           eventId;
    private int           tableNumber;
    private int           statusId;
    private LocalDateTime createdAt;

    public Call() {

    }

    public Call(int id, int eventId, int tableNumber, int statusId) {
        super();
        this.id          = id;
        this.eventId     = eventId;
        this.tableNumber = tableNumber;
        this.statusId    = statusId;
    }

    public Call(int id, int eventId, int tableNumber, int statusId, LocalDateTime createdAt) {
        super();
        this.id          = id;
        this.eventId     = eventId;
        this.tableNumber = tableNumber;
        this.statusId    = statusId;
        this.createdAt   = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Call [id=" + id + ", eventId=" + eventId + ", tableNumber=" + tableNumber + ", statusId=" + statusId + ", createdAt=" + createdAt
                + "]";
    }

    public String getFormattedOrderDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
