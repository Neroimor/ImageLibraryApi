package com.neroimor.ImageLibrary.Services.Admin;

import com.neroimor.ImageLibrary.Components.ErrorComoponent.DataError;
import com.neroimor.ImageLibrary.Components.Properties.AdminSettings;
import com.neroimor.ImageLibrary.Models.PageData.PagesDataInfo;
import com.neroimor.ImageLibrary.Models.UsersModels.User;
import com.neroimor.ImageLibrary.Models.UsersModels.UserData;
import com.neroimor.ImageLibrary.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
public class GetUserService {

    private final UserRepository userRepository;
    private final DataError dataError;
    private final AdminSettings adminSettings;

    @Autowired
    public GetUserService(UserRepository userRepository, DataError dataError, AdminSettings adminSettings) {
        this.userRepository = userRepository;
        this.dataError = dataError;
        this.adminSettings = adminSettings;
    }

    public ResponseEntity<String> getUser(String email) {
        try {
            Optional<User> user = userRepository.findByEmail(email);
            return user.map(value
                    -> ResponseEntity.status(HttpStatus.OK).body(DataTransformation(value).toString())).orElseGet(()
                    -> ResponseEntity.status(HttpStatus.OK).body(adminSettings.getSettingOperation().getUserNotFound()));

        } catch (DataAccessException e) {
            return dataError.dataAccessError(e);
        }
    }

    private UserData DataTransformation(User user) {
        log.info("Начата трансформация данных");
        var userData = new UserData();
        userData.setEmail(user.getEmail());
        userData.setNickname(user.getNickname());
        return userData;
    }

    public List<UserData> getAllUsers(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> userPage = userRepository.findAll(pageable);
            List<User> users = userRepository.findAll();
            log.info("Все пользователи найдены");
            return users.stream()
                    .sorted((u1,u2)->
                            u2.getCreated_at().compareTo(u1.getCreated_at()))
                    .map(s -> {
                        var user = new UserData();
                        user.setEmail(s.getEmail());
                        user.setNickname(s.getNickname());
                        return user;
                    })
                    .toList();
        } catch (DataAccessException e) {
            return null;
        }
    }

    public PagesDataInfo getPagesDataInfo(int page, int size) {
        log.info("Начато полученния данных из базы данных");
        long totalElements = userRepository.count();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        PagesDataInfo pagesDataInfo = new PagesDataInfo();
        pagesDataInfo.setTotalElements(totalElements);
        pagesDataInfo.setTotalPages(totalPages);
        pagesDataInfo.setCurrentPage(page);
        pagesDataInfo.setPageSize(size);
        log.info("Расчет окончен");
        return pagesDataInfo;
    }
}
