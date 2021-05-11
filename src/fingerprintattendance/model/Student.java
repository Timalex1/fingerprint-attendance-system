/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprintattendance.model;

/**
 *
 * @author hp
 */
public class Student {
    
    private String name, matric_no, level;
    private byte[] image_byte;

    public Student(String name, String matric_no, String level, byte [] image_byte) {
        this.name = name;
        this.matric_no = matric_no;
        this.level = level;
        this.image_byte = image_byte;
    }

    public byte[] getImage_byte() {
        return image_byte;
    }

    public void setImage_byte(byte[] image_byte) {
        this.image_byte = image_byte;
    }

    public Student() {
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatric_no() {
        return matric_no;
    }

    public void setMatric_no(String matric_no) {
        this.matric_no = matric_no;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    
    
    
}
