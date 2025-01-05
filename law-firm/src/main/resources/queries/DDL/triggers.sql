-- Przy edycji wydzialu sadu sedziego nie moze miec zadnych niedokonczonych spraw

    CREATE OR REPLACE FUNCTION LawFirm.check_judge_cases_on_court_division_update()
        RETURNS TRIGGER AS $$
    BEGIN
        -- Sprawdź, czy sędzia ma niedokończone sprawy
        IF EXISTS (
            SELECT 1
            FROM LawFirm.trial t
            WHERE t.judge_id = OLD.id
              AND t.trial_status_id IN (
                SELECT id
                FROM LawFirm.trial_status
                WHERE name NOT IN ('Closed', 'Completed') -- Statusy oznaczające niedokończone sprawy
            )
        ) THEN
            RAISE EXCEPTION 'Cannot change court division for judge %; they have unresolved cases.', OLD.first_name || ' ' || OLD.last_name;
        END IF;

        RETURN NEW;
    END;
    $$ LANGUAGE plpgsql;

    CREATE TRIGGER trg_check_judge_cases_on_update
        BEFORE UPDATE OF court_division_id ON LawFirm.judge
        FOR EACH ROW
    EXECUTE FUNCTION LawFirm.check_judge_cases_on_court_division_update();




-- Przy przypisaniu prawnika do rozprawy - sprawdzic dostepnosc

    CREATE OR REPLACE FUNCTION LawFirm.check_lawyer_availability_on_trial_assignment()
        RETURNS TRIGGER AS $$
    BEGIN
        -- Sprawdź, czy prawnik jest dostępny w dniu rozprawy
        IF EXISTS (
            SELECT 1
            FROM LawFirm.trial t
            WHERE t.lawyer_id = NEW.lawyer_id
              AND t.date = NEW.date
              AND t.id != NEW.id
        ) THEN
            RAISE EXCEPTION 'Lawyer % is already assigned to another trial on %.', NEW.lawyer_id, NEW.date;
        END IF;

        RETURN NEW;
    END;
    $$ LANGUAGE plpgsql;

    CREATE TRIGGER trg_check_lawyer_availability
        BEFORE INSERT OR UPDATE OF lawyer_id, date ON LawFirm.trial
        FOR EACH ROW
    EXECUTE FUNCTION LawFirm.check_lawyer_availability_on_trial_assignment();




-- Przy przypisaniu prawnika do sprawy jako prowadzacy - moze prowadzic max 5 spraw na raz

    CREATE OR REPLACE FUNCTION LawFirm.check_lawyer_case_limit()
        RETURNS TRIGGER AS $$
    BEGIN
        -- Sprawdź, ile spraw prowadzi obecnie prawnik
        IF (
               SELECT COUNT(*)
               FROM LawFirm.case c
               WHERE c.responsible_lawyer_id = NEW.responsible_lawyer_id
                 AND NOT EXISTS (
                   SELECT 1
                   FROM LawFirm.trial t
                   WHERE t.case_id = c.id
                     AND t.trial_status_id IN (
                       SELECT id
                       FROM LawFirm.trial_status
                       WHERE name IN ('Closed', 'Completed') -- Tylko zakończone sprawy się nie liczą
                   )
               )
           ) >= 5 THEN
            RAISE EXCEPTION 'Lawyer % cannot be assigned to more than 5 active cases.', NEW.responsible_lawyer_id;
        END IF;

        RETURN NEW;
    END;
    $$ LANGUAGE plpgsql;

    CREATE TRIGGER trg_check_lawyer_case_limit
        BEFORE INSERT OR UPDATE OF responsible_lawyer_id ON LawFirm.case
        FOR EACH ROW
    EXECUTE FUNCTION LawFirm.check_lawyer_case_limit();




-- Automatyczne ustawianie statusu sprawy na "Zakończona",
--          jeśli wszystkie jej rozprawy są zakończone
-- Gdy status wszystkich rozpraw związanych z daną sprawą zmieni się na "Closed" lub "Completed",
-- status sprawy (case) również zmieni się automatycznie.

    CREATE OR REPLACE FUNCTION LawFirm.auto_close_case_if_trials_completed()
        RETURNS TRIGGER AS $$
    BEGIN
        -- Sprawdź, czy wszystkie rozprawy związane z daną sprawą są zakończone
        IF NOT EXISTS (
            SELECT 1
            FROM LawFirm.trial t
            WHERE t.case_id = NEW.case_id
              AND t.trial_status_id NOT IN (
                SELECT id
                FROM LawFirm.trial_status
                WHERE name IN ('Closed', 'Completed')
            )
        ) THEN
            UPDATE LawFirm.case
            SET description = 'Closed - All trials are completed.'
            WHERE id = NEW.case_id;
        END IF;

        RETURN NEW;
    END;
    $$ LANGUAGE plpgsql;

    CREATE TRIGGER trg_auto_close_case
        AFTER UPDATE OF trial_status_id ON LawFirm.trial
        FOR EACH ROW
    EXECUTE FUNCTION LawFirm.auto_close_case_if_trials_completed();


--  Blokada usunięcia klienta, który ma przypisane aktywne sprawy
-- Klient, który ma przypisane sprawy, nie może zostać usunięty z bazy danych.

    CREATE OR REPLACE FUNCTION LawFirm.prevent_client_deletion_with_active_cases()
        RETURNS TRIGGER AS $$
    BEGIN
        -- Sprawdź, czy klient ma przypisane aktywne sprawy
        IF EXISTS (
            SELECT 1
            FROM LawFirm.case c
            WHERE c.client_id = OLD.id
        ) THEN
            RAISE EXCEPTION 'Cannot delete client %; they have active cases.', OLD.first_name || ' ' || OLD.last_name;
        END IF;

        RETURN OLD;
    END;
    $$ LANGUAGE plpgsql;

    CREATE TRIGGER trg_prevent_client_deletion
        BEFORE DELETE ON LawFirm.client
        FOR EACH ROW
    EXECUTE FUNCTION LawFirm.prevent_client_deletion_with_active_cases();



--  Blokada usunięcia dokumentów, które są wymagane w aktywnych procesach

CREATE OR REPLACE FUNCTION LawFirm.prevent_document_deletion_if_required()
    RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM LawFirm.required_documents_for_trial rd
                 INNER JOIN LawFirm.trial t ON rd.trial_id = t.id
        WHERE rd.document_id = OLD.id
          AND t.trial_status_id NOT IN (
            SELECT id
            FROM LawFirm.trial_status
            WHERE name IN ('Closed', 'Completed')
        )
    ) THEN
        RAISE EXCEPTION 'Cannot delete document %; it is required in an active trial.', OLD.id;
    END IF;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_prevent_document_deletion
    BEFORE DELETE ON LawFirm.document
    FOR EACH ROW
EXECUTE FUNCTION LawFirm.prevent_document_deletion_if_required();
