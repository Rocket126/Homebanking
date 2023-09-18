package com.mindhub.homebanking.repositories;
import com.mindhub.homebanking.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Set;

/*Esto representa un crud, estas administrarian las bases de datos*/

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {
   List<Account> findById(long id);

   //Account findById(long id);
   Account findByNumber(String number);



}