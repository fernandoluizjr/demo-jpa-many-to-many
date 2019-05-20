package demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.domain.A;

public interface As extends JpaRepository<A, Long> {

}
