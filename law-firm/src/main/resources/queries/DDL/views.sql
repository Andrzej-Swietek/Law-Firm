
--  1. Widok Klientów z Danymi Kontaktowymi
--      Zintegrowane dane klienta z jego danymi kontaktowymi.

CREATE VIEW LawFirm.client_with_contact AS
SELECT
    c.id AS client_id,
    c.first_name,
    c.last_name,
    c.email AS client_email,
    cd.phone_number,
    cd.email AS contact_email,
    cd.street,
    cd.city,
    cd.state,
    cd.zip_code,
    cd.country
FROM LawFirm.client c
         JOIN LawFirm.contact_data cd ON c.contact_data_id = cd.id;



--  2. Widok Spraw z Odpowiedzialnym Prawnikiem i Klientem
--      Pozwala łatwo uzyskać szczegóły dotyczące sprawy, prawnika i klienta.

CREATE VIEW LawFirm.case_with_details AS
SELECT
    ca.id AS case_id,
    ca.name AS case_name,
    ca.description AS case_description,
    l.first_name AS lawyer_first_name,
    l.last_name AS lawyer_last_name,
    cl.first_name AS client_first_name,
    cl.last_name AS client_last_name
FROM LawFirm.case ca
         JOIN LawFirm.lawyer l ON ca.responsible_lawyer_id = l.id
         JOIN LawFirm.client cl ON ca.client_id = cl.id;



--  3. Widok Rozpraw z Szczegółami
--      Zbiera informacje o rozprawie, klientach, prawnikach, sędziach i statusie rozprawy.

CREATE VIEW LawFirm.trial_with_details AS
SELECT
    t.id AS trial_id,
    t.title,
    t.description,
    t.date AS trial_date,
    ts.name AS trial_status,
    cl.first_name AS client_first_name,
    cl.last_name AS client_last_name,
    l.first_name AS lawyer_first_name,
    l.last_name AS lawyer_last_name,
    j.first_name AS judge_first_name,
    j.last_name AS judge_last_name
FROM LawFirm.trial t
         JOIN LawFirm.client cl ON t.client_id = cl.id
         JOIN LawFirm.lawyer l ON t.lawyer_id = l.id
         JOIN LawFirm.judge j ON t.judge_id = j.id
         JOIN LawFirm.trial_status ts ON t.trial_status_id = ts.id;



--  4. Widok Wymaganych Dokumentów dla Rozprawy
--      Zestawia wszystkie dokumenty wymagane do danej rozprawy.

CREATE VIEW LawFirm.required_documents_per_trial AS
SELECT
    rd.trial_id,
    t.title AS trial_title,
    d.id AS document_id,
    d.file_path AS document_path,
    dt.name AS document_type
FROM LawFirm.required_documents_for_trial rd
         JOIN LawFirm.document d ON rd.document_id = d.id
         JOIN LawFirm.document_types dt ON d.type_id = dt.id
         JOIN LawFirm.trial t ON rd.trial_id = t.id;



--  5. Widok Decyzji w Sprawach
--      Lista decyzji podejmowanych w sprawach.

CREATE VIEW LawFirm.decisions_in_cases AS
SELECT
    d.id AS decision_id,
    d.name AS decision_name,
    d.description AS decision_description,
    d.date AS decision_date,
    ca.name AS case_name,
    ca.description AS case_description
FROM LawFirm.decision d
         JOIN LawFirm.case ca ON d.case_id = ca.id;


-- 6. Widok z rozprawą
-- Pobiera wszystkie relacje

CREATE VIEW LawFirm.full_data_trial AS
SELECT
    t.id AS trial_id,
    t.title AS trial_title,
    t.description AS trial_description,
    t.trial_status_id AS trial_status_id,
    ts.name AS trial_status_name,
    t.client_id AS client_id,
    c.first_name AS client_first_name,
    c.last_name AS client_last_name,
    t.lawyer_id AS lawyer_id,
    l.first_name AS lawyer_first_name,
    l.last_name AS lawyer_last_name,
    t.judge_id AS judge_id,
    j.first_name AS judge_first_name,
    j.last_name AS judge_last_name,
    t.date AS trial_date,
    t.case_id AS case_id
FROM LawFirm.trial t
         LEFT JOIN LawFirm.trial_status ts ON t.trial_status_id = ts.id
         LEFT JOIN LawFirm.client c ON t.client_id = c.id
         LEFT JOIN LawFirm.lawyer l ON t.lawyer_id = l.id
         LEFT JOIN LawFirm.judge j ON t.judge_id = j.id
;

