package com.icsd19080_icsd19235;

import com.icsd19080_icsd19235.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() 
            .authorizeRequests()
                .antMatchers("/api/**").permitAll() 
                .anyRequest().permitAll() 
            .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
            .and()
                .logout()
                    .permitAll();
        
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userService;
    }
}

/*
 * @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // Disable CSRF for simplicity, enable it in production
            .authorizeRequests()
                // Visitor role permissions
                .antMatchers("/papers/search").permitAll()
                .antMatchers("/conferences/search").permitAll() 
                .antMatchers("/papers/{paperId}/view").hasAnyRole("VISITOR", "AUTHOR", "PC_CHAIR", "PC_MEMBER")
                .antMatchers("/conferences/{id}/view").hasAnyRole("VISITOR", "PC_CHAIR", "PC_MEMBER", "AUTHOR")

                // Author-related permissions
                .antMatchers("/papers/{conferenceId}").hasRole("AUTHOR") 
                .antMatchers("/papers/{id}").hasRole("AUTHOR")
                .antMatchers("/papers/{paperId}/add-coauthors").hasRole("AUTHOR")
                .antMatchers("/papers/{paperId}/submit").hasRole("AUTHOR") 
                .antMatchers("/papers/{paperId}/final-submit").hasRole("AUTHOR") 
                .antMatchers("/papers/{paperId}/withdraw").hasRole("AUTHOR")

                // PC Chair permissions (full conference management)
                .antMatchers("/conferences").hasRole("PC_CHAIR") 
                .antMatchers("/conferences/{id}").hasRole("PC_CHAIR") 
                .antMatchers("/conferences/{id}/add-pc-chairs").hasRole("PC_CHAIR") 
                .antMatchers("/conferences/{id}/add-pc-members").hasRole("PC_CHAIR")
                .antMatchers("/conferences/{id}/delete").hasRole("PC_CHAIR") 
                .antMatchers("/conferences/{id}/start-submission").hasRole("PC_CHAIR") 
                .antMatchers("/conferences/{id}/start-reviewer-assignment").hasAnyRole("PC_CHAIR", "PC_MEMBER") 
                .antMatchers("/conferences/{id}/start-review").hasAnyRole("PC_CHAIR", "PC_MEMBER") 
                .antMatchers("/conferences/{id}/start-decision-making").hasRole("PC_CHAIR") 
                .antMatchers("/conferences/{id}/start-final-submission").hasAnyRole("PC_CHAIR", "AUTHOR") 
                .antMatchers("/conferences/{id}/end-conference").hasRole("PC_CHAIR") 

                // PC Member permissions
                .antMatchers("/papers/{paperId}/submit-review").hasRole("PC_MEMBER") 
                .antMatchers("/papers/{paperId}/assign-reviewers").hasAnyRole("PC_CHAIR", "PC_MEMBER") 

                // Paper approval and rejection by PC Chair
                .antMatchers("/papers/{paperId}/approve").hasRole("PC_CHAIR") 
                .antMatchers("/papers/{paperId}/reject").hasRole("PC_CHAIR") 
                .antMatchers("/papers/{paperId}/accept").hasRole("PC_CHAIR") 

                // All other requests require authentication
                .anyRequest().authenticated();
        return http.build();
    }

 */