package com.familytree.api.repository;

import com.familytree.api.entity.FamilyTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyTreeRepository extends JpaRepository<FamilyTree, Long> {
}
