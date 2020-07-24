package com.sadik.web.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.sadik.web.model.user.User;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = { "com.sadik.web" })
@PropertySource("classpath:database.properties")
@EnableTransactionManagement
@Import({ SecurityConfig.class })
public class AppConfig {

	@Autowired
	private Environment env;

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/pages/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Bean(name = "dataSource")
	public DriverManagerDataSource getDataSource() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName(env.getProperty("db.driver"));
		driverManagerDataSource.setUrl(env.getProperty("db.url"));
		driverManagerDataSource.setUsername(env.getProperty("db.username"));
		driverManagerDataSource.setPassword(env.getProperty("db.password"));
		return driverManagerDataSource;
	}

	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean getSessionFactory() {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(getDataSource());
		sessionFactoryBean.setPackagesToScan("com.sadik.web");
		
		Properties hibernateProps = new Properties();
		hibernateProps.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
		hibernateProps.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		hibernateProps.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
		hibernateProps.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));

		sessionFactoryBean.setHibernateProperties(hibernateProps);
		sessionFactoryBean.setAnnotatedClasses(User.class);
		return sessionFactoryBean;
	}

	@Bean(name = "transactionManager")
	public HibernateTransactionManager getTransactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(getSessionFactory().getObject());
		return transactionManager;
	}
}
