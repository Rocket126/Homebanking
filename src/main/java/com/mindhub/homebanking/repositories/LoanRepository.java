package com.mindhub.homebanking.repositories;
import com.mindhub.homebanking.models.Loan;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

//aqui es donde va el crud o se le conoce como crudRepository, aqui es donde se genera Crear, Leer, Actualizar, Borrar
//que administra instacias de una clase "prestamos"
@RepositoryRestResource
public interface LoanRepository extends JpaRepository<Loan, Long> {

  Loan findById(long id);

}
