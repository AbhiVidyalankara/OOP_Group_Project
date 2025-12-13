// --- Scheduler.java ---
package Medicare;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    public static List<LocalTime> generateSlots() {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(17, 0);

        LocalTime current = start;
        while (current.isBefore(end)) {
            slots.add(current);
            current = current.plusMinutes(30);
        }
        return slots;
    }

    public static boolean isAvailable(String doctorId, LocalDate date, LocalTime time) {
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return false;
        }

        for (Appointment ap : Datastore.store.appointments) {
            if (ap.doctorId.equals(doctorId) && ap.date.equals(date) && ap.time.equals(time)) {
                return false;
            }
        }
        return true;
    }
}