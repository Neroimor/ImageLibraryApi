package com.neroimor.ImageLibrary.Components.ErrorComoponent;

import com.neroimor.ImageLibrary.Components.Properties.AppSettings;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataError {
    private final AppSettings appSettings;

    @Autowired
    public DataError(AppSettings appSettings) {
        this.appSettings = appSettings;
    }

    public ResponseEntity<String> dataAccessError(DataAccessException e) {
        log.error("Произошла ошибка в базе данных ");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(appSettings.getErrorResponseServer().getConnectedDb() + "\n" + e.getMessage());
    }
}
