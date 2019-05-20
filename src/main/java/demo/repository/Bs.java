package demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.domain.B;

public interface Bs extends JpaRepository<B, Long> {

}
