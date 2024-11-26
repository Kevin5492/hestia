package com.ispan.hestia.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "[order]")
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Integer orderId;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 加入時間部分
	@Temporal(TemporalType.TIMESTAMP) // 設定為時間戳
	@Column(name = "[date]")
	private Date date;

	@ManyToOne
	@JoinColumn(name = "state_id", referencedColumnName = "state_id")
	private State state;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<OrderDetails> orderDetails = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<OrderRefundRecord> orderRefundRecord = new HashSet<>();

}
