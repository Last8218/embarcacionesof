package com.last.embarcaciones;


import org.springframework.beans.factory.annotation.Autowired;


import javax.sql.DataSource;

public class DatabaseConnectionIntegrationTest {

    @Autowired
    private DataSource dataSource;


}
