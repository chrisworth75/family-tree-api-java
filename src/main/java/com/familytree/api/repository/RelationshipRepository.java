package com.familytree.api.repository;

import com.familytree.api.entity.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    List<Relationship> findByMemberId(Long memberId);
}
