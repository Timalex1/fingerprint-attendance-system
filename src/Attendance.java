
import java.time.Instant;

public class Attendance {

    private String student;
    private String course;
    private Instant date;

     public Attendance(String student, String course, Instant date) {
          this.student = student;
          this.course = course;
          this.date = date;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

     


}