package com.impound.tn.models;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "impounds")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Impound {
	@Id
	private String id;
	@NotBlank
	@Size(max = 20)
	private String matricule;

	@Size(max = 8)
	private int telephone;

	@Size(max = 8)
	private String cin;
	@NotBlank
	private String idDriver;
	@NotBlank
	private Date impoundDate;

	private Date releaseDate;

	private Date paidDate;
	private Boolean paid = false;
	private Boolean released = false;
	// where gonna put cars
	private String idPlace;
	// from where cars taken
	private String idPlaceTake;
	private String idImageFront;
	private String idImageBack;

	private Date createDate = new Date();

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	public int getTelephone() {
		return telephone;
	}

	public void setTelephone(int telephone) {
		this.telephone = telephone;
	}

	public String getCin() {
		return cin;
	}

	public void setCin(String cin) {
		this.cin = cin;
	}

	public String getIdDriver() {
		return idDriver;
	}

	public void setIdDriver(String idDriver) {
		this.idDriver = idDriver;
	}

	public Date getImpoundDate() {
		return impoundDate;
	}

	public void setImpoundDate(Date impoundDate) {
		this.impoundDate = impoundDate;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Boolean getPaid() {
		return paid;
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	public Boolean getReleased() {
		return released;
	}

	public void setReleased(Boolean released) {
		this.released = released;
	}

	public String getIdPlace() {
		return idPlace;
	}

	public void setIdPlace(String idPlace) {
		this.idPlace = idPlace;
	}

	public Date getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}

	public String getIdPlaceTake() {
		return idPlaceTake;
	}

	public void setIdPlaceTake(String idPlaceTake) {
		this.idPlaceTake = idPlaceTake;
	}

	public String getIdImageFront() {
		return idImageFront;
	}

	public void setIdImageFront(String idImageFront) {
		this.idImageFront = idImageFront;
	}

	public String getIdImageBack() {
		return idImageBack;
	}

	public void setIdImageBack(String idImageBack) {
		this.idImageBack = idImageBack;
	}

}
