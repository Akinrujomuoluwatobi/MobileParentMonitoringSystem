package Model;

/**
 * Created by PROG. TOBI on 17-Feb-17.
 */
public class Results {
    private String subject, grade;
    private int ca1, ca2, total;
    private int exam;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Results(){
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getCa1() {
        return ca1;
    }

    public void setCa1(int ca1) {
        this.ca1 = ca1;
    }

    public int getCa2() {
        return ca2;
    }

    public void setCa2(int ca2) {
        this.ca2 = ca2;
    }

    public int getExam() {
        return exam;
    }

    public void setExam(int exam) {
        this.exam = exam;
    }
}
