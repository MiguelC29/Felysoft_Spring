-- Insert para un usuario con rol CUSTOMER
INSERT INTO users (user_name, numIdentification, typeDoc, names, lastNames, address, phoneNumber, email, gender, password, dateRegister, lastModification, eliminated, role) VALUES ('lmarquez', 1234567890, 'CC', 'Luis', 'Márquez', 'Calle Principal 123', 3001234567, 'cliente@mail.com', 'MASCULINO', '$2a$10$iZB7HNKo8Sg9ERfmYEQCl.jNJiUFaJUhS.9dAat.uNDVyvn627rai', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'CUSTOMER');

-- Insert para un usuario con rol ADMINISTRATOR
INSERT INTO users (user_name, numIdentification, typeDoc, names, lastNames, address, phoneNumber, email, gender, password, dateRegister, lastModification, eliminated, role) VALUES ('mhernandez', 9876543210, 'TI', 'Menganito', 'Hernández', 'Avenida Principal 456', 3019876543, 'admin@mail.com', 'MASCULINO', '$2a$10$N7uH4BW51DAPzOeK/Ju/Fu1BW68dV59QpLZ7jWwwA2uKhmyIQD3TS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'ADMINISTRATOR');

-- Insert para un usuario con rol FINANCIAL_MANAGER
INSERT INTO users (user_name, numIdentification, typeDoc, names, lastNames, address, phoneNumber, email, gender, password, dateRegister, lastModification, eliminated, role) VALUES ('juanito', 1031805187, 'CC', 'Juan', 'Pineda', 'Calle 134 #89', 3222275429, 'financiero@mail.com', 'MASCULINO', '$2a$10$iZB7HNKo8Sg9ERfmYEQCl.jNJiUFaJUhS.9dAat.uNDVyvn627rai', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'FINANCIAL_MANAGER');

-- Insert para un usuario con rol INVENTORY_MANAGER
INSERT INTO users (user_name, numIdentification, typeDoc, names, lastNames, address, phoneNumber, email, gender, password, dateRegister, lastModification, eliminated, role) VALUES ('ramiro', 19063224, 'CC', 'Ramiro', 'Martinez', 'Calle 99a #8-0', 3105793409, 'inventario@mail.com', 'MASCULINO', '$2a$10$iZB7HNKo8Sg9ERfmYEQCl.jNJiUFaJUhS.9dAat.uNDVyvn627rai', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'INVENTORY_MANAGER');

-- Insert para un usuario con rol SALESPERSON
INSERT INTO users (user_name, numIdentification, typeDoc, names, lastNames, address, phoneNumber, email, gender, password, dateRegister, lastModification, eliminated, role) VALUES ('rLopez', 1047987456, 'CC', 'Roberto', 'Lopez', 'Carrera 15 #45-06', 312465876, 'vendedor@mail.com', 'MASCULINO', '$2a$10$N7uH4BW51DAPzOeK/Ju/Fu1BW68dV59QpLZ7jWwwA2uKhmyIQD3TS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'SALESPERSON');

/*
INSERT INTO categories (eliminated, name) VALUES (0, 'GALLETAS');
INSERT INTO categories (eliminated, name) VALUES (0, 'DETERGENTES');
INSERT INTO categories (eliminated, name) VALUES (0, 'DULCES');

INSERT INTO providers (eliminated, phoneNumber, email, name, nit) VALUES (0, 22323231, 'oreo@gmail.com', 'OREO', '1212121212');
INSERT INTO providers (eliminated, phoneNumber, email, name, nit) VALUES (0, 3212121323, 'correo@gmail.com', 'EMPRESA', '213432-21');
INSERT INTO providers (eliminated, phoneNumber, email, name, nit) VALUES (0, 3216549874, 'prueba.1@gmail.com', 'PRUEBARECICLARR', '1234789-20');

INSERT INTO products (eliminated, expiryDate, salePrice, fkIdCategory, fkIdProvider, brand, name, typeImg, nameImg, image) VALUES (0, '2024-04-04', 2000.00, 1, 1, 'OREO', 'GALLETAS OREO', '', '', '');
 */