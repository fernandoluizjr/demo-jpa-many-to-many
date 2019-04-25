package demo.repository;

import org.springframework.data.repository.CrudRepository;

import demo.domain.B;

public interface Bs extends CrudRepository<B, Long> {

}
