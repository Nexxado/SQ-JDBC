
--------------------- CREATE TABLE METHOD -----------------------
CREATE TABLE flights (
	fno number(3) NOT NULL,
	ffrom varchar2(30) NOT NULL,
	fto varchar2(30) NOT NULL,
	cost number(4) NOT NULL,
	primary key (fno)
);
----------------------------------------------------------------


--------------------- ADD TRIGGER METHOD -----------------------
CREATE OR REPLACE TRIGGER flights_trig
BEFORE INSERT ON flights
FOR EACH ROW
DECLARE maxfno NUMBER;
BEGIN
  IF :NEW.fno < 1000 THEN 
    SELECT MAX(fno)+1 INTO maxfno FROM Flights;
    IF maxfno IS NOT NULL THEN
    :NEW.fno := maxfno;
	END IF;
  END IF;
END;
----------------------------------------------------------------

----------------------- INESERT METHOD -------------------------
INSERT INTO flights values (< fno >, < ffrom >, < fto >, < cost >);
----------------------------------------------------------------


--------------------- CHEAPEST FLIGHT METHOD -------------------

--every trip check if found destination, if so, save minimum value

CREATE OR REPLACE VIEW trip1 AS
SELECT fto AS ffrom, cost AS total_cost
FROM flights
WHERE ffrom='source'
ORDER BY total_cost ASC;

CREATE OR REPLACE VIEW trip2 AS
SELECT f.fto AS ffrom, f.cost + tp.total_cost AS total_cost
FROM flights f JOIN trip1 tp ON f.ffrom = tp.ffrom
ORDER BY total_cost ASC;

CREATE OR REPLACE VIEW trip3 AS
SELECT f.fto AS ffrom, f.cost + tp.total_cost AS total_cost
FROM flights f JOIN trip2 tp ON f.ffrom = tp.ffrom
ORDER BY total_cost ASC;

----------------------------------------------------------------



--DEBUG
INSERT INTO Flights values (123, 'Tel-Aviv', 'Brazil', 1754);
INSERT INTO Flights values (456, 'Tel-Aviv', 'London', 482);
INSERT INTO Flights values (514, 'London', 'Brazil', 750);
INSERT INTO Flights values (793, 'Madrid', 'London', 1548);
INSERT INTO Flights values (137, 'Madrid', 'Buenos Aires', 1486);
INSERT INTO Flights values (943, 'Buenos Aires', 'Tel-Aviv', 2431);
INSERT INTO Flights values (842, 'Chicago', 'Madrid', 749);
INSERT INTO Flights values (543, 'Tel-Aviv', 'Chicago', 2187);




