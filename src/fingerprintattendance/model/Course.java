/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprintattendance.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author hp
 */
public class Course {

    private StringProperty course_title;
    private StringProperty course_code;
    private StringProperty course_teacher;

    public Course() {
        this.course_title = new SimpleStringProperty();
        this.course_code = new SimpleStringProperty();
        this.course_teacher = new SimpleStringProperty();
    }
    
    
//    public Course(SimpleStringProperty course_title, SimpleStringProperty course_code, SimpleStringProperty course_teacher) {
//        this.course_title = course_title;
//        this.course_code = course_code;
//        this.course_teacher = course_teacher;
//    }
    
    /* Getters and setters */
    

//    Course title
    public String course_title_get() {
        return course_title.get();
    }

    public StringProperty getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title.set(course_title);
    }

//    Course code
    public void setCourse_code(String course_code) {
        this.course_code.set(course_code);
    }

    public String course_code_get() {
        return course_code.get();
    }

    public StringProperty getCourse_code() {
        return course_code;
    }

//    Course teacher
    public void setCourse_teacher(String course_teacher) {
        this.course_teacher.set(course_teacher);
    }

    public StringProperty getCourse_teacher() {
        return course_teacher;
    }

    public String course_teacher_get() {
        return course_teacher.get();
    }

}
