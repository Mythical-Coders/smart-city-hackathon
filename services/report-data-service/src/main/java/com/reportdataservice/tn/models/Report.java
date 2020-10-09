package com.reportdataservice.tn.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
	@Id
	private String id;

	@NotBlank
	private String type;
	@NotBlank
	private String idImage;
	@NotBlank
	private String idPlace;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIdImage() {
		return idImage;
	}

	public void setIdImage(String idImage) {
		this.idImage = idImage;
	}

	public String getIdPlace() {
		return idPlace;
	}

	public void setIdPlace(String idPlace) {
		this.idPlace = idPlace;
	}

}
