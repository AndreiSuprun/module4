package com.epam.esm.dao.audit;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AuditListener {

    private final String USER = "User";

    @PrePersist
    public void setCreatedOn(Auditable auditable) {
        Audit audit = auditable.getAudit();
        if(audit == null) {
            audit = new Audit();
            auditable.setAudit(audit);
        }
        audit.setCreatedOn(LocalDateTime.now());
        audit.setCreatedBy(USER);
    }

    @PreUpdate
    public void setUpdatedOn(Auditable auditable) {
        Audit audit = auditable.getAudit();
        if(audit == null) {
            audit = new Audit();
            auditable.setAudit(audit);
        }
        audit.setUpdatedOn(LocalDateTime.now());
        audit.setUpdatedBy(USER);
    }
}
