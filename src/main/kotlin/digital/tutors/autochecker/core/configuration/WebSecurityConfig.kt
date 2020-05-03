package digital.tutors.autochecker.core.configuration

import digital.tutors.autochecker.core.services.impl.UserDetailsServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig(
        val userDetailsService: UserDetailsServiceImpl,
        val bCryptPasswordEncoder: BCryptPasswordEncoder
) : WebSecurityConfigurerAdapter() {

    @Bean(name = [BeanIds.AUTHENTICATION_MANAGER])
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity?) {
        http!!.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,
                        "/sign-up",
                        "/login"
                ).permitAll()
                .antMatchers("/swagger-ui.html/**", "/webjars/**", "/v2/**", "/swagger-resources/**").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/**"
                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(
                        JWTAuthenticationFilter(
                                authenticationManagerBean()
                        ),
                        UsernamePasswordAuthenticationFilter::class.java
                )
                .addFilter(JWTAuthorizationFilter(authenticationManagerBean()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Throws(Exception::class)
    public override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder)
    }

}
