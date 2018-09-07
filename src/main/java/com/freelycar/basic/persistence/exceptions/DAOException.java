package com.freelycar.basic.persistence.exceptions;

import org.springframework.dao.DataAccessException;

public class DAOException extends DataAccessException {
    private static final long serialVersionUID = 1L;

    public DAOException(String s) {
        super(s);
    }

    public DAOException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
