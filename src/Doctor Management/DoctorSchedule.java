import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class DoctorSchedule implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Map<DayOfWeek, TimeSlot> weeklySchedule;
    
    public DoctorSchedule() {
        this.weeklySchedule = new HashMap<>();
    }
    
    public void setDaySchedule(DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        if (startTime != null && endTime != null) {
            weeklySchedule.put(day, new TimeSlot(startTime, endTime));
        } else {
            weeklySchedule.remove(day);
        }
    }
    
    public boolean isAvailableOn(DayOfWeek day) {
        return weeklySchedule.containsKey(day);
    }
    
    public TimeSlot getTimeSlot(DayOfWeek day) {
        return weeklySchedule.get(day);
    }
    
    public Map<DayOfWeek, TimeSlot> getWeeklySchedule() {
        return new HashMap<>(weeklySchedule);
    }
    
    public String getScheduleSummary() {
        if (weeklySchedule.isEmpty()) {
            return "No schedule set";
        }
        
        StringBuilder sb = new StringBuilder();
        DayOfWeek[] days = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, 
                           DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY};
        
        for (DayOfWeek day : days) {
            if (weeklySchedule.containsKey(day)) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(day.toString().substring(0, 3)).append(": ").append(weeklySchedule.get(day));
            }
        }
        
        return sb.toString();
    }
    
    public static class TimeSlot implements Serializable {
        private static final long serialVersionUID = 1L;
        private LocalTime startTime;
        private LocalTime endTime;
        
        public TimeSlot(LocalTime startTime, LocalTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }
        
        public LocalTime getStartTime() { return startTime; }
        public LocalTime getEndTime() { return endTime; }
        
        @Override
        public String toString() {
            return startTime + " - " + endTime;
        }
    }
}