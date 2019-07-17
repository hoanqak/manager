create database manager;
use manager;

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fullname NVARCHAR(40) NOT NULL,
    birth_day DATE,
    email VARCHAR(50) NOT NULL unique,
    address NVARCHAR(100),
    password VARCHAR(32),
    phone_number VARCHAR(15),
    picture VARCHAR(200),
    -- Fulltime, Parttime--  
    kind_of_emp NVARCHAR(50),
    created_time DATETIME,
    updated_time DATETIME,
    department INT DEFAULT 0,
    -- Active, Block--  
	status INT DEFAULT 0,
    -- ADMIN, MEMBER, MANAGER-- 
    role INT DEFAULT 0,
    position INT DEFAULT 0
);

CREATE TABLE check_in_out (
    id INT AUTO_INCREMENT PRIMARY KEY,
    day_check_in DATE,
    start_time DATETIME,
    end_time DATETIME,
    total_time INT,
    id_user INT,
    updated_time DATETIME,
    FOREIGN KEY (id_user)
        REFERENCES user (id)
);

CREATE TABLE leave_application (
    id INT AUTO_INCREMENT PRIMARY KEY,
    start_time DATETIME,
    end_time DATETIME,
    reason NVARCHAR(200) NOT NULL,
    status VARCHAR(20) DEFAULT 'Pending',
    id_user INT,
    created_time DATETIME,
    updated_time DATETIME,
    FOREIGN KEY (id_user)
        REFERENCES user (id)
);


CREATE TABLE message (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_leave_application INT,
    status BIT DEFAULT 0,
    created_time DATETIME,
    FOREIGN KEY (id_leave_application)
        REFERENCES leave_application (id)
);

CREATE TABLE message_for_user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    id_leave_application INT,
    message NVARCHAR(30),
    created_time DATETIME,
    status BIT DEFAULT 0,
    FOREIGN KEY (user_id)
        REFERENCES user (id),
    FOREIGN KEY (id_leave_application)
        REFERENCES leave_application (id)
);
create table message_demo(
	id int auto_increment primary key,
    status BIT default 0,
    title nvarchar(100),
    type int,
    content varchar(200),
    send_from int,
    send_to int,
    time_request datetime,
    id_report int,     
    foreign key (send_from) references user(id),
    foreign key (send_to) references user(id)    
);
 
 create table token(
	id int primary key,
    token varchar(100),
    foreign key(id) references user(id)
 );

create table passwrod_issuing_code (
	id int primary key,
    code varchar(100),
    foreign key(id) references user(id)
);




