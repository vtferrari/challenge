package br.com.vtferrari.challenge.repository;

import br.com.vtferrari.challenge.repository.model.LoanDatabase;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface LoanRepository extends ReactiveMongoRepository<LoanDatabase,String> {

}
