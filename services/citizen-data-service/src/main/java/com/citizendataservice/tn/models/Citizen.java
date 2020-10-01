package com.citizendataservice.tn.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "citizens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Citizen {
	@Id
	private String id;
	
	@NotBlank
	private String matricule;
	
	@NotBlank
	@Size(max = 8,min = 8)
	private int telephone;
	
	@NotBlank
	@Size(max = 8,min = 8)
	private int cin;


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

	public int getCin() {
		return cin;
	}

	public void setCin(int cin) {
		this.cin = cin;
	}


}