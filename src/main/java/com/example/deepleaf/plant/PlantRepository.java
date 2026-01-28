package com.example.deepleaf.plant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.deepleaf.plant.domain.Plant;

public interface PlantRepository extends JpaRepository<Plant, Long> {

    Page<Plant> findByCommonNameContainingIgnoreCase(String commonName, Pageable pageable);

    Page<Plant> findByGenusContainingIgnoreCase(String genus, Pageable pageable);

    Page<Plant> findByFamilyContainingIgnoreCase(String family, Pageable pageable);

    @Query("""
        select p
        from Plant p
        where (:genus is null or lower(p.genus) like lower(concat('%', :genus, '%')))
          and (:family is null or lower(p.family) like lower(concat('%', :family, '%')))
        """)
    Page<Plant> searchByGenusAndFamily(
        @Param("genus") String genus,
        @Param("family") String family,
        Pageable pageable
    );
}
