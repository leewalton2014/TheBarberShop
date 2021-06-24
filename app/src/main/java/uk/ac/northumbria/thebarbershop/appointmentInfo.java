package uk.ac.northumbria.thebarbershop;

/**
 * old class no longer used since replaced with cursor adapter
 */
public class appointmentInfo {
    String date;
    String time;

    public appointmentInfo(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
