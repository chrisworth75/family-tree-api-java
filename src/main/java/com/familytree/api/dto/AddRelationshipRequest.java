package com.familytree.api.dto;

public class AddRelationshipRequest {
    private Long relatedMemberId;

    public Long getRelatedMemberId() {
        return relatedMemberId;
    }

    public void setRelatedMemberId(Long relatedMemberId) {
        this.relatedMemberId = relatedMemberId;
    }
}
