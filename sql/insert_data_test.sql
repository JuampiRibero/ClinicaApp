-- Insertamos pacientes
INSERT INTO	paciente (eliminado, nombre, apellido, dni, fecha_nacimiento)
VALUES
(FALSE, 'María', 'Gómez', '35111222', '1990-05-14'),
(FALSE, 'Juan', 'Pérez', '34455987', '1989-09-20'),
(FALSE, 'Lucía', 'Martínez', '39100555', '1995-11-02');

-- Insertamos historias clínicas (1→1)
INSERT INTO historia_clinica (eliminado, nro_historia, grupo_sanguineo, antecedentes, medicacion_actual, observaciones, paciente_id)
VALUES
(FALSE, 'HC-0001', 'A+', 'Hipertensión leve', 'Losartán 50mg', 'Control mensual', 1),
(FALSE, 'HC-0002', 'O-', 'Asma bronquial', 'Salbutamol', 'Evitar humedad', 2),
(FALSE, 'HC-0003', 'B+', 'Ninguno', 'Ninguno', 'Paciente en buen estado', 3);