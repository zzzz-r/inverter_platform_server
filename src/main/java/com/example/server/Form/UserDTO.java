package com.example.server.Form;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String userName;
    private String password;
    private String email;
    private String phone;
    private Integer type;
    private Integer alarmMessage;
    private Integer instituteId;
    private String institute;
    private String avatar;
    private String token;
}
