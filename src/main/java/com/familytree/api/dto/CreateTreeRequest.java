package com.familytree.api.dto;

import com.familytree.api.entity.Member;

public class CreateTreeRequest {
    private String treeName;
    private String description;
    private RootPerson rootPerson;

    public static class RootPerson {
        private String firstName;
        private String lastName;
        private Member.Gender gender;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Member.Gender getGender() {
            return gender;
        }

        public void setGender(Member.Gender gender) {
            this.gender = gender;
        }
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RootPerson getRootPerson() {
        return rootPerson;
    }

    public void setRootPerson(RootPerson rootPerson) {
        this.rootPerson = rootPerson;
    }
}
