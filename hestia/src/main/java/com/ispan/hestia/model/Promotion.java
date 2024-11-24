package com.ispan.hestia.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name="promotion")
public class Promotion implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="promotion_id")
	private Integer promotionId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name="start_at")
	private Date startAt;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name="end_at")
	private Date endAt;
	
	@Column(name="name")
	private String name;
	
	@Column(name="discount")
	private Double discount;
	
	@Column(name="promotion_content")
	private String promotionContent;
	
	@Column(name="promotion_code")
	private String promotionCode;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "promotion")
	private Set<PromotionRooms> promotionRooms=new HashSet<>();
	
	

}
