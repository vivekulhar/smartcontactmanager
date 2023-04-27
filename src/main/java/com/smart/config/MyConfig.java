package com.smart.config;

//import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.ldap.EmbeddedLdapServerContextSourceFactoryBean;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;


	@Configuration
	public class MyConfig {
		
		@Bean
		public UserDetailsService getUserDetailService()
		{
			return new UserDetailsServiceImpl();
		}
		@Bean
		public BCryptPasswordEncoder passwordEncoder()
		{
			return new BCryptPasswordEncoder();
		}
		
		@Bean
		public DaoAuthenticationProvider authenticationProvider()
		{
			DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
			daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
			daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
			
			return daoAuthenticationProvider;
			
			
		}
		
		//Configure method...
		
//		  @Bean
//		  protected void configure(AuthenticationManagerBuilder auth) throws
//		  Exception {
//		  
//			  auth.authenticationProvider(authenticationProvider());
//		  
//		  }
		
	    @Bean
	    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
	           AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);

	           auth.authenticationProvider(authenticationProvider());

	           return auth.build();
	       }
		  
//		  @Override protected void configure(HttpSecurity http) throws Exception {
//		  http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
//		  .antMatchers("/user/").hasRole("USER")
//		  .antMatchers("/**").permitAll().and().formLogin().and() .csrf() .disable(); *
//		  }
		 
		
		
		@Bean
	    public DefaultSecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			http.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN")
	        .requestMatchers("/user/").hasRole("USER")
	        .requestMatchers("/**").permitAll()
	        .and().formLogin().and().csrf().disable();
	        return http.build();
	    }
		
		
		
				
		
		
		
	}
