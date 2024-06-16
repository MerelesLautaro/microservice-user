package com.lautadev.microservice_user.service;

import com.lautadev.microservice_user.Throwable.UserException;
import com.lautadev.microservice_user.Throwable.UserValidator;
import com.lautadev.microservice_user.dto.BenefitDTO;
import com.lautadev.microservice_user.dto.UserDTO;
import com.lautadev.microservice_user.model.User;
import com.lautadev.microservice_user.repository.IBenefitAPIClient;
import com.lautadev.microservice_user.repository.IUserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepo;
    private final IBenefitAPIClient benefitClient;
    private final UserValidator validator;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(IUserRepository userRepo, IBenefitAPIClient benefitClient, UserValidator validator) {
        this.userRepo = userRepo;
        this.benefitClient = benefitClient;
        this.validator = validator;
    }

    @Override
    @Transactional
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
    @Transactional
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void editUser(Long id,User user) {
        validator.validate(user);
        User userEdit = this.findUser(id);
        BeanUtils.copyProperties(user, userEdit, "idUser");
        this.saveUser(userEdit);
    }

    @Override
    @CircuitBreaker(name="microservice-benefit",fallbackMethod = "fallBackForInfoUserBenefit")
    @Retry(name="microservice-benefit")
    public UserDTO findUserAndBenefit(Long id) {
        User user = this.findUser(id);
        BenefitDTO benefitDTO = benefitClient.findBenefit(user.getIdBenefit());
        return new UserDTO(user,benefitDTO);
    }

    public UserDTO fallBackForInfoUserBenefit(Throwable throwable) {
        return new UserDTO();
    }

    @Override
    @Transactional
    public void updateTickets(Long id, int ticket,String methodOverride) {
        User user = this.findUser(id);
        if(validator.isEligibleForBenefit(user)) {
            int currentTickets = user.getTickets();
            currentTickets -= ticket;
            user.setTickets(currentTickets);
            this.saveUser(user);
        }
    }

    @Override
    @CircuitBreaker(name="microservice-benefit",fallbackMethod = "fallBackForInfoUserBenefit")
    @Retry(name="microservice-benefit")
    @Transactional
    public void assignBenefit(Long idUser, Long idBenefit,String methodOverride) {
        User user = this.findUser(idUser);
        if(validator.hasBenefit(user)) {
            user.setIdBenefit(idBenefit);
            BenefitDTO benefitDTO = benefitClient.findBenefit(idBenefit);
            user.setTickets(benefitDTO.getTickets());
            this.saveUser(user);
        } else {
            throw new UserException("User already has a benefit.");
        }
    }

    public void fallBackForInfoUserBenefit(Long idUser, Long idBenefit, String methodOverride, Throwable throwable) {
        logger.error("Error occurred in assignBenefit with idUser: {}, idBenefit: {}, methodOverride: {}", idUser, idBenefit, methodOverride, throwable);
    }

}
