package com.ispan.hestia.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="comment")
public class Comment implements Serializable{
	
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="comment_id")
	private Integer commentId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_room_id")
	private OrderDetails orderdetails;
	
	@Column(name="comment_date")
	private Date commentDate=new Date();
	
	@Column(name="cleaness_score", nullable=true)
	private Integer cleanessScore;
	
	@Column(name="comfort_score", nullable=true)
	private Integer comfortScore;
	
	@Column(name="location_score", nullable=true)
	private Integer locationScore;
	
	@Column(name="facility_score", nullable=true)
	private Integer facilityScore;
	
	@Column(name="pationess_score", nullable=true)
	private Integer pationessScore;
	
	@Column(name="score")
	private Integer score;
	
	@Column(name="comment_content")
	private String commentContent;
	
	
}
