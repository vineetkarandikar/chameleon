package com.digital.chameleon.security.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "otp_request")
@NamedQuery(name = "OtpRequest.findAll", query = "SELECT a FROM OtpRequest a")
public class OtpRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "mobile")
	private String mobile; // Destination

	@Column(name = "email_id")
	private String emailId; // Destination

	private int otp;

	@Column(name = "expiry_time")
	private long expiryTime;

}
