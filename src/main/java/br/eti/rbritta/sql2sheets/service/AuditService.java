package br.eti.rbritta.sql2sheets.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuditService {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);

    public void log(String action, String resource, Object id) {
        logger.info("{} performed {} on {}[{}]", userService.getCurrentUsername(), action, resource, id);
    }
}
