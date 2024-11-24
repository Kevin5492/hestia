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
@Table(name="password_reset_token")
public class PasswordResetToken {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="token_id")
    private Integer tokenId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name="reset_code")
    private String resetCode;

    @Column(name="expiry_time")//過期時間
    private Date expiryTime;

    @Column(name="created_time")
    private Date createdTime;
    
    //預設15分鐘過期
    public PasswordResetToken(User user, String resetCode) {
        this.user = user;
        this.resetCode = resetCode;
        this.createdTime = new Date(); 
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(createdTime);
        calendar.add(Calendar.MINUTE, 15); 
        
        this.expiryTime = calendar.getTime();
    }
	
}
