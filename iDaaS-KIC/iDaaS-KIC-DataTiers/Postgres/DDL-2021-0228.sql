--
-- PostgreSQL database dump
--

-- Dumped from database version 13.1
-- Dumped by pg_dump version 13.2

-- Started on 2021-03-02 14:29:04 CST

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 200 (class 1259 OID 24577)
-- Name: audit; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.audit (
    auditentiremessage text,
    messageprocesseddate character varying(256),
    messageprocessedtime character varying(256),
    processingtype character varying(256),
    industrystd character varying(256),
    component character varying(256),
    messagetrigger character varying(256),
    processname character varying(256),
    auditdetails character varying(256),
    camelid character varying(256),
    exchangeid character varying(256),
    internalmsgid character varying(256),
    bodydata text,
    "auditmessageID" bigint NOT NULL
);


ALTER TABLE public.audit OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 24583)
-- Name: audit_auditmessageID_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."audit_auditmessageID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."audit_auditmessageID_seq" OWNER TO postgres;

--
-- TOC entry 3255 (class 0 OID 0)
-- Dependencies: 201
-- Name: audit_auditmessageID_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."audit_auditmessageID_seq" OWNED BY public.audit."auditmessageID";


--
-- TOC entry 3116 (class 2604 OID 24585)
-- Name: audit auditmessageID; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audit ALTER COLUMN "auditmessageID" SET DEFAULT nextval('public."audit_auditmessageID_seq"'::regclass);


--
-- TOC entry 3119 (class 2606 OID 24594)
-- Name: audit PK_audit; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audit
    ADD CONSTRAINT "PK_audit" PRIMARY KEY ("auditmessageID");


--
-- TOC entry 3117 (class 1259 OID 24592)
-- Name: INDX_audit; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX "INDX_audit" ON public.audit USING btree ("auditmessageID", messageprocesseddate, messageprocessedtime, processingtype, industrystd, component, messagetrigger, processname, auditdetails, camelid, exchangeid, internalmsgid);


-- Completed on 2021-03-02 14:29:05 CST

--
-- PostgreSQL database dump complete
--

