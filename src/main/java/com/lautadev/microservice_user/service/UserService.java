package com.lautadev.microservice_user.service;

import com.lautadev.microservice_user.Throwable.UserValidator;
import com.lautadev.microservice_user.dto.BenefitDTO;
import com.lautadev.microservice_user.dto.UserDTO;
import com.lautadev.microservice_user.model.User;
import com.lautadev.microservice_user.repository.IBenefitAPIClient;
import com.lautadev.microservice_user.repository.IUserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @CircuitBreaker(name="microservice-benefit",fallbackMethod = "fallBackForInfoUserBenefit")
    @Retry(name="microservice-benefit")
    public UserDTO findUserAndBenefit(Long id) {
        User user = this.findUser(id);
        BenefitDTO benefitDTO = benefitClient.findBenefit(user.getIdBenefit());
        return new UserDTO(user,benefitDTO);
    }

    public UserDTO fallbackFindSales(Throwable throwable) {
        return new UserDTO();
    }

}
