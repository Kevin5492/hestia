package com.ispan.hestia.model;

import java.util.Calendar;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="verification_token")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="token_id")
    private Integer tokenId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name="verification_code")
    private String verificationCode;

    @Column(name="expiry_time")//過期時間
    private Date expiryTime;

    @Column(name="created_time")
    private Date createdTime;
    
    //預設10分鐘過期
    public VerificationToken(User user, String verificationCode) {
        this.user = user;
        this.verificationCode = verificationCode;
        this.createdTime = new Date(); 
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(createdTime);
        calendar.add(Calendar.MINUTE, 10); 
        
        this.expiryTime = calendar.getTime();
    }
}
