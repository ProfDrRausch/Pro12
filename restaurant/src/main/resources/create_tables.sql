-- H2 2.3.232;
;             
CREATE USER IF NOT EXISTS "SA" SALT '095091c95250fec7' HASH 'a81b2bb00ad1a52145e5270edef80f12842b7f574b94b00896b0b40995be7d0e' ADMIN;         
CREATE CACHED TABLE "PUBLIC"."PRODUKT"(
    "NAME" CHARACTER VARYING NOT NULL,
    "KATEGORIE" CHARACTER VARYING,
    "ANZAHL" INTEGER,
    "PREIS" DOUBLE PRECISION
);  
ALTER TABLE "PUBLIC"."PRODUKT" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_1" PRIMARY KEY("NAME");    
-- 12 +/- SELECT COUNT(*) FROM PUBLIC.PRODUKT;
INSERT INTO "PUBLIC"."PRODUKT" VALUES
('Garnelen', 'Basisgericht', 4, 13.5),
('Hueftsteak', 'Basisgericht', 4, 13.0),
('Tofu', 'Basisgericht', 4, 8.5),
('Wienerschnitzel', 'Basisgericht', 4, 10.5),
('Bratkartoffeln', 'Beilage', 4, 1.5),
('Nudeln', 'Beilage', 4, 4.5),
('Pommes', 'Beilage', 4, 2.5),
('Salat', 'Beilage', 4, 2.25),
('Suppe', 'Beilage', 4, 1.5),
('Bier', 'Getraenk', 4, 3.0),
('Wasser', 'Getraenk', 4, 1.5),
('Wein', 'Getraenk', 4, 4.0);          