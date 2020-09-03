drop table URL_STORE;
CREATE TABLE url_store

(
 id BIGSERIAL,
 longurl varchar(2000) DEFAULT NULL UNIQUE,
 shorturl varchar(7) DEFAULT NULL UNIQUE,
 dateadded timestamp,
 PRIMARY KEY (id)
);

-- seed the DB with some data
INSERT INTO url_store(longurl, shorturl,dateadded) VALUES ('https://www.baeldung.com/configuration-properties-in-spring-boot', 'short12' , '2020-08-30');
INSERT INTO url_store(longurl, shorturl,dateadded) VALUES ('https://www.baeldung.com/configuration-properties-in-spring-boot-2', 'short13' , '2020-08-30');
INSERT INTO url_store(longurl, shorturl,dateadded) VALUES ('https://www.baeldung.com/configuration-properties-in-spring-boot-3', 'short14' , '2020-08-29');
INSERT INTO url_store(longurl, shorturl,dateadded) VALUES ('https://www.baeldung.com/configuration-properties-in-spring-boot-4', 'short15' , '2020-08-29');
INSERT INTO url_store(longurl, shorturl,dateadded) VALUES ('https://www.baeldung.com/configuration-properties-in-spring-boot-5', 'short16' , '2020-08-28');
INSERT INTO url_store(longurl, shorturl,dateadded) VALUES ('https://www.baeldung.com/configuration-properties-in-spring-boot-6', 'short17' , '2020-08-27');