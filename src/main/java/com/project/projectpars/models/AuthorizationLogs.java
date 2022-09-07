package com.project.projectpars.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "authorization_logs")
@NoArgsConstructor
public class AuthorizationLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "action")
    private String action;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "date")
    private Date date;
    @Column(name = "location")
    private String location;

    public AuthorizationLogs(Long userId, Date date, String location, String action) {
        this.userId = userId;
        this.date = date;
        this.location = location;
        this.action = action;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAction() {
        return action;
    }
}
