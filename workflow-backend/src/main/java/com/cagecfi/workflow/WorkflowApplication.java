/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow;

import com.cagecfi.workflow.utilisateurs.entity.Utilisateur;
import com.cagecfi.workflow.utilisateurs.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author USER
 */
@SpringBootApplication
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
//@EnableSwagger2
public class WorkflowApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(WorkflowApplication.class, args);
    }

//    @Bean
//    CommandLineRunner start(UtilisateurRepository utilisateurRepository) {
//        return args -> {
//            if (utilisateurRepository.findAll().isEmpty()) {
//                utilisateurRepository.save(new Utilisateur(1l, "user", "user", "user1", "user1", "1234"));
//            }
//
//        };
//    }

//    @Bean
//    CommandLineRunner exposeIdsVision(RepositoryRestConfiguration restConfiguration) {
//        return args -> {
//
//        };
//    }

//    @Bean
//    public Docket swaggerConfiguration(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                            .select()
//                            .paths(PathSelectors.any())
//                            .apis(RequestHandlerSelectors.basePackage("io.javabrains"))
//                            .build();
//    }

}
