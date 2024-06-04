package ng.edu.aun.sca.service;

import java.util.List;
import java.util.Optional;

import ng.edu.aun.sca.model.Catalog;

public interface CatalogService {
    Catalog createCatalog(Catalog catalog);

    List<Catalog> findAll();

    Optional<Catalog> findById(Long id);
}
