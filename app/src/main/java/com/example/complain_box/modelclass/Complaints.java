package com.example.complain_box.modelclass;

import java.io.Serializable;

public class Complaints implements Serializable {
    String emp_name = null;
    String complaint_id = null;
    String subject = null;

    String description = null;
    String status = null;
    String upvote = null;
    String upvotedbyyouornot=null;

    public String getUpvotedbyyouornot() {
        return upvotedbyyouornot;
    }

    public void setUpvotedbyyouornot(String upvotedbyyouornot) {
        this.upvotedbyyouornot = upvotedbyyouornot;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getComplaint_id() {
        return complaint_id;
    }

    public void setComplaint_id(String complaint_id) {
        this.complaint_id = complaint_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpvote() {
        return upvote;
    }

    public void setUpvote(String upvote) {
        this.upvote = upvote;
    }

}
