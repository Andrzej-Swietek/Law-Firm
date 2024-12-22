DROP SCHEMA IF EXISTS LawFirm CASCADE;
CREATE SCHEMA IF NOT EXISTS LawFirm;

CREATE TABLE LawFirm.contact_data
(
    id           SERIAL  NOT NULL PRIMARY KEY,
    phone_number TEXT NOT NULL,
    email        TEXT NOT NULL,
    street       TEXT NOT NULL,
    city         TEXT NOT NULL,
    state        TEXT NOT NULL,
    zip_code     TEXT NOT NULL,
    country      TEXT NOT NULL
);

CREATE TABLE LawFirm.client
(
    id              SERIAL  NOT NULL PRIMARY KEY,
    first_name      TEXT NOT NULL,
    last_name       TEXT NOT NULL,
    email           TEXT NOT NULL,
    contact_data_id INT  NOT NULL,
    CONSTRAINT client_contact_data_fk FOREIGN KEY (contact_data_id) REFERENCES LawFirm.contact_data (id)
);

CREATE TABLE LawFirm.lawyer
(
    id             SERIAL  NOT NULL PRIMARY KEY,
    first_name     TEXT NOT NULL,
    last_name      TEXT NOT NULL,
    specialization TEXT NOT NULL
);

CREATE TABLE LawFirm.court_division
(
    id   SERIAL  NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    city TEXT NOT NULL
);

CREATE TABLE LawFirm.judge
(
    id                SERIAL  NOT NULL PRIMARY KEY,
    first_name        TEXT NOT NULL,
    last_name         TEXT NOT NULL,
    court_division_id INT  NOT NULL,
    CONSTRAINT judge_court_division_fk FOREIGN KEY (court_division_id) REFERENCES LawFirm.court_division (id)
);

CREATE TABLE LawFirm.trial_status
(
    id   SERIAL  NOT NULL PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE LawFirm.document_types
(
    id   SERIAL  NOT NULL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE LawFirm.document
(
    id        SERIAL  NOT NULL PRIMARY KEY,
    type_id   INT  NOT NULL,
    file_path TEXT NOT NULL,
    CONSTRAINT document_type_fk FOREIGN KEY (type_id) REFERENCES LawFirm.document_types (id)
);

CREATE TABLE LawFirm.case
(
    id                    SERIAL  NOT NULL PRIMARY KEY,
    name                  TEXT NOT NULL,
    description           TEXT NOT NULL,
    responsible_lawyer_id INT  NOT NULL,
    client_id             INT  NOT NULL,
    CONSTRAINT case_lawyer_fk FOREIGN KEY (responsible_lawyer_id) REFERENCES LawFirm.lawyer (id),
    CONSTRAINT case_client_fk FOREIGN KEY (client_id) REFERENCES LawFirm.client (id)
);

CREATE TABLE LawFirm.trial
(
    id              SERIAL  NOT NULL PRIMARY KEY,
    title           TEXT NOT NULL,
    description     TEXT NOT NULL,
    trial_status_id INT  NOT NULL,
    client_id       INT  NOT NULL,
    lawyer_id       INT  NOT NULL,
    judge_id        INT  NOT NULL,
    date            DATE NOT NULL,
    case_id         INT  NOT NULL,
    CONSTRAINT trial_client_fk FOREIGN KEY (client_id) REFERENCES LawFirm.client (id),
    CONSTRAINT trial_lawyer_fk FOREIGN KEY (lawyer_id) REFERENCES LawFirm.lawyer (id),
    CONSTRAINT trial_judge_fk FOREIGN KEY (judge_id) REFERENCES LawFirm.judge (id),
    CONSTRAINT trial_case_fk FOREIGN KEY (case_id) REFERENCES LawFirm.case (id),
    CONSTRAINT trial_status_fk FOREIGN KEY (trial_status_id) REFERENCES LawFirm.trial_status (id)
);

CREATE TABLE LawFirm.ruling
(
    id                SERIAL     NOT NULL PRIMARY KEY,
    is_final          BOOLEAN NOT NULL,
    content           TEXT    NOT NULL,
    trial_id          INT     NOT NULL,
    finalization_date DATE    NOT NULL,
    CONSTRAINT ruling_trial_fk FOREIGN KEY (trial_id) REFERENCES LawFirm.trial (id)
);

CREATE TABLE LawFirm.appeal
(
    id                SERIAL NOT NULL PRIMARY KEY,
    initial_ruling_id INT NOT NULL,
    final_ruling_id   INT NOT NULL,
    trial_id          INT NOT NULL,
    CONSTRAINT appeal_initial_ruling_fk FOREIGN KEY (initial_ruling_id) REFERENCES LawFirm.ruling (id),
    CONSTRAINT appeal_final_ruling_fk FOREIGN KEY (final_ruling_id) REFERENCES LawFirm.ruling (id),
    CONSTRAINT appeal_trial_fk FOREIGN KEY (trial_id) REFERENCES LawFirm.trial (id)
);

CREATE TABLE LawFirm.required_documents_for_trial
(
    id          SERIAL NOT NULL PRIMARY KEY,
    trial_id    INT NOT NULL,
    document_id INT NOT NULL,
    CONSTRAINT required_documents_trial_fk FOREIGN KEY (trial_id) REFERENCES LawFirm.trial (id),
    CONSTRAINT required_documents_document_fk FOREIGN KEY (document_id) REFERENCES LawFirm.document (id)
);

CREATE TABLE LawFirm.signature
(
    id                   SERIAL  NOT NULL PRIMARY KEY,
    person_id            INT  NOT NULL,
    role                 TEXT NOT NULL CHECK (role IN ('client', 'lawyer', 'judge')),
    required_document_id INT  NOT NULL,
    CONSTRAINT signature_required_document_fk FOREIGN KEY (required_document_id) REFERENCES LawFirm.required_documents_for_trial(id)
);

CREATE TABLE LawFirm.decision
(
    id          SERIAL  NOT NULL PRIMARY KEY,
    name        TEXT NOT NULL,
    description TEXT NOT NULL,
    date        DATE NOT NULL,
    case_id     INT  NOT NULL,
    CONSTRAINT decision_case_fk FOREIGN KEY (case_id) REFERENCES LawFirm.case (id)
);
