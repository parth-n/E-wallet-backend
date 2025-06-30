package com.example.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl() ;
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setUsername("parth.nepalia@gmail.com");
        javaMailSender.setPort(587);
        javaMailSender.setPassword("nqgaxhndrlitvbjp");
        Properties properties = javaMailSender.getJavaMailProperties() ;
        properties.put("mail.smtp.starttls.enable",true) ;
        properties.put("mail.debug",true) ;

        return javaMailSender ;
    }

}


