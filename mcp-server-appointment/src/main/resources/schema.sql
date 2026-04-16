CREATE TABLE IF NOT EXISTS appointment_slot (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    date_time TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS appointment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT NOT NULL,
    user_email VARCHAR(255) NOT NULL,
    FOREIGN KEY (appointment_id) REFERENCES appointment_slot(id)
);
