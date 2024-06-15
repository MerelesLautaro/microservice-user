package com.lautadev.microservice_user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    private String cel;
    @NotBlank
    private String cuit;
    @NotBlank
    private String dni;
    @Temporal(TemporalType.DATE)
    @NotNull
    @Past // se asegura que la fecha sea en "pasado"
    private LocalDate dateOfBirth;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String province;
    @NotBlank
    private String departament;
    @NotBlank
    private String city;
    @NotBlank
    private String address;
    @NotBlank
    private String zipCode;
    private Long idBenefit;
    private int tickets;
}
