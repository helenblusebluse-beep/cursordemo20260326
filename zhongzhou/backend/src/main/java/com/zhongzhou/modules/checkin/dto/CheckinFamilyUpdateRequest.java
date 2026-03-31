package com.zhongzhou.modules.checkin.dto;

import java.util.List;

public class CheckinFamilyUpdateRequest {
    private List<FamilyMemberDTO> familyMembers;

    public List<FamilyMemberDTO> getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(List<FamilyMemberDTO> familyMembers) {
        this.familyMembers = familyMembers;
    }

    public static class FamilyMemberDTO {
        private String familyName;
        private String familyContact;
        private String relationType;

        public String getFamilyName() {
            return familyName;
        }

        public void setFamilyName(String familyName) {
            this.familyName = familyName;
        }

        public String getFamilyContact() {
            return familyContact;
        }

        public void setFamilyContact(String familyContact) {
            this.familyContact = familyContact;
        }

        public String getRelationType() {
            return relationType;
        }

        public void setRelationType(String relationType) {
            this.relationType = relationType;
        }
    }
}
