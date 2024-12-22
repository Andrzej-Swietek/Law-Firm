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
