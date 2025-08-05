CREATE TABLE donor (
                       id SERIAL PRIMARY KEY,
                       full_name VARCHAR(255),
                       email VARCHAR(255)
);

CREATE TABLE payment (
                         id VARCHAR(255) PRIMARY KEY,
                         payment_date DATE,
                         amount DOUBLE PRECISION,
                         payment_method VARCHAR(255),
                         status VARCHAR(50)
);

CREATE TABLE donation (
                          id SERIAL PRIMARY KEY,
                          donor_id INTEGER NOT NULL REFERENCES donor(id),
                          payment_id VARCHAR(255) NOT NULL REFERENCES payment(id),
                          time TIMESTAMP
);

CREATE TABLE beneficiary (
                             id SERIAL PRIMARY KEY,
                             full_name VARCHAR(255),
                             email VARCHAR(255)
);

CREATE TABLE help (
                      id SERIAL PRIMARY KEY,
                      beneficiary_id BIGINT NOT NULL REFERENCES beneficiary(id),
                      payment_id VARCHAR(255) NOT NULL REFERENCES payment(id),
                      accident_description TEXT
);
