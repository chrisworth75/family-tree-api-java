package com.familytree.api.controller;

import com.familytree.api.dto.CreateTreeRequest;
import com.familytree.api.dto.AddMemberRequest;
import com.familytree.api.dto.AddRelationshipRequest;
import com.familytree.api.entity.FamilyTree;
import com.familytree.api.entity.Member;
import com.familytree.api.entity.Relationship;
import com.familytree.api.service.FamilyTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trees")
public class FamilyTreeController {

    @Autowired
    private FamilyTreeService familyTreeService;

    @PostMapping
    public ResponseEntity<?> createTree(@RequestBody CreateTreeRequest request) {
        try {
            FamilyTree tree = new FamilyTree();
            tree.setName(request.getTreeName());
            tree.setDescription(request.getDescription());

            Member rootMember = new Member();
            rootMember.setFirstName(request.getRootPerson().getFirstName());
            rootMember.setLastName(request.getRootPerson().getLastName());
            rootMember.setGender(request.getRootPerson().getGender());

            FamilyTree savedTree = familyTreeService.createTree(tree, rootMember);
            return ResponseEntity.ok(savedTree);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<FamilyTree>> getAllTrees() {
        return ResponseEntity.ok(familyTreeService.getAllTrees());
    }

    @GetMapping("/{treeId}")
    public ResponseEntity<?> getTree(@PathVariable Long treeId) {
        return familyTreeService.getTreeById(treeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{treeId}")
    public ResponseEntity<?> deleteTree(@PathVariable Long treeId) {
        familyTreeService.deleteTree(treeId);
        return ResponseEntity.ok(Map.of("message", "Tree deleted"));
    }

    @PostMapping("/{treeId}/members")
    public ResponseEntity<?> addMember(@PathVariable Long treeId, @RequestBody AddMemberRequest request) {
        try {
            Member member = new Member();
            member.setFirstName(request.getFirstName());
            member.setLastName(request.getLastName());
            member.setGender(request.getGender());
            member.setBirthDate(request.getBirthDate());

            Member savedMember = familyTreeService.addMember(treeId, member);
            return ResponseEntity.ok(savedMember);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{treeId}/members")
    public ResponseEntity<List<Member>> getMembers(@PathVariable Long treeId) {
        return ResponseEntity.ok(familyTreeService.getMembers(treeId));
    }

    @GetMapping("/{treeId}/members/{memberId}")
    public ResponseEntity<?> getMember(@PathVariable Long treeId, @PathVariable Long memberId) {
        return familyTreeService.getMemberById(memberId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{treeId}/members/{memberId}/partner")
    public ResponseEntity<?> addPartner(@PathVariable Long treeId,
                                       @PathVariable Long memberId,
                                       @RequestBody AddRelationshipRequest request) {
        try {
            Relationship relationship = familyTreeService.addRelationship(
                    memberId, request.getRelatedMemberId(), Relationship.RelationshipType.SPOUSE);
            return ResponseEntity.ok(relationship);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{treeId}/members/{memberId}/children")
    public ResponseEntity<?> addChild(@PathVariable Long treeId,
                                     @PathVariable Long memberId,
                                     @RequestBody AddRelationshipRequest request) {
        try {
            Relationship relationship = familyTreeService.addRelationship(
                    memberId, request.getRelatedMemberId(), Relationship.RelationshipType.CHILD);
            return ResponseEntity.ok(relationship);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{treeId}/members/{memberId}/relationships")
    public ResponseEntity<List<Relationship>> getRelationships(@PathVariable Long treeId,
                                                               @PathVariable Long memberId) {
        return ResponseEntity.ok(familyTreeService.getRelationships(memberId));
    }
}
