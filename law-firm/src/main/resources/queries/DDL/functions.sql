CREATE FUNCTION LawFirm.calculate_case_duration(start_date DATE, end_date DATE)
    RETURNS INTERVAL AS $$
BEGIN
    RETURN end_date - start_date;
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION LawFirm.get_trial_count_for_client(client_id_param INT)
    RETURNS INT AS $$
DECLARE
    trial_count INT;
BEGIN
    SELECT COUNT(*)
        INTO trial_count
    FROM LawFirm.trial
    WHERE client_id = client_id_param;

    RETURN trial_count;
END;
$$ LANGUAGE plpgsql;


-- GET SIGNATURE DATA

CREATE OR REPLACE FUNCTION LawFirm.get_signature_person_data(signature_id INT)
    RETURNS TABLE (first_name TEXT, last_name TEXT) AS $$
BEGIN
    RETURN QUERY
        SELECT c.first_name, c.last_name
        FROM LawFirm.signature s
                 JOIN LawFirm.client c
                      ON s.person_id = c.id
        WHERE s.id = signature_id AND s.role = 'client'

        UNION ALL

        SELECT l.first_name, l.last_name
        FROM LawFirm.signature s
                 JOIN LawFirm.lawyer l
                      ON s.person_id = l.id
        WHERE s.id = signature_id AND s.role = 'lawyer'

        UNION ALL

        SELECT j.first_name, j.last_name
        FROM LawFirm.signature s
                 JOIN LawFirm.judge j
                      ON s.person_id = j.id
        WHERE s.id = signature_id AND s.role = 'judge';
END;
$$ LANGUAGE plpgsql;


-- NUMBER OF CASES FALLING ONTO ONE LAWYER
-- Liczba spraw przypadających na każdego prawnika

CREATE OR REPLACE FUNCTION LawFirm.get_case_count_per_lawyer(min_cases INT DEFAULT 0)
    RETURNS TABLE(lawyer_name TEXT, case_count INT) AS $$
BEGIN
    RETURN QUERY
        SELECT
            l.first_name || ' ' || l.last_name AS lawyer_name,
            COUNT(c.id) AS case_count
        FROM
            LawFirm.case c
                INNER JOIN
            LawFirm.lawyer l ON c.responsible_lawyer_id = l.id
        GROUP BY
            l.id
        HAVING
            COUNT(c.id) > min_cases
        ORDER BY
            case_count DESC;
END;
$$ LANGUAGE plpgsql;


-- LIST OF DOCUMENTS WITH ITS STATUSES AND NUMBER OF REQUIRED DOCUMENTS
-- Lista procesów z ich statusami i liczbą wymaganych dokumentów

CREATE OR REPLACE FUNCTION LawFirm.get_trial_details()
    RETURNS TABLE(trial_title TEXT, trial_date DATE, status TEXT, required_documents_count INT) AS $$
BEGIN
    RETURN QUERY
        SELECT
            t.title AS trial_title,
            t.date AS trial_date,
            ts.name AS status,
            COUNT(rd.document_id) AS required_documents_count
        FROM
            LawFirm.trial t
                INNER JOIN
            LawFirm.trial_status ts ON t.trial_status_id = ts.id
                LEFT JOIN
            LawFirm.required_documents_for_trial rd ON rd.trial_id = t.id
        GROUP BY
            t.id, t.title, t.date, ts.name
        ORDER BY
            trial_date DESC;
END;
$$ LANGUAGE plpgsql;



-- CLIENTS WITH GREATEST NUMBER OF TRIALS
-- Klienci z największą liczbą procesów

CREATE OR REPLACE FUNCTION LawFirm.get_top_clients(limit_count INT DEFAULT 10)
    RETURNS TABLE(client_name TEXT, trial_count INT) AS $$
BEGIN
    RETURN QUERY
        SELECT
            c.first_name || ' ' || c.last_name AS client_name,
            COUNT(t.id) AS trial_count
        FROM
            LawFirm.client c
                LEFT JOIN
            LawFirm.trial t ON t.client_id = c.id
        GROUP BY
            c.id
        ORDER BY
            trial_count DESC
        LIMIT limit_count;
END;
$$ LANGUAGE plpgsql;


-- DOCUMENTS THAT DONTY HAVE SIGNATURES
-- Dokumenty, które nie mają jeszcze podpisów

CREATE OR REPLACE FUNCTION LawFirm.get_unsigned_documents()
    RETURNS TABLE(document_title TEXT, signatures_count INT) AS $$
BEGIN
    RETURN QUERY
        SELECT
            d.title AS document_title,
            COUNT(s.id) AS signatures_count
        FROM
            LawFirm.document d
                LEFT JOIN
            LawFirm.required_documents_for_trial rd ON d.id = rd.document_id
                LEFT JOIN
            LawFirm.signature s ON rd.id = s.required_document_id
        GROUP BY
            d.id
        HAVING
            COUNT(s.id) = 0;
END;
$$ LANGUAGE plpgsql;



-- LIST OF DECISIONS
CREATE OR REPLACE FUNCTION LawFirm.get_case_decisions(start_date DATE DEFAULT '2000-01-01')
    RETURNS TABLE(case_name TEXT, decision_name TEXT, decision_description TEXT, decision_date DATE) AS $$
BEGIN
    RETURN QUERY
        SELECT
            c.name AS case_name,
            d.name AS decision_name,
            d.description AS decision_description,
            d.date AS decision_date
        FROM
            LawFirm.case c
                INNER JOIN
            LawFirm.decision d ON c.id = d.case_id
        WHERE
            d.date >= start_date
        ORDER BY
            d.date DESC;
END;
$$ LANGUAGE plpgsql;



-- Funkcję SQL, która na podstawie ID klienta wylicza kwotę do zapłaty za bieżący miesiąc.

CREATE OR REPLACE FUNCTION LawFirm.calculate_monthly_payment(
    client_id INT,
    document_cost NUMERIC DEFAULT 100,
    trial_cost NUMERIC DEFAULT 300
)
    RETURNS NUMERIC AS $$
DECLARE
    total_payment NUMERIC := 0;
BEGIN

    -- Suma kosztów dokumentów (100 zł za każdy dokument powiązany z rozprawami w bieżącym miesiącu)
    total_payment := total_payment + COALESCE((
      SELECT COUNT(d.id) * document_cost
      FROM LawFirm.trial t
               JOIN LawFirm.required_documents_for_trial rd ON rd.trial_id = t.id
               JOIN LawFirm.document d ON rd.document_id = d.id
      WHERE t.client_id = calculate_monthly_payment.client_id
        AND EXTRACT(MONTH FROM t.date) = EXTRACT(MONTH FROM CURRENT_DATE)
        AND EXTRACT(YEAR FROM t.date) = EXTRACT(YEAR FROM CURRENT_DATE)
    ), 0);

    -- Suma kosztów rozpraw (300–500 zł za każdą rozprawę w bieżącym miesiącu)
    total_payment := total_payment + COALESCE((
      SELECT COUNT(t.id) * trial_cost
      FROM LawFirm.trial t
      WHERE t.client_id = calculate_monthly_payment.client_id
        AND EXTRACT(MONTH FROM t.date) = EXTRACT(MONTH FROM CURRENT_DATE)
        AND EXTRACT(YEAR FROM t.date) = EXTRACT(YEAR FROM CURRENT_DATE)
    ), 0);

    RETURN total_payment;
END;
$$ LANGUAGE plpgsql;
