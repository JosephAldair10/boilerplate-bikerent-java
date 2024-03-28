
CREATE TABLE IF NOT EXISTS bikes (
    id int PRIMARY KEY NOT NULL,
    name varchar(255),
    type varchar(255),
    body_size int,
    max_load int,
    rate int,
    description varchar(255),
    ratings decimal (2,1)
);

CREATE TABLE IF NOT EXISTS bike_images (
    id int PRIMARY KEY NOT NULL,
    url varchar(255),
    bike_id int,
    FOREIGN KEY (bike_id) REFERENCES BIKES(id)
);

CREATE TABLE IF NOT EXISTS customer (
    id int PRIMARY KEY NOT NULL,
    name varchar(255)
);

CREATE TABLE IF NOT EXISTS rent (
    id int PRIMARY KEY AUTO_INCREMENT,
    startdate date,
    enddate date,
    customer_id int,
    bike_id int,
    days int,
    total decimal (10,2),
    fee decimal (10,2),
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (bike_id) REFERENCES bikes(id)
);
