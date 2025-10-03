package com.familytree.api.service;

import com.familytree.api.entity.FamilyTree;
import com.familytree.api.entity.Member;
import com.familytree.api.entity.Relationship;
import com.familytree.api.repository.FamilyTreeRepository;
import com.familytree.api.repository.MemberRepository;
import com.familytree.api.repository.RelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FamilyTreeService {

    @Autowired
    private FamilyTreeRepository familyTreeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Transactional
    public FamilyTree createTree(FamilyTree tree, Member rootMember) {
        FamilyTree savedTree = familyTreeRepository.save(tree);
        rootMember.setFamilyTree(savedTree);
        memberRepository.save(rootMember);
        return savedTree;
    }

    public List<FamilyTree> getAllTrees() {
        return familyTreeRepository.findAll();
    }

    public Optional<FamilyTree> getTreeById(Long id) {
        return familyTreeRepository.findById(id);
    }

    @Transactional
    public void deleteTree(Long id) {
        familyTreeRepository.deleteById(id);
    }

    @Transactional
    public Member addMember(Long treeId, Member member) {
        FamilyTree tree = familyTreeRepository.findById(treeId)
                .orElseThrow(() -> new RuntimeException("Tree not found"));
        member.setFamilyTree(tree);
        return memberRepository.save(member);
    }

    public List<Member> getMembers(Long treeId) {
        return memberRepository.findByFamilyTreeId(treeId);
    }

    public Optional<Member> getMemberById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    @Transactional
    public Relationship addRelationship(Long memberId, Long relatedMemberId, Relationship.RelationshipType type) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Member relatedMember = memberRepository.findById(relatedMemberId)
                .orElseThrow(() -> new RuntimeException("Related member not found"));

        Relationship relationship = new Relationship();
        relationship.setMember(member);
        relationship.setRelatedMember(relatedMember);
        relationship.setRelationshipType(type);

        return relationshipRepository.save(relationship);
    }

    public List<Relationship> getRelationships(Long memberId) {
        return relationshipRepository.findByMemberId(memberId);
    }

    public Optional<FamilyTree> getTreeWithMembers(Long treeId) {
        Optional<FamilyTree> tree = familyTreeRepository.findById(treeId);
        if (tree.isPresent()) {
            // Fetch members to initialize the collection
            tree.get().getMembers().size();
        }
        return tree;
    }
}
