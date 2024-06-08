package com.lautadev.microservice_user.service;

import com.lautadev.microservice_user.dto.BenefitDTO;
import com.lautadev.microservice_user.dto.UserDTO;
import com.lautadev.microservice_user.model.User;

import java.util.List;

public interface IUserService {
    public void saveUser(User user);
    public List<User> getUsers();
    public User findUser(Long id);
    public void deleteUser(Long id);
    public void editUser(User user);
    public UserDTO findUserAndBenefit(Long id);
}
