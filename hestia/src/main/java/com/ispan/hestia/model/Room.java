package com.ispan.hestia.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "room")
public class Room implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_id")
	private Integer roomId;

	@Column(name = "room_name")
	private String roomName;

	@ManyToOne
	@JoinColumn(name = "room_type_id", referencedColumnName = "room_type_id")
	private RoomType roomType;

	@ManyToOne
	@JoinColumn(name = "city_id", referencedColumnName = "city_id")
	private City city;

	@Column(name = "room_addr")
	private String roomAddr;

	@Column(name = "room_size")
	private Integer roomSize;

	@Column(name = "room_content")
	private String roomContent;

	@Column(name = "room_notice")
	private String roomNotice;

	@ManyToOne
	@JoinColumn(name = "provider_id", referencedColumnName = "provider_id")
	private Provider provider;

	@ManyToOne
	@JoinColumn(name = "state_id", referencedColumnName = "state_id")
	private State state;

	@Column(name = "double_bed")
	private Integer doubleBed;

	@Column(name = "single_bed")
	private Integer singleBed;

	@Column(name = "bedroom_count")
	private Integer bedroomCount;

	@Column(name = "bathroom")
	private Integer bathroom;

	@ManyToOne
	@JoinColumn(name = "refund_policy_id", referencedColumnName = "refund_policy_id")
	private RefundPolicy refundPolicy;

	@Column(name = "checkin_time")
	private Double checkinTime;

	@Column(name = "checkout_time")
	private Double checkoutTime;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
	private Set<Favorite> favorite = new HashSet<>();

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "room")
	private Set<RoomAvailableDate> roomAvailableDate = new HashSet<>();

}
