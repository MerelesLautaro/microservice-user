package com.lautadev.microservice_user.service;

import com.lautadev.microservice_user.Throwable.UserValidator;
import com.lautadev.microservice_user.dto.BenefitDTO;
import com.lautadev.microservice_user.dto.UserDTO;
import com.lautadev.microservice_user.model.User;
import com.lautadev.microservice_user.repository.IBenefitAPIClient;
import com.lautadev.microservice_user.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepo;

    @Autowired
    private IBenefitAPIClient benefitClient;

    @Autowired
    private UserValidator validator;

    @Override
    public void saveUser(User user) {
        validator.validate(user);
        userRepo.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public User findUser(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    public void editUser(User user) {
        validator.validate(user);
        this.saveUser(user);
    }

    @Override
    public UserDTO findUserAndBenefit(Long id) {
        User user = this.findUser(id);
        BenefitDTO benefitDTO = benefitClient.findBenefit(user.getIdBenefit());
        return new UserDTO(user,benefitDTO);
    }
}
