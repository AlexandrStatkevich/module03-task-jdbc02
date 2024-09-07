package by.alst.je.jdbc.entity;

import java.util.Objects;

public class Seat {
    private Integer aircraftId;
    private String seatNo;

    private final int SEAT_NO_LENGTH = 4;

    public Seat() {
    }

    public Seat(Integer aircraftId, String seatNo) {
        this.aircraftId = aircraftId;
        if (seatNo.length() == SEAT_NO_LENGTH) {
            this.seatNo = seatNo;
        } else {
            this.seatNo = seatNo + " ".repeat(Math.max(0, SEAT_NO_LENGTH - seatNo.length()));
        }
    }

    public Integer getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(Integer aircraftId) {
        this.aircraftId = aircraftId;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        if (seatNo.length() == SEAT_NO_LENGTH) {
            this.seatNo = seatNo;
        } else {
            this.seatNo = seatNo + " ".repeat(Math.max(0, SEAT_NO_LENGTH - seatNo.length()));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return Objects.equals(aircraftId, seat.aircraftId) && Objects.equals(seatNo, seat.seatNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aircraftId, seatNo);
    }

    @Override
    public String toString() {
        return "Seat{" +
               "aircraftId=" + aircraftId +
               ", seatNo='" + seatNo + '\'' +
               '}';
    }
}
