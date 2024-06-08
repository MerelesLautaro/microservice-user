package com.lautadev.microservice_user.dto;

import com.lautadev.microservice_user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long idUser;
    private String name;
    private String surname;
    private String dni;
    private BenefitDTO benefit;

    public UserDTO(User user, BenefitDTO benefitDTO) {
        this.idUser = user.getIdUser();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.dni = user.getDni();
        this.benefit = benefitDTO;
    }
}
