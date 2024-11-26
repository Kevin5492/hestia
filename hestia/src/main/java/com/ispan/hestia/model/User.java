package com.ispan.hestia.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@DynamicInsert
@Entity
@Table(name = "[user]")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "name")
	private String name;

	@Column(name = "birthdate")
	private Date birthdate;

	@Column(name = "photo")
	private byte[] photo;

	@Column(name = "is_provider")
	private boolean isProvider;

	@ManyToOne
	@JoinColumn(name = "state_id", referencedColumnName = "state_id")
	private State state;

	@Column(name = "login_attempts")
	private Integer loginAttempts = 0;

	@Column(name = "login_attempts_last")
	private Date loginAttemptsLast;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "last_edit_time")
	private Date lastEditTime;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
	@JsonManagedReference
	private Provider provider;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
	private Cart cart;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "user")
	private Set<Order> order = new HashSet<>();

	public boolean getIsProvider() {
		return isProvider;
	}

	public void setIsProvider(boolean isProvider) {
		this.isProvider = isProvider;
	}

}
