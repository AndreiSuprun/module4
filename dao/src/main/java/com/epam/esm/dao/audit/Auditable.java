package com.epam.esm.dao.audit;

public interface Auditable {
    Audit getAudit();
    void setAudit(Audit audit);
}
