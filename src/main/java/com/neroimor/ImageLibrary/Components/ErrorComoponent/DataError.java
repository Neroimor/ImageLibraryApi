package com.neroimor.ImageLibrary.Components.ErrorComoponent;

import com.neroimor.ImageLibrary.Components.Properties.AppSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class DataError {
    private final AppSettings appSettings;

    @Autowired
    public DataError(AppSettings appSettings) {
        this.appSettings = appSettings;
    }

    public ResponseEntity<String> dataAccessError(DataAccessException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(appSettings.getErrorResponseServer().getConnectedDB() + "\n" + e.getMessage());
    }
}
