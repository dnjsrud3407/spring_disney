package com.tistory.dnjsrud.disney;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.thymeleaf.spring5.view.ThymeleafView;

import javax.persistence.EntityManager;

@EnableJpaAuditing
@SpringBootApplication
public class DisneyApplication {

	public static void main(String[] args) {
		SpringApplication.run(DisneyApplication.class, args);
	}

	@Bean
	JPAQueryFactory jpaQueryFactory(EntityManager em) {return new JPAQueryFactory(em);}

	@Bean
	public ThymeleafView thymeleafView() {
		ThymeleafView thymeleafView = new ThymeleafView();
		thymeleafView.setCharacterEncoding("UTF-8");
		return thymeleafView;
	}
}
