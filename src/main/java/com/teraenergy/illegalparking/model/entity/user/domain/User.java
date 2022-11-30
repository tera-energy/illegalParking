package com.teraenergy.illegalparking.model.entity.user.domain;

import com.google.common.collect.Lists;
import com.teraenergy.illegalparking.encrypt.YoungEncoder;
import com.teraenergy.illegalparking.model.entity.governmentoffice.domain.GovernmentOffice;
import com.teraenergy.illegalparking.model.entity.user.enums.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Date : 2022-09-20
 * Author : young
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
@Entity (name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer userSeq;

    @Column(name = "email", nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Long userCode;

    @Column
    private String photoName;

    @Column
    private String phoneNumber;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "officeSeq")
    private GovernmentOffice governMentOffice;

    @Column(nullable = false)
    private Boolean isDel = false;

    @Column
    private LocalDateTime delDt;

    @Transient
    private boolean accountNonExpired = true;

    @Transient
    private boolean accountNonLocked = true;

    @Transient
    private boolean credentialsNonExpired = true;

    @Transient
    private boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = Lists.newArrayList();
        authorityList.add(new SimpleGrantedAuthority(this.role.getValue()));
        return authorityList;
    }

    public void setEncyptPassword() {
        this.password = YoungEncoder.encrypt(this.password);
    }

    public void setDecryptPassword(){
        this.password = YoungEncoder.decrypt(this.password);
    }
}
