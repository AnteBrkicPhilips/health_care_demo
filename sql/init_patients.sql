CREATE USER dem1 IDENTIFIED BY dem1;
GRANT CONNECT, RESOURCE TO dem1;

-- Drop the table if it exists
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE dem1.patients';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -942 THEN
            RAISE;
        END IF;
END;
/

-- Drop the sequence if it exists
BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE dem1.patients_seq';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2289 THEN
            RAISE;
        END IF;
END;
/

-- Create Sequence
CREATE SEQUENCE dem1.patients_seq START WITH 1 INCREMENT BY 1;

-- Create Table
CREATE TABLE dem1.patients (
    id INT PRIMARY KEY,
    name VARCHAR2(255) NOT NULL,
    age INT NOT NULL,
    diagnosis VARCHAR2(255) NOT NULL
);

-- Create Trigger
CREATE OR REPLACE TRIGGER dem1.patients_before_insert
BEFORE INSERT ON dem1.patients
FOR EACH ROW
BEGIN
    SELECT dem1.patients_seq.NEXTVAL INTO :new.id FROM dual;
END;
/