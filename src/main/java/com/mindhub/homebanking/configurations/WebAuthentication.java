package com.mindhub.homebanking.configurations;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {
    @Autowired
    ClientRepository clientRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(inputName-> {
            Client client = clientRepository.findByEmail(inputName);//inputName tiene el email del usuario logueado
            if (client != null) {
                return new User(client.getEmail(), client.getPassword(),AuthorityUtils.createAuthorityList("CLIENT"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }

     //La anotación @Bean genera un objeto de tipo PasswordEncoder en el ApplicationContext para que luego se
            // pueda usar en cualquier parte de la aplicación que se requiera.
    //passordEncoder sirve para cifrar las claves de usuario
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}






//GlobalAuthenticationConfigurerAdapter que es el objeto que utiliza el Spring Security para saber cómo buscará los detalles del usuario
//Configuration le indica a spring que debe crear un objeto de este tipo cuando se está iniciando la aplicación
// para que cuando se configure el módulo de spring utilice ese objeto ya creado.