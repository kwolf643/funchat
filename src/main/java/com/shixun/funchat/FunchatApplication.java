package com.shixun.funchat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.shixun.funchat.dao")
public class FunchatApplication {

    public static void main(String[] args) {
        SpringApplication.run(FunchatApplication.class, args);
    }

}
