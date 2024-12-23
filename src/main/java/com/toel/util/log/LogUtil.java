package com.toel.util.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.model.Account;
import com.toel.model.Log;
import com.toel.repository.AccountRepository;

import org.apache.logging.log4j.LogManager;

@Service
public class LogUtil {
    @Autowired
    AccountRepository accountRepository;

    public void setLog(Class<?> logClass, Integer accountID, String level, String tableName, Object dataOld,
            Object dataNew,
            String action_type) {
        // Logger logger = LogManager.getLogger(logClass);
        Log log = new Log();
        if (accountID != null) {
            Account account = accountRepository.findById(accountID)
                    .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Account"));
            log.setAccount(account);
        } else {
            log.setAccount(null);
        }
        log.setLevel(level);
        log.setAction_type(action_type);
        log.setTableName(tableName);
        String jsonDataOld = convertToJson(dataOld);

        log.setDataOld(jsonDataOld);

        String jsonDataNew = convertToJson(dataNew);

        log.setDataNew(jsonDataNew);
        com.toel.util.log.LogManager logManager = new com.toel.util.log.LogManager();
        logManager.listen();
        logManager.submit(log);
        // long start = new Date().getTime();

        // logger.info("Async: " + (new Date().getTime() - start) + "ms");
        // start = new Date().getTime();
        // Ghi log không dùng thread
        // save(log,logClass);
        // logger.info("No-async: " + (new Date().getTime() - start) + "ms");
    }

    private String convertToJson(Object data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    private void save(Log log, Class<?> logClass) {
        Logger logger = LogManager.getLogger(logClass);
        PreparedStatement stmt = null;
        Connection con = null;
        try {
            con = DatabaseUtils.getConnection();
            stmt = con.prepareStatement(
                    " INSERT INTO logs (timestamps, level, tableName, dataNew, dataOld, action_type, account_id) " +
                            " VALUES (CURRENT_TIMESTAMP, ?,?,?,?,?,?)");
            stmt.setString(1, log.getLevel());
            stmt.setString(2, log.getTableName());
            stmt.setString(3, log.getDataNew());
            stmt.setString(4, log.getDataOld());
            stmt.setString(5, log.getAction_type());
            stmt.setInt(6, log.getAccount().getId());

            stmt.execute();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            DatabaseUtils.closeObject(stmt);
            DatabaseUtils.closeObject(con);
        }
    }
}
