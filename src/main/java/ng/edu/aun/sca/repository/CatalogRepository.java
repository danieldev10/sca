package ng.edu.aun.sca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ng.edu.aun.sca.model.Catalog;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long> {

}
