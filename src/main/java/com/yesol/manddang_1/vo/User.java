package com.yesol.manddang_1.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name="USER")
public class User implements UserDetails {
    @Id
    @Column(name = "USER_ID")
    //@GeneratedValue(strategy= GenerationType.IDENTITY)
    private String userId;

    @Column(name = "USER_PWD")
    private String userPwd;

    @CreatedDate//FIXME
    @Column(name = "REG_DATE")
    private Date regDate;
    @Column(name = "USER_AUTH")
    private String userAuth;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.userAuth));
    }

    @Override
    public String getPassword() {
        return this.userPwd;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
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
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", regDate=" + regDate +
                ", userAuth='" + userAuth + '\'' +
                '}';
    }
}
