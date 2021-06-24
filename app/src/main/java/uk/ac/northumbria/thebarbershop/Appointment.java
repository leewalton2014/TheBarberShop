package uk.ac.northumbria.thebarbershop;

/**
 * class containing database column and table names
 * used in DBJHelper
 */
public class Appointment {
    //table name
    public static final String TABLE = "Appointments";
    // Labels Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_date = "date";
    public static final String KEY_time = "time";
    public static final String KEY_duration = "duration";
    public static final String KEY_barberName = "barberName";
    public static final String KEY_booked = "booked";
    public static final String KEY_customerName = "customerName";
}
