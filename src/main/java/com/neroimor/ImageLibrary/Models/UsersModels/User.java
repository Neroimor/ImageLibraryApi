package com.neroimor.ImageLibrary.Models.UsersModels;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


//todo продумать систему подписок
//todo Что еще добавить

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name="nickname")
    private String nickname;
    @Column(name="email")
    private String email;
    @Column(name="password")
    private String password;
    @Column(name="role")
    private String ROLE;
    @Column(name="created_at")
    private Date created_at;
    @Column(name = "verified")
    private boolean verified;
    @Column(name = "codeuid")
    private String codeuid;
}
