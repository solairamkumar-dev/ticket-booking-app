package com.train.ticket.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name= "passenger")
public class Passenger implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name= "id")
    private Long id;
    @Column(unique = true, nullable = false,name = "user_name")
    private String username;
    @Column(name= "password")
    private String password;
   // @Column(unique = true, nullable = false,name= "email")
   // private String email;
    private boolean enabled;
    @Column(name= "verification_code")
    private String verificationCode;
    @Column(name= "verification_expiration")
    private LocalDateTime verificationCodeExpirationAt;

    public Passenger(){}

    public Passenger( String username ,String password) {
        this.username = username;
        this.password = password;
        //this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }*/

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public LocalDateTime getVerificationCodeExpirationAt() {
        return verificationCodeExpirationAt;
    }

    public void setVerificationCodeExpirationAt(LocalDateTime verificationCodeExpirationAt) {
        this.verificationCodeExpirationAt = verificationCodeExpirationAt;
    }
}
