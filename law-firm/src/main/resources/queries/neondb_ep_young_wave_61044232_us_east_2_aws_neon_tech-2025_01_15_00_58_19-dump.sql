--
-- PostgreSQL database dump
--

-- Dumped from database version 15.10
-- Dumped by pg_dump version 16.6 (Ubuntu 16.6-0ubuntu0.24.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: lawfirm; Type: SCHEMA; Schema: -; Owner: mr.andi321
--

CREATE SCHEMA lawfirm;


ALTER SCHEMA lawfirm OWNER TO "mr.andi321";

--
-- Name: calculate_case_duration(date, date); Type: FUNCTION; Schema: lawfirm; Owner: mr.andi321
--

CREATE FUNCTION lawfirm.calculate_case_duration(start_date date, end_date date) RETURNS interval
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN end_date - start_date;
END;
$$;


ALTER FUNCTION lawfirm.calculate_case_duration(start_date date, end_date date) OWNER TO "mr.andi321";

--
-- Name: calculate_monthly_payment(integer, numeric, numeric); Type: FUNCTION; Schema: lawfirm; Owner: mr.andi321
--

CREATE FUNCTION lawfirm.calculate_monthly_payment(client_id integer, document_cost numeric DEFAULT 100, trial_cost numeric DEFAULT 300) RETURNS numeric
    LANGUAGE plpgsql
    AS $$
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
$$;


ALTER FUNCTION lawfirm.calculate_monthly_payment(client_id integer, document_cost numeric, trial_cost numeric) OWNER TO "mr.andi321";

--
-- Name: get_case_count_per_lawyer(integer); Type: FUNCTION; Schema: lawfirm; Owner: mr.andi321
--

CREATE FUNCTION lawfirm.get_case_count_per_lawyer(min_cases integer DEFAULT 0) RETURNS TABLE(lawyer_name text, case_count integer)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
        SELECT
            l.first_name || ' ' || l.last_name AS lawyer_name,
            COUNT(c.id)::INTEGER AS case_count
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
$$;


ALTER FUNCTION lawfirm.get_case_count_per_lawyer(min_cases integer) OWNER TO "mr.andi321";

--
-- Name: get_case_decisions(date); Type: FUNCTION; Schema: lawfirm; Owner: mr.andi321
--

CREATE FUNCTION lawfirm.get_case_decisions(start_date date DEFAULT '2000-01-01'::date) RETURNS TABLE(case_name text, decision_name text, decision_description text, decision_date date)
    LANGUAGE plpgsql
    AS $$
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
$$;


ALTER FUNCTION lawfirm.get_case_decisions(start_date date) OWNER TO "mr.andi321";

--
-- Name: get_signature_person_data(integer); Type: FUNCTION; Schema: lawfirm; Owner: mr.andi321
--

CREATE FUNCTION lawfirm.get_signature_person_data(signature_id integer) RETURNS TABLE(first_name text, last_name text)
    LANGUAGE plpgsql
    AS $$
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
$$;


ALTER FUNCTION lawfirm.get_signature_person_data(signature_id integer) OWNER TO "mr.andi321";

--
-- Name: get_top_clients(integer); Type: FUNCTION; Schema: lawfirm; Owner: mr.andi321
--

CREATE FUNCTION lawfirm.get_top_clients(limit_count integer DEFAULT 10) RETURNS TABLE(client_name text, trial_count integer)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
        SELECT
            c.first_name || ' ' || c.last_name AS client_name,
            COUNT(t.id)::INTEGER AS trial_count
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
$$;


ALTER FUNCTION lawfirm.get_top_clients(limit_count integer) OWNER TO "mr.andi321";

--
-- Name: get_trial_count_for_client(integer); Type: FUNCTION; Schema: lawfirm; Owner: mr.andi321
--

CREATE FUNCTION lawfirm.get_trial_count_for_client(client_id_param integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    trial_count INT;
BEGIN
    SELECT COUNT(*)
        INTO trial_count
    FROM LawFirm.trial
    WHERE client_id = client_id_param;

    RETURN trial_count;
END;
$$;


ALTER FUNCTION lawfirm.get_trial_count_for_client(client_id_param integer) OWNER TO "mr.andi321";

--
-- Name: get_trial_details(); Type: FUNCTION; Schema: lawfirm; Owner: mr.andi321
--

CREATE FUNCTION lawfirm.get_trial_details() RETURNS TABLE(trial_title text, trial_date date, status text, required_documents_count integer)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
        SELECT
            t.title AS trial_title,
            t.date AS trial_date,
            ts.name::TEXT AS status,
            COUNT(rd.document_id)::INTEGER AS required_documents_count
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
$$;


ALTER FUNCTION lawfirm.get_trial_details() OWNER TO "mr.andi321";

--
-- Name: get_unsigned_documents(); Type: FUNCTION; Schema: lawfirm; Owner: mr.andi321
--

CREATE FUNCTION lawfirm.get_unsigned_documents() RETURNS TABLE(document_title text, signatures_count integer)
    LANGUAGE plpgsql
    AS $$
BEGIN
    BEGIN
        RETURN QUERY
            SELECT
                d.title::TEXT AS document_title,
                COUNT(s.id)::INTEGER AS signatures_count
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
    EXCEPTION
        WHEN OTHERS THEN
            -- W przypadku jakiegokolwiek błędu zwróć pustą tabelę
            RETURN QUERY SELECT NULL::TEXT, NULL::INTEGER LIMIT 0;
    END;
END;
$$;


ALTER FUNCTION lawfirm.get_unsigned_documents() OWNER TO "mr.andi321";

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: appeal; Type: TABLE; Schema: lawfirm; Owner: mr.andi321
--

CREATE TABLE lawfirm.appeal (
    id integer NOT NULL,
    initial_ruling_id integer NOT NULL,
    final_ruling_id integer NOT NULL,
    trial_id integer NOT NULL
);


ALTER TABLE lawfirm.appeal OWNER TO "mr.andi321";

--
-- Name: appeal_id_seq; Type: SEQUENCE; Schema: lawfirm; Owner: mr.andi321
--

CREATE SEQUENCE lawfirm.appeal_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE lawfirm.appeal_id_seq OWNER TO "mr.andi321";

--
-- Name: appeal_id_seq; Type: SEQUENCE OWNED BY; Schema: lawfirm; Owner: mr.andi321
--

ALTER SEQUENCE lawfirm.appeal_id_seq OWNED BY lawfirm.appeal.id;


--
-- Name: case; Type: TABLE; Schema: lawfirm; Owner: mr.andi321
--

CREATE TABLE lawfirm."case" (
    id integer NOT NULL,
    name text NOT NULL,
    description text NOT NULL,
    responsible_lawyer_id integer NOT NULL,
    client_id integer NOT NULL
);


ALTER TABLE lawfirm."case" OWNER TO "mr.andi321";

--
-- Name: case_id_seq; Type: SEQUENCE; Schema: lawfirm; Owner: mr.andi321
--

CREATE SEQUENCE lawfirm.case_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE lawfirm.case_id_seq OWNER TO "mr.andi321";

--
-- Name: case_id_seq; Type: SEQUENCE OWNED BY; Schema: lawfirm; Owner: mr.andi321
--

ALTER SEQUENCE lawfirm.case_id_seq OWNED BY lawfirm."case".id;


--
-- Name: client; Type: TABLE; Schema: lawfirm; Owner: mr.andi321
--

CREATE TABLE lawfirm.client (
    id integer NOT NULL,
    first_name text NOT NULL,
    last_name text NOT NULL,
    email text NOT NULL,
    contact_data_id integer NOT NULL
);


ALTER TABLE lawfirm.client OWNER TO "mr.andi321";

--
-- Name: lawyer; Type: TABLE; Schema: lawfirm; Owner: mr.andi321
--

CREATE TABLE lawfirm.lawyer (
    id integer NOT NULL,
    first_name text NOT NULL,
    last_name text NOT NULL,
    specialization text NOT NULL
);


ALTER TABLE lawfirm.lawyer OWNER TO "mr.andi321";

--
-- Name: case_with_details; Type: VIEW; Schema: lawfirm; Owner: mr.andi321
--

CREATE VIEW lawfirm.case_with_details AS
 SELECT ca.id AS case_id,
    ca.name AS case_name,
    ca.description AS case_description,
    l.first_name AS lawyer_first_name,
    l.last_name AS lawyer_last_name,
    cl.first_name AS client_first_name,
    cl.last_name AS client_last_name
   FROM ((lawfirm."case" ca
     JOIN lawfirm.lawyer l ON ((ca.responsible_lawyer_id = l.id)))
     JOIN lawfirm.client cl ON ((ca.client_id = cl.id)));


ALTER VIEW lawfirm.case_with_details OWNER TO "mr.andi321";

--
-- Name: client_id_seq; Type: SEQUENCE; Schema: lawfirm; Owner: mr.andi321
--

CREATE SEQUENCE lawfirm.client_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE lawfirm.client_id_seq OWNER TO "mr.andi321";

--
-- Name: client_id_seq; Type: SEQUENCE OWNED BY; Schema: lawfirm; Owner: mr.andi321
--

ALTER SEQUENCE lawfirm.client_id_seq OWNED BY lawfirm.client.id;


--
-- Name: contact_data; Type: TABLE; Schema: lawfirm; Owner: mr.andi321
--

CREATE TABLE lawfirm.contact_data (
    id integer NOT NULL,
    phone_number text NOT NULL,
    email text NOT NULL,
    street text NOT NULL,
    city text NOT NULL,
    state text NOT NULL,
    zip_code text NOT NULL,
    country text NOT NULL
);


ALTER TABLE lawfirm.contact_data OWNER TO "mr.andi321";

--
-- Name: client_with_contact; Type: VIEW; Schema: lawfirm; Owner: mr.andi321
--

CREATE VIEW lawfirm.client_with_contact AS
 SELECT c.id AS client_id,
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
   FROM (lawfirm.client c
     JOIN lawfirm.contact_data cd ON ((c.contact_data_id = cd.id)));


ALTER VIEW lawfirm.client_with_contact OWNER TO "mr.andi321";

--
-- Name: contact_data_id_seq; Type: SEQUENCE; Schema: lawfirm; Owner: mr.andi321
--

CREATE SEQUENCE lawfirm.contact_data_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE lawfirm.contact_data_id_seq OWNER TO "mr.andi321";

--
-- Name: contact_data_id_seq; Type: SEQUENCE OWNED BY; Schema: lawfirm; Owner: mr.andi321
--

ALTER SEQUENCE lawfirm.contact_data_id_seq OWNED BY lawfirm.contact_data.id;


--
-- Name: court_division; Type: TABLE; Schema: lawfirm; Owner: mr.andi321
--

CREATE TABLE lawfirm.court_division (
    id integer NOT NULL,
    name text NOT NULL,
    city text NOT NULL
);


ALTER TABLE lawfirm.court_division OWNER TO "mr.andi321";

--
-- Name: court_division_id_seq; Type: SEQUENCE; Schema: lawfirm; Owner: mr.andi321
--

CREATE SEQUENCE lawfirm.court_division_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE lawfirm.court_division_id_seq OWNER TO "mr.andi321";

--
-- Name: court_division_id_seq; Type: SEQUENCE OWNED BY; Schema: lawfirm; Owner: mr.andi321
--

ALTER SEQUENCE lawfirm.court_division_id_seq OWNED BY lawfirm.court_division.id;


--
-- Name: decision; Type: TABLE; Schema: lawfirm; Owner: mr.andi321
--

CREATE TABLE lawfirm.decision (
    id integer NOT NULL,
    name text NOT NULL,
    description text NOT NULL,
    date date NOT NULL,
    case_id integer NOT NULL
);


ALTER TABLE lawfirm.decision OWNER TO "mr.andi321";

--
-- Name: decision_id_seq; Type: SEQUENCE; Schema: lawfirm; Owner: mr.andi321
--

CREATE SEQUENCE lawfirm.decision_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE lawfirm.decision_id_seq OWNER TO "mr.andi321";

--
-- Name: decision_id_seq; Type: SEQUENCE OWNED BY; Schema: lawfirm; Owner: mr.andi321
--

ALTER SEQUENCE lawfirm.decision_id_seq OWNED BY lawfirm.decision.id;


--
-- Name: decisions_in_cases; Type: VIEW; Schema: lawfirm; Owner: mr.andi321
--

CREATE VIEW lawfirm.decisions_in_cases AS
 SELECT d.id AS decision_id,
    d.name AS decision_name,
    d.description AS decision_description,
    d.date AS decision_date,
    ca.name AS case_name,
    ca.description AS case_description
   FROM (lawfirm.decision d
     JOIN lawfirm."case" ca ON ((d.case_id = ca.id)));


ALTER VIEW lawfirm.decisions_in_cases OWNER TO "mr.andi321";

--
-- Name: document; Type: TABLE; Schema: lawfirm; Owner: mr.andi321
--

CREATE TABLE lawfirm.document (
    id integer NOT NULL,
    type_id integer NOT NULL,
    file_path text NOT NULL,
    title character varying(255),
    description text
);


ALTER TABLE lawfirm.document OWNER TO "mr.andi321";

--
-- Name: document_id_seq; Type: SEQUENCE; Schema: lawfirm; Owner: mr.andi321
--

CREATE SEQUENCE lawfirm.document_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE lawfirm.document_id_seq OWNER TO "mr.andi321";

--
-- Name: document_id_seq; Type: SEQUENCE OWNED BY; Schema: lawfirm; Owner: mr.andi321
--

ALTER SEQUENCE lawfirm.document_id_seq OWNED BY lawfirm.document.id;


--
-- Name: document_types; Type: TABLE; Schema: lawfirm; Owner: mr.andi321
--

CREATE TABLE lawfirm.document_types (
    id integer NOT NULL,
    name text NOT NULL
);


ALTER TABLE lawfirm.document_types OWNER TO "mr.andi321";

--
-- Name: document_types_id_seq; Type: SEQUENCE; Schema: lawfirm; Owner: mr.andi321
--

CREATE SEQUENCE lawfirm.document_types_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE lawfirm.document_types_id_seq OWNER TO "mr.andi321";

--
-- Name: document_types_id_seq; Type: SEQUENCE OWNED BY; Schema: lawfirm; Owner: mr.andi321
--

ALTER SEQUENCE lawfirm.document_types_id_seq OWNED BY lawfirm.document_types.id;


--
-- Name: full_data_case; Type: VIEW; Schema: lawfirm; Owner: mr.andi321
--

CREATE VIEW lawfirm.full_data_case AS
 SELECT c.id,
    c.name,
    c.description,
    c.responsible_lawyer_id,
    c.client_id,
    cl.id AS case_client_id,
    cl.first_name AS client_first_name,
    cl.last_name AS client_last_name,
    cl.email AS client_email,
    cl.contact_data_id AS client_contact_details_id,
    l.id AS case_responsible_lawyer_id,
    l.first_name AS lawyer_first_name,
    l.last_name AS lawyer_last_name,
    l.specialization AS lawyer_specialization,
    cd.id AS contact_details_id,
    cd.phone_number AS contact_details_phone_number,
    cd.email AS contact_details_email,
    cd.street AS contact_details_street,
    cd.city AS contact_details_city,
    cd.state AS contact_details_state,
    cd.zip_code AS contact_details_zip_code,
    cd.country AS contact_details_country
   FROM (((lawfirm."case" c
     LEFT JOIN lawfirm.client cl ON ((c.client_id = cl.id)))
     LEFT JOIN lawfirm.lawyer l ON ((c.responsible_lawyer_id = l.id)))
     LEFT JOIN lawfirm.contact_data cd ON ((cl.contact_data_id = cd.id)));


ALTER VIEW lawfirm.full_data_case OWNER TO "mr.andi321";

--
-- Name: judge; Type: TABLE; Schema: lawfirm; Owner: mr.andi321
--

CREATE TABLE lawfirm.judge (
    id integer NOT NULL,
    first_name text NOT NULL,
    last_name text NOT NULL,
    court_division_id integer NOT NULL
);


ALTER TABLE lawfirm.judge OWNER TO "mr.andi321";

--
-- Name: trial; Type: TABLE; Schema: lawfirm; Owner: mr.andi321
--

CREATE TABLE lawfirm.trial (
    id integer NOT NULL,
    title text NOT NULL,
    description text NOT NULL,
    trial_status_id integer NOT NULL,
    client_id integer NOT NULL,
    lawyer_id integer NOT NULL,
    judge_id integer NOT NULL,
    date date NOT NULL,
    case_id integer NOT NULL
);


ALTER TABLE lawfirm.trial OWNER TO "mr.andi321";

--
-- Name: trial_status; Type: TABLE; Schema: lawfirm; Owner: mr.andi321
--

CREATE TABLE lawfirm.trial_status (
    id integer NOT NULL,
    name text NOT NULL
);


ALTER TABLE lawfirm.trial_status OWNER TO "mr.andi321";

--
-- Name: full_data_trial; Type: VIEW; Schema: lawfirm; Owner: mr.andi321
--

CREATE VIEW lawfirm.full_data_trial AS
 SELECT t.id AS trial_id,
    t.title AS trial_title,
    t.description AS trial_description,
    t.client_id AS trial_client_id,
    t.lawyer_id AS trial_lawyer_id,
    t.judge_id AS trial_judge_id,
    t.client_id AS trial_case_id,
    t.date AS trial_date,
    t.trial_status_id,
    ts.name AS trial_status_name,
    c.id AS client_id,
    c.first_name AS client_first_name,
    c.last_name AS client_last_name,
    c.email AS client_email,
    c.contact_data_id AS client_contact_data_id,
    t.lawyer_id,
    l.first_name AS lawyer_first_name,
    l.last_name AS lawyer_last_name,
    l.specialization AS lawyer_specialization,
    j.id AS judge_id,
    j.first_name AS judge_first_name,
    j.last_name AS judge_last_name,
    j.court_division_id AS judge_court_division_id,
    cd.name AS court_division_name,
    cd.city AS court_division_city,
    ca.id AS case_id,
    ca.name AS case_name,
    ca.description AS case_description,
    ca.responsible_lawyer_id AS case_responsible_lawyer_id,
    ca.client_id AS case_client_id
   FROM ((((((lawfirm.trial t
     LEFT JOIN lawfirm.trial_status ts ON ((t.trial_status_id = ts.id)))
     LEFT JOIN lawfirm.client c ON ((t.client_id = c.id)))
     LEFT JOIN lawfirm.lawyer l ON ((t.lawyer_id = l.id)))
     LEFT JOIN lawfirm.judge j ON ((t.judge_id = j.id)))
     LEFT JOIN lawfirm.court_division cd ON ((j.court_division_id = cd.id)))
     LEFT JOIN lawfirm."case" ca ON ((t.case_id = ca.id)));


ALTER VIEW lawfirm.full_data_trial OWNER TO "mr.andi321";

--
-- Name: judge_id_seq; Type: SEQUENCE; Schema: lawfirm; Owner: mr.andi321
--

CREATE SEQUENCE lawfirm.judge_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE lawfirm.judge_id_seq OWNER TO "mr.andi321";

--
-- Name: judge_id_seq; Type: SEQUENCE OWNED BY; Schema: lawfirm; Owner: mr.andi321
--

ALTER SEQUENCE lawfirm.judge_id_seq OWNED BY lawfirm.judge.id;


--
-- Name: lawyer_id_seq; Type: SEQUENCE; Schema: lawfirm; Owner: mr.andi321
--

CREATE SEQUENCE lawfirm.lawyer_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE lawfirm.lawyer_id_seq OWNER TO "mr.andi321";

--
-- Name: lawyer_id_seq; Type: SEQUENCE OWNED BY; Schema: lawfirm; Owner: mr.andi321
--

ALTER SEQUENCE lawfirm.lawyer_id_seq OWNED BY lawfirm.lawyer.id;


--
-- Name: required_documents_for_trial; Type: TABLE; Schema: lawfirm; Owner: mr.andi321
--

CREATE TABLE lawfirm.required_documents_for_trial (
    id integer NOT NULL,
    trial_id integer NOT NULL,
    document_id integer NOT NULL
);


ALTER TABLE lawfirm.required_documents_for_trial OWNER TO "mr.andi321";

--
-- Name: required_documents_for_trial_id_seq; Type: SEQUENCE; Schema: lawfirm; Owner: mr.andi321
--

CREATE SEQUENCE lawfirm.required_documents_for_trial_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE lawfirm.required_documents_for_trial_id_seq OWNER TO "mr.andi321";

--
-- Name: required_documents_for_trial_id_seq; Type: SEQUENCE OWNED BY; Schema: lawfirm; Owner: mr.andi321
--

ALTER SEQUENCE lawfirm.required_documents_for_trial_id_seq OWNED BY lawfirm.required_documents_for_trial.id;


--
-- Name: required_documents_per_trial; Type: VIEW; Schema: lawfirm; Owner: mr.andi321
--

CREATE VIEW lawfirm.required_documents_per_trial AS
 SELECT rd.trial_id,
    t.title AS trial_title,
    d.id AS document_id,
    d.file_path AS document_path,
    dt.name AS document_type
   FROM (((lawfirm.required_documents_for_trial rd
     JOIN lawfirm.document d ON ((rd.document_id = d.id)))
     JOIN lawfirm.document_types dt ON ((d.type_id = dt.id)))
     JOIN lawfirm.trial t ON ((rd.trial_id = t.id)));


ALTER VIEW lawfirm.required_documents_per_trial OWNER TO "mr.andi321";

--
-- Name: ruling; Type: TABLE; Schema: lawfirm; Owner: mr.andi321
--

CREATE TABLE lawfirm.ruling (
    id integer NOT NULL,
    is_final boolean NOT NULL,
    content text NOT NULL,
    trial_id integer NOT NULL,
    finalization_date date NOT NULL
);


ALTER TABLE lawfirm.ruling OWNER TO "mr.andi321";

--
-- Name: ruling_id_seq; Type: SEQUENCE; Schema: lawfirm; Owner: mr.andi321
--

CREATE SEQUENCE lawfirm.ruling_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE lawfirm.ruling_id_seq OWNER TO "mr.andi321";

--
-- Name: ruling_id_seq; Type: SEQUENCE OWNED BY; Schema: lawfirm; Owner: mr.andi321
--

ALTER SEQUENCE lawfirm.ruling_id_seq OWNED BY lawfirm.ruling.id;


--
-- Name: signature; Type: TABLE; Schema: lawfirm; Owner: mr.andi321
--

CREATE TABLE lawfirm.signature (
    id integer NOT NULL,
    person_id integer NOT NULL,
    role text NOT NULL,
    required_document_id integer NOT NULL,
    date date,
    CONSTRAINT signature_role_check CHECK ((role = ANY (ARRAY['client'::text, 'lawyer'::text, 'judge'::text])))
);


ALTER TABLE lawfirm.signature OWNER TO "mr.andi321";

--
-- Name: signature_id_seq; Type: SEQUENCE; Schema: lawfirm; Owner: mr.andi321
--

CREATE SEQUENCE lawfirm.signature_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE lawfirm.signature_id_seq OWNER TO "mr.andi321";

--
-- Name: signature_id_seq; Type: SEQUENCE OWNED BY; Schema: lawfirm; Owner: mr.andi321
--

ALTER SEQUENCE lawfirm.signature_id_seq OWNED BY lawfirm.signature.id;


--
-- Name: trial_id_seq; Type: SEQUENCE; Schema: lawfirm; Owner: mr.andi321
--

CREATE SEQUENCE lawfirm.trial_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE lawfirm.trial_id_seq OWNER TO "mr.andi321";

--
-- Name: trial_id_seq; Type: SEQUENCE OWNED BY; Schema: lawfirm; Owner: mr.andi321
--

ALTER SEQUENCE lawfirm.trial_id_seq OWNED BY lawfirm.trial.id;


--
-- Name: trial_status_id_seq; Type: SEQUENCE; Schema: lawfirm; Owner: mr.andi321
--

CREATE SEQUENCE lawfirm.trial_status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE lawfirm.trial_status_id_seq OWNER TO "mr.andi321";

--
-- Name: trial_status_id_seq; Type: SEQUENCE OWNED BY; Schema: lawfirm; Owner: mr.andi321
--

ALTER SEQUENCE lawfirm.trial_status_id_seq OWNED BY lawfirm.trial_status.id;


--
-- Name: trial_with_details; Type: VIEW; Schema: lawfirm; Owner: mr.andi321
--

CREATE VIEW lawfirm.trial_with_details AS
 SELECT t.id AS trial_id,
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
   FROM ((((lawfirm.trial t
     JOIN lawfirm.client cl ON ((t.client_id = cl.id)))
     JOIN lawfirm.lawyer l ON ((t.lawyer_id = l.id)))
     JOIN lawfirm.judge j ON ((t.judge_id = j.id)))
     JOIN lawfirm.trial_status ts ON ((t.trial_status_id = ts.id)));


ALTER VIEW lawfirm.trial_with_details OWNER TO "mr.andi321";

--
-- Name: appeal id; Type: DEFAULT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.appeal ALTER COLUMN id SET DEFAULT nextval('lawfirm.appeal_id_seq'::regclass);


--
-- Name: case id; Type: DEFAULT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm."case" ALTER COLUMN id SET DEFAULT nextval('lawfirm.case_id_seq'::regclass);


--
-- Name: client id; Type: DEFAULT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.client ALTER COLUMN id SET DEFAULT nextval('lawfirm.client_id_seq'::regclass);


--
-- Name: contact_data id; Type: DEFAULT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.contact_data ALTER COLUMN id SET DEFAULT nextval('lawfirm.contact_data_id_seq'::regclass);


--
-- Name: court_division id; Type: DEFAULT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.court_division ALTER COLUMN id SET DEFAULT nextval('lawfirm.court_division_id_seq'::regclass);


--
-- Name: decision id; Type: DEFAULT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.decision ALTER COLUMN id SET DEFAULT nextval('lawfirm.decision_id_seq'::regclass);


--
-- Name: document id; Type: DEFAULT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.document ALTER COLUMN id SET DEFAULT nextval('lawfirm.document_id_seq'::regclass);


--
-- Name: document_types id; Type: DEFAULT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.document_types ALTER COLUMN id SET DEFAULT nextval('lawfirm.document_types_id_seq'::regclass);


--
-- Name: judge id; Type: DEFAULT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.judge ALTER COLUMN id SET DEFAULT nextval('lawfirm.judge_id_seq'::regclass);


--
-- Name: lawyer id; Type: DEFAULT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.lawyer ALTER COLUMN id SET DEFAULT nextval('lawfirm.lawyer_id_seq'::regclass);


--
-- Name: required_documents_for_trial id; Type: DEFAULT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.required_documents_for_trial ALTER COLUMN id SET DEFAULT nextval('lawfirm.required_documents_for_trial_id_seq'::regclass);


--
-- Name: ruling id; Type: DEFAULT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.ruling ALTER COLUMN id SET DEFAULT nextval('lawfirm.ruling_id_seq'::regclass);


--
-- Name: signature id; Type: DEFAULT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.signature ALTER COLUMN id SET DEFAULT nextval('lawfirm.signature_id_seq'::regclass);


--
-- Name: trial id; Type: DEFAULT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.trial ALTER COLUMN id SET DEFAULT nextval('lawfirm.trial_id_seq'::regclass);


--
-- Name: trial_status id; Type: DEFAULT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.trial_status ALTER COLUMN id SET DEFAULT nextval('lawfirm.trial_status_id_seq'::regclass);


--
-- Data for Name: appeal; Type: TABLE DATA; Schema: lawfirm; Owner: mr.andi321
--

COPY lawfirm.appeal (id, initial_ruling_id, final_ruling_id, trial_id) FROM stdin;
1	1	2	1
\.


--
-- Data for Name: case; Type: TABLE DATA; Schema: lawfirm; Owner: mr.andi321
--

COPY lawfirm."case" (id, name, description, responsible_lawyer_id, client_id) FROM stdin;
1	MOCK CASE	Lorem Ipsum dolor sit amet	1	4
2	Mock-02	Mock-02	2	4
3	Mock-02	Mock-02	2	4
4	Rozwód #1234	Lorem Ipsum Dolor Sit amet consetetur 	2	5
\.


--
-- Data for Name: client; Type: TABLE DATA; Schema: lawfirm; Owner: mr.andi321
--

COPY lawfirm.client (id, first_name, last_name, email, contact_data_id) FROM stdin;
5	Andrzej	Świętek	mr.andi321@gmail.com	13
6	Dominik	Świętek	mr.dominik321@gmail.com	14
4	User	Example	user@example.com	12
7	Mikołaj	Świętek	mr.mik321@gmail.com	17
\.


--
-- Data for Name: contact_data; Type: TABLE DATA; Schema: lawfirm; Owner: mr.andi321
--

COPY lawfirm.contact_data (id, phone_number, email, street, city, state, zip_code, country) FROM stdin;
9	string	string	string	string	string	string	string
10	string	string	string	string	string	string	string
11	string	string	string	string	string	string	string
13	796940894	mr.andi321@gmail.com	Okrężna		małopolskie	32-005	Poland
14	796940894	mr.dominik321@gmail.com	Okrężna		małopolskie	32-005	Poland
12	+48 123 456 789	user@example.com	Street	string	State	32-005	Country
17	796940894	mr.mik321@gmail.com	Okrężna		małopolskie	32-005	Poland
\.


--
-- Data for Name: court_division; Type: TABLE DATA; Schema: lawfirm; Owner: mr.andi321
--

COPY lawfirm.court_division (id, name, city) FROM stdin;
1	Sąd Rodzinny	Kraków
2	Sąd Rodzinny	Wieliczka
3	Sąd Cywilny	Wieliczka
5	Sąd Cywilny	Bochnia
\.


--
-- Data for Name: decision; Type: TABLE DATA; Schema: lawfirm; Owner: mr.andi321
--

COPY lawfirm.decision (id, name, description, date, case_id) FROM stdin;
1	TEST DECISION	TEST DECISION	2024-12-23	1
4	ttt	ttt	2024-12-11	3
5	New Decision	test test test	2024-12-25	1
\.


--
-- Data for Name: document; Type: TABLE DATA; Schema: lawfirm; Owner: mr.andi321
--

COPY lawfirm.document (id, type_id, file_path, title, description) FROM stdin;
12	1	/documents/Tes-01-court-mock-doc.pdf	Test-01	Test 01
\.


--
-- Data for Name: document_types; Type: TABLE DATA; Schema: lawfirm; Owner: mr.andi321
--

COPY lawfirm.document_types (id, name) FROM stdin;
1	Raport
2	Akta policyjne
3	Księgi wieczyste
4	Operaty szacunkowe
5	Decyzje administracyjne
6	KRS
7	Uchwały zarządu
8	Umowy
\.


--
-- Data for Name: judge; Type: TABLE DATA; Schema: lawfirm; Owner: mr.andi321
--

COPY lawfirm.judge (id, first_name, last_name, court_division_id) FROM stdin;
2	Michael	Jackson	2
3	Andrzej	Świętek	1
1	John	Smith	2
\.


--
-- Data for Name: lawyer; Type: TABLE DATA; Schema: lawfirm; Owner: mr.andi321
--

COPY lawfirm.lawyer (id, first_name, last_name, specialization) FROM stdin;
1	Harvey	Specter	Corporate
2	Michael	Ross	Corporate
3	Test Lawyer - 01	Test Lawyer - 01	Criminal
\.


--
-- Data for Name: required_documents_for_trial; Type: TABLE DATA; Schema: lawfirm; Owner: mr.andi321
--

COPY lawfirm.required_documents_for_trial (id, trial_id, document_id) FROM stdin;
2	1	12
3	2	12
5	4	12
\.


--
-- Data for Name: ruling; Type: TABLE DATA; Schema: lawfirm; Owner: mr.andi321
--

COPY lawfirm.ruling (id, is_final, content, trial_id, finalization_date) FROM stdin;
1	f	Ruling to make ruling in not yet fully defined future\n	1	2024-12-30
2	t	Yet another useless Ruling to appealed 	1	2024-12-31
3	t	xxxxxxxxxxxxxxxxxxxxxxx\nxxxxxxxxxxxxxxxxxxxxxxx	3	2024-12-16
\.


--
-- Data for Name: signature; Type: TABLE DATA; Schema: lawfirm; Owner: mr.andi321
--

COPY lawfirm.signature (id, person_id, role, required_document_id, date) FROM stdin;
1	4	client	2	2025-01-02
2	4	client	3	2025-01-02
3	6	client	3	2025-01-02
4	1	judge	3	2025-01-03
5	2	lawyer	5	2025-01-08
\.


--
-- Data for Name: trial; Type: TABLE DATA; Schema: lawfirm; Owner: mr.andi321
--

COPY lawfirm.trial (id, title, description, trial_status_id, client_id, lawyer_id, judge_id, date, case_id) FROM stdin;
1	string	string	1	4	1	1	2024-12-23	1
2	Mock Trial 02	Mock Trial 02 - Lorem ipsum dolor sit amet	1	5	1	2	2025-01-03	1
3	Mock Trial 03	Lorem Ispum dolor sit amet 	3	5	2	3	2025-02-19	1
4	Mock Trial 04	Lorem Ispum dolor sit amet 	4	7	3	1	2025-02-17	1
\.


--
-- Data for Name: trial_status; Type: TABLE DATA; Schema: lawfirm; Owner: mr.andi321
--

COPY lawfirm.trial_status (id, name) FROM stdin;
1	OPEN
2	PENDING
3	SCHEDULED
4	FINISHED
\.


--
-- Name: appeal_id_seq; Type: SEQUENCE SET; Schema: lawfirm; Owner: mr.andi321
--

SELECT pg_catalog.setval('lawfirm.appeal_id_seq', 1, true);


--
-- Name: case_id_seq; Type: SEQUENCE SET; Schema: lawfirm; Owner: mr.andi321
--

SELECT pg_catalog.setval('lawfirm.case_id_seq', 4, true);


--
-- Name: client_id_seq; Type: SEQUENCE SET; Schema: lawfirm; Owner: mr.andi321
--

SELECT pg_catalog.setval('lawfirm.client_id_seq', 7, true);


--
-- Name: contact_data_id_seq; Type: SEQUENCE SET; Schema: lawfirm; Owner: mr.andi321
--

SELECT pg_catalog.setval('lawfirm.contact_data_id_seq', 17, true);


--
-- Name: court_division_id_seq; Type: SEQUENCE SET; Schema: lawfirm; Owner: mr.andi321
--

SELECT pg_catalog.setval('lawfirm.court_division_id_seq', 5, true);


--
-- Name: decision_id_seq; Type: SEQUENCE SET; Schema: lawfirm; Owner: mr.andi321
--

SELECT pg_catalog.setval('lawfirm.decision_id_seq', 6, true);


--
-- Name: document_id_seq; Type: SEQUENCE SET; Schema: lawfirm; Owner: mr.andi321
--

SELECT pg_catalog.setval('lawfirm.document_id_seq', 12, true);


--
-- Name: document_types_id_seq; Type: SEQUENCE SET; Schema: lawfirm; Owner: mr.andi321
--

SELECT pg_catalog.setval('lawfirm.document_types_id_seq', 8, true);


--
-- Name: judge_id_seq; Type: SEQUENCE SET; Schema: lawfirm; Owner: mr.andi321
--

SELECT pg_catalog.setval('lawfirm.judge_id_seq', 3, true);


--
-- Name: lawyer_id_seq; Type: SEQUENCE SET; Schema: lawfirm; Owner: mr.andi321
--

SELECT pg_catalog.setval('lawfirm.lawyer_id_seq', 4, true);


--
-- Name: required_documents_for_trial_id_seq; Type: SEQUENCE SET; Schema: lawfirm; Owner: mr.andi321
--

SELECT pg_catalog.setval('lawfirm.required_documents_for_trial_id_seq', 5, true);


--
-- Name: ruling_id_seq; Type: SEQUENCE SET; Schema: lawfirm; Owner: mr.andi321
--

SELECT pg_catalog.setval('lawfirm.ruling_id_seq', 3, true);


--
-- Name: signature_id_seq; Type: SEQUENCE SET; Schema: lawfirm; Owner: mr.andi321
--

SELECT pg_catalog.setval('lawfirm.signature_id_seq', 5, true);


--
-- Name: trial_id_seq; Type: SEQUENCE SET; Schema: lawfirm; Owner: mr.andi321
--

SELECT pg_catalog.setval('lawfirm.trial_id_seq', 4, true);


--
-- Name: trial_status_id_seq; Type: SEQUENCE SET; Schema: lawfirm; Owner: mr.andi321
--

SELECT pg_catalog.setval('lawfirm.trial_status_id_seq', 4, true);


--
-- Name: appeal appeal_pkey; Type: CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.appeal
    ADD CONSTRAINT appeal_pkey PRIMARY KEY (id);


--
-- Name: case case_pkey; Type: CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm."case"
    ADD CONSTRAINT case_pkey PRIMARY KEY (id);


--
-- Name: client client_pkey; Type: CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.client
    ADD CONSTRAINT client_pkey PRIMARY KEY (id);


--
-- Name: contact_data contact_data_pkey; Type: CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.contact_data
    ADD CONSTRAINT contact_data_pkey PRIMARY KEY (id);


--
-- Name: court_division court_division_pkey; Type: CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.court_division
    ADD CONSTRAINT court_division_pkey PRIMARY KEY (id);


--
-- Name: decision decision_pkey; Type: CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.decision
    ADD CONSTRAINT decision_pkey PRIMARY KEY (id);


--
-- Name: document document_pkey; Type: CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.document
    ADD CONSTRAINT document_pkey PRIMARY KEY (id);


--
-- Name: document_types document_types_name_key; Type: CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.document_types
    ADD CONSTRAINT document_types_name_key UNIQUE (name);


--
-- Name: document_types document_types_pkey; Type: CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.document_types
    ADD CONSTRAINT document_types_pkey PRIMARY KEY (id);


--
-- Name: judge judge_pkey; Type: CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.judge
    ADD CONSTRAINT judge_pkey PRIMARY KEY (id);


--
-- Name: lawyer lawyer_pkey; Type: CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.lawyer
    ADD CONSTRAINT lawyer_pkey PRIMARY KEY (id);


--
-- Name: required_documents_for_trial required_documents_for_trial_pkey; Type: CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.required_documents_for_trial
    ADD CONSTRAINT required_documents_for_trial_pkey PRIMARY KEY (id);


--
-- Name: ruling ruling_pkey; Type: CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.ruling
    ADD CONSTRAINT ruling_pkey PRIMARY KEY (id);


--
-- Name: signature signature_pkey; Type: CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.signature
    ADD CONSTRAINT signature_pkey PRIMARY KEY (id);


--
-- Name: trial trial_pkey; Type: CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.trial
    ADD CONSTRAINT trial_pkey PRIMARY KEY (id);


--
-- Name: trial_status trial_status_pkey; Type: CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.trial_status
    ADD CONSTRAINT trial_status_pkey PRIMARY KEY (id);


--
-- Name: appeal appeal_final_ruling_fk; Type: FK CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.appeal
    ADD CONSTRAINT appeal_final_ruling_fk FOREIGN KEY (final_ruling_id) REFERENCES lawfirm.ruling(id);


--
-- Name: appeal appeal_initial_ruling_fk; Type: FK CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.appeal
    ADD CONSTRAINT appeal_initial_ruling_fk FOREIGN KEY (initial_ruling_id) REFERENCES lawfirm.ruling(id);


--
-- Name: appeal appeal_trial_fk; Type: FK CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.appeal
    ADD CONSTRAINT appeal_trial_fk FOREIGN KEY (trial_id) REFERENCES lawfirm.trial(id);


--
-- Name: case case_client_fk; Type: FK CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm."case"
    ADD CONSTRAINT case_client_fk FOREIGN KEY (client_id) REFERENCES lawfirm.client(id);


--
-- Name: case case_lawyer_fk; Type: FK CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm."case"
    ADD CONSTRAINT case_lawyer_fk FOREIGN KEY (responsible_lawyer_id) REFERENCES lawfirm.lawyer(id);


--
-- Name: client client_contact_data_fk; Type: FK CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.client
    ADD CONSTRAINT client_contact_data_fk FOREIGN KEY (contact_data_id) REFERENCES lawfirm.contact_data(id);


--
-- Name: decision decision_case_fk; Type: FK CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.decision
    ADD CONSTRAINT decision_case_fk FOREIGN KEY (case_id) REFERENCES lawfirm."case"(id);


--
-- Name: document document_type_fk; Type: FK CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.document
    ADD CONSTRAINT document_type_fk FOREIGN KEY (type_id) REFERENCES lawfirm.document_types(id);


--
-- Name: judge judge_court_division_fk; Type: FK CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.judge
    ADD CONSTRAINT judge_court_division_fk FOREIGN KEY (court_division_id) REFERENCES lawfirm.court_division(id);


--
-- Name: required_documents_for_trial required_documents_document_fk; Type: FK CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.required_documents_for_trial
    ADD CONSTRAINT required_documents_document_fk FOREIGN KEY (document_id) REFERENCES lawfirm.document(id);


--
-- Name: required_documents_for_trial required_documents_trial_fk; Type: FK CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.required_documents_for_trial
    ADD CONSTRAINT required_documents_trial_fk FOREIGN KEY (trial_id) REFERENCES lawfirm.trial(id);


--
-- Name: ruling ruling_trial_fk; Type: FK CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.ruling
    ADD CONSTRAINT ruling_trial_fk FOREIGN KEY (trial_id) REFERENCES lawfirm.trial(id);


--
-- Name: signature signature_required_document_fk; Type: FK CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.signature
    ADD CONSTRAINT signature_required_document_fk FOREIGN KEY (required_document_id) REFERENCES lawfirm.required_documents_for_trial(id);


--
-- Name: trial trial_case_fk; Type: FK CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.trial
    ADD CONSTRAINT trial_case_fk FOREIGN KEY (case_id) REFERENCES lawfirm."case"(id);


--
-- Name: trial trial_client_fk; Type: FK CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.trial
    ADD CONSTRAINT trial_client_fk FOREIGN KEY (client_id) REFERENCES lawfirm.client(id);


--
-- Name: trial trial_judge_fk; Type: FK CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.trial
    ADD CONSTRAINT trial_judge_fk FOREIGN KEY (judge_id) REFERENCES lawfirm.judge(id);


--
-- Name: trial trial_lawyer_fk; Type: FK CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.trial
    ADD CONSTRAINT trial_lawyer_fk FOREIGN KEY (lawyer_id) REFERENCES lawfirm.lawyer(id);


--
-- Name: trial trial_status_fk; Type: FK CONSTRAINT; Schema: lawfirm; Owner: mr.andi321
--

ALTER TABLE ONLY lawfirm.trial
    ADD CONSTRAINT trial_status_fk FOREIGN KEY (trial_status_id) REFERENCES lawfirm.trial_status(id);


--
-- PostgreSQL database dump complete
--

