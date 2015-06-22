CREATE TABLE flights (
	fno number(3) NOT NULL,
	ffrom varchar2(30) NOT NULL,
	fto varchar2(30) NOT NULL,
	cost number(4) NOT NULL
);

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


--DEBUG
INSERT INTO Flights values (123, 'Tel-Aviv', 'Brazil', 1754);
INSERT INTO Flights values (456, 'Tel-Aviv', 'London', 482);
INSERT INTO Flights values (514, 'London', 'Brazil', 750);
INSERT INTO Flights values (793, 'Madrid', 'London', 1548);
INSERT INTO Flights values (137, 'Madrid', 'Buenos Aires', 1486);
INSERT INTO Flights values (943, 'Buenos Aires', 'Tel-Aviv', 2431);
INSERT INTO Flights values (842, 'Chicago', 'Madrid', 749);
INSERT INTO Flights values (543, 'Tel-Aviv', 'Chicago', 2187);