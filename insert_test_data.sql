-- =====================================================
-- SCRIPT DE DATOS DE PRUEBA PARA EXTINGUIDOR APP
-- =====================================================
-- Este script inserta datos de prueba para probar el sistema completo
-- Las contraseñas están hasheadas con BCrypt (password123 = $2a$10$...)
-- =====================================================

-- Limpiar datos existentes (opcional, descomentar si es necesario)
-- SET FOREIGN_KEY_CHECKS = 0;
-- TRUNCATE TABLE comments;
-- TRUNCATE TABLE checkins;
-- TRUNCATE TABLE report_vehicles;
-- TRUNCATE TABLE report_tools;
-- TRUNCATE TABLE report_workers;
-- TRUNCATE TABLE reports;
-- TRUNCATE TABLE parte_articulos;
-- TRUNCATE TABLE parte_documentos;
-- TRUNCATE TABLE parte_comentarios;
-- TRUNCATE TABLE partes;
-- TRUNCATE TABLE facturacion;
-- TRUNCATE TABLE route_materials;
-- TRUNCATE TABLE route_users;
-- TRUNCATE TABLE routes;
-- TRUNCATE TABLE ruta_n;
-- TRUNCATE TABLE customer_documents;
-- TRUNCATE TABLE customer_contract_systems;
-- TRUNCATE TABLE customers;
-- TRUNCATE TABLE articles;
-- TRUNCATE TABLE materials;
-- TRUNCATE TABLE vehicles;
-- TRUNCATE TABLE zones;
-- TRUNCATE TABLE alerts;
-- TRUNCATE TABLE parte_templates;
-- TRUNCATE TABLE users;
-- SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- 1. USUARIOS
-- =====================================================
-- NOTA IMPORTANTE: Las contraseñas están hasheadas con BCrypt
-- Contraseña para todos: "password123"
-- Si las contraseñas no funcionan, puedes:
-- 1. Usar el usuario admin creado por DataInitializer (admin@extinguidor.com / Admin123!)
-- 2. Cambiar las contraseñas desde la aplicación usando el endpoint de actualización
-- 3. Regenerar los hashes BCrypt con strength 10
--
-- Hash BCrypt para "password123" (puede variar por el salt):
-- $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
--
-- Para generar un nuevo hash, puedes usar:
-- BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
-- String hash = encoder.encode("password123");
INSERT IGNORE INTO users (name, code, email, phone, password, role, photo, activo, created_date, updated_date) VALUES
('Juan Pérez', 'WORKER001', 'juan.perez@extinguidor.com', '+34600000002', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'WORKER', 'https://ionicframework.com/docs/img/demos/avatar.svg', true, NOW(), NOW()),
('María García', 'WORKER002', 'maria.garcia@extinguidor.com', '+34600000003', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'WORKER', 'https://ionicframework.com/docs/img/demos/avatar.svg', true, NOW(), NOW()),
('Carlos López', 'WORKER003', 'carlos.lopez@extinguidor.com', '+34600000004', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'WORKER', 'https://ionicframework.com/docs/img/demos/avatar.svg', true, NOW(), NOW()),
('Ana Martínez', 'WORKER004', 'ana.martinez@extinguidor.com', '+34600000005', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'WORKER', 'https://ionicframework.com/docs/img/demos/avatar.svg', true, NOW(), NOW()),
('Pedro Sánchez', 'WORKER005', 'pedro.sanchez@extinguidor.com', '+34600000006', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'WORKER', 'https://ionicframework.com/docs/img/demos/avatar.svg', true, NOW(), NOW());

-- =====================================================
-- 2. ZONAS
-- =====================================================
INSERT IGNORE INTO zones (name, code, codezip, created_date, updated_date) VALUES
('Madrid Centro', 'MAD-CENTRO', 28001, NOW(), NOW()),
('Madrid Norte', 'MAD-NORTE', 28020, NOW(), NOW()),
('Madrid Sur', 'MAD-SUR', 28041, NOW(), NOW()),
('Barcelona Centro', 'BCN-CENTRO', 08001, NOW(), NOW()),
('Valencia', 'VAL', 46001, NOW(), NOW()),
('Sevilla', 'SEV', 41001, NOW(), NOW()),
('Bilbao', 'BIL', 48001, NOW(), NOW());

-- =====================================================
-- 3. CLIENTES
-- =====================================================
INSERT IGNORE INTO customers (name, email, nifCif, address, phone, contactName, code, active, zone_id, description, gestiona, photo, start_date, end_date, type, average_time, delegation, revision_frequency, rate, mi, tipo, total, created_date, updated_date)
SELECT 'Empresa ABC S.L.', 'contacto@empresaabc.com', 'B12345678', 'Calle Mayor 123, Madrid', '+34911234567', 'Roberto Fernández', 'CLI001', true,
    (SELECT id FROM zones WHERE code = 'MAD-CENTRO' LIMIT 1),
    'Cliente corporativo con múltiples instalaciones', 'Gestor 1', 'foto.jpg', '2024-01-15', '2025-12-31', 'F', 120, 'Madrid', 'T', 'T', 5, 'Normal', 15000.00, NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM zones WHERE code = 'MAD-CENTRO')
UNION ALL
SELECT 'Comercial XYZ S.A.', 'info@comercialxyz.com', 'A87654321', 'Avenida Diagonal 456, Barcelona', '+34932123456', 'Laura Torres', 'CLI002', true,
    (SELECT id FROM zones WHERE code = 'BCN-CENTRO' LIMIT 1),
    'Cliente con sistema de extintores', 'Gestor 2', 'foto.jpg', '2024-03-01', '2025-12-31', 'E', 90, 'Barcelona', 'S', 'S', 3, 'Normal', 8500.00, NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM zones WHERE code = 'BCN-CENTRO')
UNION ALL
SELECT 'Industrias DEF S.L.', 'contacto@industriasdef.com', 'B11223344', 'Polígono Industrial Norte, Valencia', '+34961234567', 'Miguel Ángel Ruiz', 'CLI003', true,
    (SELECT id FROM zones WHERE code = 'VAL' LIMIT 1),
    'Cliente industrial con mantenimiento mensual', 'Gestor 1', 'foto.jpg', '2024-02-10', '2025-12-31', 'R', 180, 'Valencia', 'T', 'T', 8, 'Normal', 22000.00, NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM zones WHERE code = 'VAL')
UNION ALL
SELECT 'Centro Comercial Plaza', 'admin@ccplaza.com', 'A55667788', 'Calle Comercio 789, Sevilla', '+34954123456', 'Carmen Jiménez', 'CLI004', true,
    (SELECT id FROM zones WHERE code = 'SEV' LIMIT 1),
    'Centro comercial con sistema completo', 'Gestor 3', 'foto.jpg', '2024-01-20', '2025-12-31', 'C', 240, 'Sevilla', 'A', 'A', 12, 'Normal', 35000.00, NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM zones WHERE code = 'SEV')
UNION ALL
SELECT 'Oficinas Global S.A.', 'info@oficinasglobal.com', 'A99887766', 'Paseo de la Castellana 321, Madrid', '+34911234568', 'David Moreno', 'CLI005', true,
    (SELECT id FROM zones WHERE code = 'MAD-CENTRO' LIMIT 1),
    'Edificio de oficinas corporativas', 'Gestor 1', 'foto.jpg', '2024-04-01', '2025-12-31', 'F', 150, 'Madrid', 'T', 'T', 6, 'Normal', 18000.00, NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM zones WHERE code = 'MAD-CENTRO')
UNION ALL
SELECT 'Hotel Residencia', 'recepcion@hotelresidencia.com', 'B44556677', 'Avenida del Mar 654, Bilbao', '+34944123456', 'Patricia Vega', 'CLI006', true,
    (SELECT id FROM zones WHERE code = 'BIL' LIMIT 1),
    'Hotel con sistema de seguridad', 'Gestor 2', 'foto.jpg', '2024-03-15', '2025-12-31', 'E', 200, 'Bilbao', 'S', 'S', 10, 'Normal', 28000.00, NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM zones WHERE code = 'BIL');

-- Insertar sistemas de contrato para clientes
INSERT IGNORE INTO customer_contract_systems (customer_id, contract_system) 
SELECT id, 'Extintores' FROM customers WHERE code = 'CLI001'
UNION ALL SELECT id, 'Detección de Incendios' FROM customers WHERE code = 'CLI001'
UNION ALL SELECT id, 'CCTV' FROM customers WHERE code = 'CLI001'
UNION ALL SELECT id, 'Extintores' FROM customers WHERE code = 'CLI002'
UNION ALL SELECT id, 'Robo' FROM customers WHERE code = 'CLI002'
UNION ALL SELECT id, 'Extintores' FROM customers WHERE code = 'CLI003'
UNION ALL SELECT id, 'Detección de Incendios' FROM customers WHERE code = 'CLI003'
UNION ALL SELECT id, 'Pasiva' FROM customers WHERE code = 'CLI003'
UNION ALL SELECT id, 'Extintores' FROM customers WHERE code = 'CLI004'
UNION ALL SELECT id, 'Detección de Incendios' FROM customers WHERE code = 'CLI004'
UNION ALL SELECT id, 'CCTV' FROM customers WHERE code = 'CLI004'
UNION ALL SELECT id, 'Robo' FROM customers WHERE code = 'CLI004'
UNION ALL SELECT id, 'Extintores' FROM customers WHERE code = 'CLI005'
UNION ALL SELECT id, 'Detección de Incendios' FROM customers WHERE code = 'CLI005'
UNION ALL SELECT id, 'Extintores' FROM customers WHERE code = 'CLI006'
UNION ALL SELECT id, 'CCTV' FROM customers WHERE code = 'CLI006'
UNION ALL SELECT id, 'Robo' FROM customers WHERE code = 'CLI006';

-- Insertar documentos de clientes
INSERT IGNORE INTO customer_documents (customer_id, name, url)
SELECT id, 'Contrato Principal', 'https://example.com/docs/cliente1/contrato.pdf' FROM customers WHERE code = 'CLI001'
UNION ALL SELECT id, 'Certificado Seguridad', 'https://example.com/docs/cliente1/certificado.pdf' FROM customers WHERE code = 'CLI001'
UNION ALL SELECT id, 'Contrato Servicio', 'https://example.com/docs/cliente2/contrato.pdf' FROM customers WHERE code = 'CLI002'
UNION ALL SELECT id, 'Acuerdo Mantenimiento', 'https://example.com/docs/cliente3/acuerdo.pdf' FROM customers WHERE code = 'CLI003'
UNION ALL SELECT id, 'Contrato Completo', 'https://example.com/docs/cliente4/contrato.pdf' FROM customers WHERE code = 'CLI004'
UNION ALL SELECT id, 'Documentación Legal', 'https://example.com/docs/cliente5/documentos.pdf' FROM customers WHERE code = 'CLI005';

-- =====================================================
-- 4. VEHÍCULOS
-- =====================================================
INSERT IGNORE INTO vehicles (modelo, brand, matricula, fuel, type, photo, created_date, updated_date) VALUES
('Transit', 'Ford', '1234ABC', 'Diesel', 'Furgon', 'auto.jpg', NOW(), NOW()),
('Sprinter', 'Mercedes', '5678DEF', 'Diesel', 'Furgon', 'auto.jpg', NOW(), NOW()),
('Master', 'Renault', '9012GHI', 'Diesel', 'Furgon', 'auto.jpg', NOW(), NOW()),
('Caddy', 'Volkswagen', '3456JKL', 'Diesel', 'Furgon', 'auto.jpg', NOW(), NOW()),
('Focus', 'Ford', '7890MNO', 'Gasolina', 'Turismo', 'auto.jpg', NOW(), NOW()),
('Astra', 'Opel', '2345PQR', 'Diesel', 'Turismo', 'auto.jpg', NOW(), NOW());

-- =====================================================
-- 5. MATERIALES
-- =====================================================
INSERT IGNORE INTO materials (name, code, description, type, created_date, updated_date) VALUES
('Extintor Polvo ABC 6kg', 'MAT001', 'Extintor de polvo ABC de 6 kilogramos', 'Extintor', NOW(), NOW()),
('Extintor CO2 5kg', 'MAT002', 'Extintor de dióxido de carbono de 5 kilogramos', 'Extintor', NOW(), NOW()),
('Manguera Antiincendios', 'MAT003', 'Manguera para sistemas de extinción', 'Herramienta', NOW(), NOW()),
('Detector de Humo', 'MAT004', 'Detector de humo para sistema de alarma', 'Detección', NOW(), NOW()),
('Batería de Emergencia', 'MAT005', 'Batería de respaldo para sistemas de seguridad', 'Componente', NOW(), NOW()),
('Cámara CCTV', 'MAT006', 'Cámara de seguridad para vigilancia', 'CCTV', NOW(), NOW()),
('Llave de Paso', 'MAT007', 'Válvula de control para sistemas de agua', 'Herramienta', NOW(), NOW()),
('Manómetro', 'MAT008', 'Medidor de presión para extintores', 'Herramienta', NOW(), NOW());

-- =====================================================
-- 6. ARTÍCULOS
-- =====================================================
INSERT IGNORE INTO articles (cantidad, codigo, grupo, familia, descripcion_articulo, precio_venta, eliminado, created_date, updated_date) VALUES
(50, 'ART001', 'Extintores', 'Portátiles', 'Extintor Polvo ABC 6kg', 45.50, false, NOW(), NOW()),
(30, 'ART002', 'Extintores', 'Portátiles', 'Extintor CO2 5kg', 65.00, false, NOW(), NOW()),
(20, 'ART003', 'Extintores', 'Ruedas', 'Extintor Polvo ABC 25kg', 180.00, false, NOW(), NOW()),
(15, 'ART004', 'Detección', 'Detectores', 'Detector de Humo Óptico', 35.75, false, NOW(), NOW()),
(25, 'ART005', 'Detección', 'Sirenas', 'Sirena de Alarma 12V', 28.50, false, NOW(), NOW()),
(40, 'ART006', 'CCTV', 'Cámaras', 'Cámara IP Exterior', 120.00, false, NOW(), NOW()),
(10, 'ART007', 'CCTV', 'Grabadores', 'Grabador DVR 8 Canales', 350.00, false, NOW(), NOW()),
(35, 'ART008', 'Herramientas', 'Mangueras', 'Manguera Antiincendios 25m', 85.00, false, NOW(), NOW());

-- =====================================================
-- 7. RUTAS NOMINALES (RutaN)
-- =====================================================
INSERT IGNORE INTO ruta_n (name, created_date, updated_date) VALUES
('Ruta Madrid Centro', NOW(), NOW()),
('Ruta Madrid Norte', NOW(), NOW()),
('Ruta Barcelona', NOW(), NOW()),
('Ruta Valencia', NOW(), NOW()),
('Ruta Sevilla', NOW(), NOW()),
('Ruta Bilbao', NOW(), NOW());

-- =====================================================
-- 8. RUTAS (Route)
-- =====================================================
INSERT IGNORE INTO routes (encargado_id, ruta_n_id, date, state, vehicle_id, comentarios, eliminado, created_date, updated_date)
SELECT 
    (SELECT id FROM users WHERE code = 'WORKER001' LIMIT 1),
    (SELECT id FROM ruta_n WHERE name = 'Ruta Madrid Centro' LIMIT 1),
    '2024-12-01', 'Pendiente',
    (SELECT id FROM vehicles WHERE matricula = '1234ABC' LIMIT 1),
    'Ruta programada para revisión mensual', false, NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM users WHERE code = 'WORKER001')
  AND EXISTS (SELECT 1 FROM ruta_n WHERE name = 'Ruta Madrid Centro')
  AND EXISTS (SELECT 1 FROM vehicles WHERE matricula = '1234ABC')
UNION ALL
SELECT 
    (SELECT id FROM users WHERE code = 'WORKER002' LIMIT 1),
    (SELECT id FROM ruta_n WHERE name = 'Ruta Madrid Norte' LIMIT 1),
    '2024-12-02', 'EnProceso',
    (SELECT id FROM vehicles WHERE matricula = '5678DEF' LIMIT 1),
    'En curso - revisión de extintores', false, NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM users WHERE code = 'WORKER002')
  AND EXISTS (SELECT 1 FROM ruta_n WHERE name = 'Ruta Madrid Norte')
  AND EXISTS (SELECT 1 FROM vehicles WHERE matricula = '5678DEF')
UNION ALL
SELECT 
    (SELECT id FROM users WHERE code = 'WORKER003' LIMIT 1),
    (SELECT id FROM ruta_n WHERE name = 'Ruta Barcelona' LIMIT 1),
    '2024-12-03', 'Pendiente',
    (SELECT id FROM vehicles WHERE matricula = '9012GHI' LIMIT 1),
    'Ruta pendiente de asignación', false, NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM users WHERE code = 'WORKER003')
  AND EXISTS (SELECT 1 FROM ruta_n WHERE name = 'Ruta Barcelona')
  AND EXISTS (SELECT 1 FROM vehicles WHERE matricula = '9012GHI')
UNION ALL
SELECT 
    (SELECT id FROM users WHERE code = 'WORKER004' LIMIT 1),
    (SELECT id FROM ruta_n WHERE name = 'Ruta Valencia' LIMIT 1),
    '2024-12-04', 'Finalizado',
    (SELECT id FROM vehicles WHERE matricula = '3456JKL' LIMIT 1),
    'Ruta completada exitosamente', false, NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM users WHERE code = 'WORKER004')
  AND EXISTS (SELECT 1 FROM ruta_n WHERE name = 'Ruta Valencia')
  AND EXISTS (SELECT 1 FROM vehicles WHERE matricula = '3456JKL')
UNION ALL
SELECT 
    (SELECT id FROM users WHERE code = 'WORKER001' LIMIT 1),
    (SELECT id FROM ruta_n WHERE name = 'Ruta Sevilla' LIMIT 1),
    '2024-12-05', 'Pendiente',
    (SELECT id FROM vehicles WHERE matricula = '1234ABC' LIMIT 1),
    'Programada para próxima semana', false, NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM users WHERE code = 'WORKER001')
  AND EXISTS (SELECT 1 FROM ruta_n WHERE name = 'Ruta Sevilla')
  AND EXISTS (SELECT 1 FROM vehicles WHERE matricula = '1234ABC');

-- Insertar usuarios en rutas
INSERT IGNORE INTO route_users (route_id, user_id)
SELECT r.id, u.id
FROM routes r
CROSS JOIN users u
WHERE (r.date = '2024-12-01' AND u.code IN ('WORKER001', 'WORKER002'))
   OR (r.date = '2024-12-02' AND u.code IN ('WORKER002', 'WORKER003'))
   OR (r.date = '2024-12-03' AND u.code IN ('WORKER003', 'WORKER004'))
   OR (r.date = '2024-12-04' AND u.code IN ('WORKER004', 'WORKER005'))
   OR (r.date = '2024-12-05' AND u.code IN ('WORKER001', 'WORKER005'));

-- Insertar materiales en rutas
INSERT IGNORE INTO route_materials (route_id, material_id)
SELECT r.id, m.id
FROM routes r
CROSS JOIN materials m
WHERE (r.date = '2024-12-01' AND m.code IN ('MAT001', 'MAT002', 'MAT003'))
   OR (r.date = '2024-12-02' AND m.code IN ('MAT001', 'MAT004'))
   OR (r.date = '2024-12-03' AND m.code IN ('MAT002', 'MAT005'))
   OR (r.date = '2024-12-04' AND m.code IN ('MAT001', 'MAT003', 'MAT007'))
   OR (r.date = '2024-12-05' AND m.code IN ('MAT001', 'MAT008'));

-- =====================================================
-- 9. PARTES
-- =====================================================
INSERT IGNORE INTO partes (title, description, date, customer_id, address, state, type, categoria, asignado, eliminado, periodico, frequency, end_date, coordination_method, gestiona, facturacion, ruta_id, worker_id, finalizado_time, created_date, updated_date)
SELECT 
    'Mantenimiento Extintores - Empresa ABC',
    'Revisión y mantenimiento de extintores en instalaciones principales',
    '2024-12-01',
    (SELECT id FROM customers WHERE code = 'CLI001' LIMIT 1),
    'Calle Mayor 123, Madrid',
    'Pendiente', 'Mantenimiento', 'Extintores', false, false, true, 'Mensual', '2025-12-01', 'COORDINAR_HORARIOS', 1, 450.00,
    (SELECT id FROM routes WHERE date = '2024-12-01' LIMIT 1),
    (SELECT id FROM users WHERE code = 'WORKER001' LIMIT 1),
    NULL, NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM customers WHERE code = 'CLI001')
UNION ALL
SELECT 
    'Instalación Sistema Detección - Comercial XYZ',
    'Instalación de nuevo sistema de detección de incendios',
    '2024-12-02',
    (SELECT id FROM customers WHERE code = 'CLI002' LIMIT 1),
    'Avenida Diagonal 456, Barcelona',
    'EnProceso', 'Obra', 'Incendio', true, false, false, NULL, NULL, 'LLAMAR_ANTES', 2, 2500.00,
    (SELECT id FROM routes WHERE date = '2024-12-02' LIMIT 1),
    (SELECT id FROM users WHERE code = 'WORKER002' LIMIT 1),
    NULL, NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM customers WHERE code = 'CLI002')
UNION ALL
SELECT 
    'Reparation Extintor - Industrias DEF',
    'Reparación de extintor dañado en nave principal',
    '2024-12-03',
    (SELECT id FROM customers WHERE code = 'CLI003' LIMIT 1),
    'Polígono Industrial Norte, Valencia',
    'Finalizado', 'Correctivo', 'Extintores', true, false, false, NULL, NULL, 'COORDINAR_HORARIOS', 1, 180.00,
    (SELECT id FROM routes WHERE date = '2024-12-03' LIMIT 1),
    (SELECT id FROM users WHERE code = 'WORKER003' LIMIT 1),
    NOW(), NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM customers WHERE code = 'CLI003')
UNION ALL
SELECT 
    'Visita Técnica - Centro Comercial Plaza',
    'Visita técnica para evaluación de sistemas de seguridad',
    '2024-12-04',
    (SELECT id FROM customers WHERE code = 'CLI004' LIMIT 1),
    'Calle Comercio 789, Sevilla',
    'Pendiente', 'Visitas', 'CCTV', false, false, false, NULL, NULL, 'COORDINAR_EMAIL', 3, 0.00,
    (SELECT id FROM routes WHERE date = '2024-12-04' LIMIT 1),
    NULL,
    NULL, NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM customers WHERE code = 'CLI004')
UNION ALL
SELECT 
    'Mantenimiento Periódico - Oficinas Global',
    'Mantenimiento trimestral de sistemas de seguridad',
    '2024-12-05',
    (SELECT id FROM customers WHERE code = 'CLI005' LIMIT 1),
    'Paseo de la Castellana 321, Madrid',
    'EnProceso', 'Mantenimiento', 'Incendio', true, false, true, 'Trimestral', '2025-12-05', 'COORDINAR_HORARIOS', 1, 650.00,
    (SELECT id FROM routes WHERE date = '2024-12-01' LIMIT 1),
    (SELECT id FROM users WHERE code = 'WORKER001' LIMIT 1),
    NULL, NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM customers WHERE code = 'CLI005')
UNION ALL
SELECT 
    'Instalación CCTV - Hotel Residencia',
    'Instalación de sistema de cámaras de seguridad',
    '2024-12-06',
    (SELECT id FROM customers WHERE code = 'CLI006' LIMIT 1),
    'Avenida del Mar 654, Bilbao',
    'Pendiente', 'Obra', 'CCTV', false, false, false, NULL, NULL, 'LLAMAR_ANTES', 2, 3200.00,
    (SELECT id FROM routes WHERE date = '2024-12-05' LIMIT 1),
    NULL,
    NULL, NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM customers WHERE code = 'CLI006');

-- Insertar comentarios en partes
INSERT IGNORE INTO parte_comentarios (parte_id, texto, fecha, usuario_id)
SELECT p.id, 'Cliente solicita revisión en horario de mañana', NOW(), (SELECT id FROM users WHERE code = 'WORKER001' LIMIT 1)
FROM partes p WHERE p.title = 'Mantenimiento Extintores - Empresa ABC' AND p.date = '2024-12-01'
UNION ALL
SELECT p.id, 'Materiales entregados, iniciando instalación', NOW(), (SELECT id FROM users WHERE code = 'WORKER002' LIMIT 1)
FROM partes p WHERE p.title = 'Instalación Sistema Detección - Comercial XYZ' AND p.date = '2024-12-02'
UNION ALL
SELECT p.id, 'Extintor reparado y recargado correctamente', NOW(), (SELECT id FROM users WHERE code = 'WORKER003' LIMIT 1)
FROM partes p WHERE p.title = 'Reparation Extintor - Industrias DEF' AND p.date = '2024-12-03'
UNION ALL
SELECT p.id, 'Revisión en curso, todo funcionando correctamente', NOW(), (SELECT id FROM users WHERE code = 'WORKER001' LIMIT 1)
FROM partes p WHERE p.title = 'Mantenimiento Periódico - Oficinas Global' AND p.date = '2024-12-05';

-- Insertar documentos en partes
INSERT IGNORE INTO parte_documentos (parte_id, nombre, url, tipo, fecha)
SELECT p.id, 'Informe Mantenimiento', 'https://example.com/docs/parte1/informe.pdf', 'PDF', NOW()
FROM partes p WHERE p.title = 'Mantenimiento Extintores - Empresa ABC' AND p.date = '2024-12-01'
UNION ALL
SELECT p.id, 'Fotos Instalación', 'https://example.com/docs/parte2/fotos.zip', 'ZIP', NOW()
FROM partes p WHERE p.title = 'Instalación Sistema Detección - Comercial XYZ' AND p.date = '2024-12-02'
UNION ALL
SELECT p.id, 'Certificado Reparación', 'https://example.com/docs/parte3/certificado.pdf', 'PDF', NOW()
FROM partes p WHERE p.title = 'Reparation Extintor - Industrias DEF' AND p.date = '2024-12-03'
UNION ALL
SELECT p.id, 'Checklist Revisión', 'https://example.com/docs/parte5/checklist.pdf', 'PDF', NOW()
FROM partes p WHERE p.title = 'Mantenimiento Periódico - Oficinas Global' AND p.date = '2024-12-05';

-- Insertar artículos en partes
INSERT IGNORE INTO parte_articulos (parte_id, cantidad, codigo, grupo, familia, descripcion_articulo, precio_venta)
SELECT p.id, 2, 'ART001', 'Extintores', 'Portátiles', 'Extintor Polvo ABC 6kg', 45.50
FROM partes p WHERE p.title = 'Mantenimiento Extintores - Empresa ABC' AND p.date = '2024-12-01'
UNION ALL
SELECT p.id, 1, 'ART002', 'Extintores', 'Portátiles', 'Extintor CO2 5kg', 65.00
FROM partes p WHERE p.title = 'Mantenimiento Extintores - Empresa ABC' AND p.date = '2024-12-01'
UNION ALL
SELECT p.id, 5, 'ART004', 'Detección', 'Detectores', 'Detector de Humo Óptico', 35.75
FROM partes p WHERE p.title = 'Instalación Sistema Detección - Comercial XYZ' AND p.date = '2024-12-02'
UNION ALL
SELECT p.id, 2, 'ART005', 'Detección', 'Sirenas', 'Sirena de Alarma 12V', 28.50
FROM partes p WHERE p.title = 'Instalación Sistema Detección - Comercial XYZ' AND p.date = '2024-12-02'
UNION ALL
SELECT p.id, 1, 'ART001', 'Extintores', 'Portátiles', 'Extintor Polvo ABC 6kg', 45.50
FROM partes p WHERE p.title = 'Reparation Extintor - Industrias DEF' AND p.date = '2024-12-03'
UNION ALL
SELECT p.id, 3, 'ART004', 'Detección', 'Detectores', 'Detector de Humo Óptico', 35.75
FROM partes p WHERE p.title = 'Mantenimiento Periódico - Oficinas Global' AND p.date = '2024-12-05'
UNION ALL
SELECT p.id, 4, 'ART006', 'CCTV', 'Cámaras', 'Cámara IP Exterior', 120.00
FROM partes p WHERE p.title = 'Instalación CCTV - Hotel Residencia' AND p.date = '2024-12-06'
UNION ALL
SELECT p.id, 1, 'ART007', 'CCTV', 'Grabadores', 'Grabador DVR 8 Canales', 350.00
FROM partes p WHERE p.title = 'Instalación CCTV - Hotel Residencia' AND p.date = '2024-12-06';

-- =====================================================
-- 10. COMENTARIOS (Comments)
-- =====================================================
INSERT IGNORE INTO comments (comentario, date, parte_id, lat, lgn, created_date, updated_date)
SELECT 'Extintores revisados correctamente. Todo en orden.', NOW(), p.id, 40.4168, -3.7038, NOW(), NOW()
FROM partes p WHERE p.title = 'Mantenimiento Extintores - Empresa ABC' AND p.date = '2024-12-01'
UNION ALL
SELECT 'Instalación en progreso. Materiales suficientes.', NOW(), p.id, 41.3851, 2.1734, NOW(), NOW()
FROM partes p WHERE p.title = 'Instalación Sistema Detección - Comercial XYZ' AND p.date = '2024-12-02'
UNION ALL
SELECT 'Reparación completada. Extintor funcionando.', NOW(), p.id, 39.4699, -0.3763, NOW(), NOW()
FROM partes p WHERE p.title = 'Reparation Extintor - Industrias DEF' AND p.date = '2024-12-03'
UNION ALL
SELECT 'Revisión técnica realizada. Sistema operativo.', NOW(), p.id, 40.4168, -3.7038, NOW(), NOW()
FROM partes p WHERE p.title = 'Mantenimiento Periódico - Oficinas Global' AND p.date = '2024-12-05';

-- Insertar fotos en comentarios
INSERT IGNORE INTO comment_fotos (comment_id, foto)
SELECT c.id, 'https://example.com/fotos/comentario1/foto1.jpg'
FROM comments c 
INNER JOIN partes p ON c.parte_id = p.id 
WHERE p.title = 'Mantenimiento Extintores - Empresa ABC' AND c.comentario LIKE 'Extintores revisados%'
UNION ALL
SELECT c.id, 'https://example.com/fotos/comentario1/foto2.jpg'
FROM comments c 
INNER JOIN partes p ON c.parte_id = p.id 
WHERE p.title = 'Mantenimiento Extintores - Empresa ABC' AND c.comentario LIKE 'Extintores revisados%'
UNION ALL
SELECT c.id, 'https://example.com/fotos/comentario2/foto1.jpg'
FROM comments c 
INNER JOIN partes p ON c.parte_id = p.id 
WHERE p.title = 'Instalación Sistema Detección - Comercial XYZ' AND c.comentario LIKE 'Instalación en progreso%'
UNION ALL
SELECT c.id, 'https://example.com/fotos/comentario3/foto1.jpg'
FROM comments c 
INNER JOIN partes p ON c.parte_id = p.id 
WHERE p.title = 'Reparation Extintor - Industrias DEF' AND c.comentario LIKE 'Reparación completada%'
UNION ALL
SELECT c.id, 'https://example.com/fotos/comentario4/foto1.jpg'
FROM comments c 
INNER JOIN partes p ON c.parte_id = p.id 
WHERE p.title = 'Mantenimiento Periódico - Oficinas Global' AND c.comentario LIKE 'Revisión técnica%';

-- =====================================================
-- 11. REPORTES (Reports)
-- =====================================================
-- Nota: start_time, end_time, created_date y last_modification son timestamps en milisegundos
INSERT IGNORE INTO reports (start_time, end_time, created_date, type, status, customer_id, title, description, contract_id, route_id, number, last_modification, user_id, created_date_audit, updated_date)
SELECT 1701432000000, 1701435600000, 1701432000000, 'maintenance', 'done',
    (SELECT id FROM customers WHERE code = 'CLI001' LIMIT 1),
    'Mantenimiento Mensual - Empresa ABC', 'Revisión completa de extintores y sistemas',
    (SELECT id FROM customers WHERE code = 'CLI001' LIMIT 1),
    (SELECT id FROM routes WHERE date = '2024-12-01' LIMIT 1),
    'RPT001', 1701435600000,
    (SELECT id FROM users WHERE code = 'WORKER001' LIMIT 1),
    NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM customers WHERE code = 'CLI001')
UNION ALL
SELECT 1701518400000, 1701525600000, 1701518400000, 'work', 'doing',
    (SELECT id FROM customers WHERE code = 'CLI002' LIMIT 1),
    'Instalación Sistema Detección', 'Instalación de detectores y sirena',
    (SELECT id FROM customers WHERE code = 'CLI002' LIMIT 1),
    (SELECT id FROM routes WHERE date = '2024-12-02' LIMIT 1),
    'RPT002', 1701522000000,
    (SELECT id FROM users WHERE code = 'WORKER002' LIMIT 1),
    NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM customers WHERE code = 'CLI002')
UNION ALL
SELECT 1701604800000, 1701608400000, 1701604800000, 'corrective', 'done',
    (SELECT id FROM customers WHERE code = 'CLI003' LIMIT 1),
    'Reparación Extintor', 'Reparación y recarga de extintor',
    (SELECT id FROM customers WHERE code = 'CLI003' LIMIT 1),
    (SELECT id FROM routes WHERE date = '2024-12-03' LIMIT 1),
    'RPT003', 1701608400000,
    (SELECT id FROM users WHERE code = 'WORKER003' LIMIT 1),
    NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM customers WHERE code = 'CLI003')
UNION ALL
SELECT 1701691200000, 1701694800000, 1701691200000, 'maintenance', 'doing',
    (SELECT id FROM customers WHERE code = 'CLI005' LIMIT 1),
    'Mantenimiento Trimestral', 'Revisión de sistemas de detección',
    (SELECT id FROM customers WHERE code = 'CLI005' LIMIT 1),
    (SELECT id FROM routes WHERE date = '2024-12-01' LIMIT 1),
    'RPT004', 1701693000000,
    (SELECT id FROM users WHERE code = 'WORKER001' LIMIT 1),
    NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM customers WHERE code = 'CLI005')
UNION ALL
SELECT 1701777600000, 1701784800000, 1701777600000, 'work', 'pending',
    (SELECT id FROM customers WHERE code = 'CLI006' LIMIT 1),
    'Instalación CCTV', 'Instalación de sistema de cámaras',
    (SELECT id FROM customers WHERE code = 'CLI006' LIMIT 1),
    (SELECT id FROM routes WHERE date = '2024-12-05' LIMIT 1),
    'RPT005', 1701777600000,
    NULL,
    NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM customers WHERE code = 'CLI006');

-- Insertar trabajadores en reportes
INSERT IGNORE INTO report_workers (report_id, user_id)
SELECT r.id, u.id
FROM reports r
CROSS JOIN users u
WHERE (r.number = 'RPT001' AND u.code IN ('WORKER001', 'WORKER002'))
   OR (r.number = 'RPT002' AND u.code IN ('WORKER002', 'WORKER003'))
   OR (r.number = 'RPT003' AND u.code = 'WORKER003')
   OR (r.number = 'RPT004' AND u.code IN ('WORKER001', 'WORKER004'))
   OR (r.number = 'RPT005' AND u.code IN ('WORKER004', 'WORKER005'));

-- Insertar herramientas en reportes
INSERT IGNORE INTO report_tools (report_id, material_id)
SELECT r.id, m.id
FROM reports r
CROSS JOIN materials m
WHERE (r.number = 'RPT001' AND m.code IN ('MAT001', 'MAT002', 'MAT008'))
   OR (r.number = 'RPT002' AND m.code IN ('MAT004', 'MAT005'))
   OR (r.number = 'RPT003' AND m.code IN ('MAT001', 'MAT008'))
   OR (r.number = 'RPT004' AND m.code IN ('MAT004', 'MAT005'))
   OR (r.number = 'RPT005' AND m.code = 'MAT006');

-- Insertar vehículos en reportes
INSERT IGNORE INTO report_vehicles (report_id, vehicle_id)
SELECT r.id, v.id
FROM reports r
CROSS JOIN vehicles v
WHERE (r.number = 'RPT001' AND v.matricula = '1234ABC')
   OR (r.number = 'RPT002' AND v.matricula = '5678DEF')
   OR (r.number = 'RPT003' AND v.matricula = '9012GHI')
   OR (r.number = 'RPT004' AND v.matricula = '1234ABC')
   OR (r.number = 'RPT005' AND v.matricula = '7890MNO');

-- =====================================================
-- 12. CHECKINS
-- =====================================================
INSERT IGNORE INTO checkins (checkin_start_time, checkin_lat, checkin_lng, checkout_start_time, checkout_lat, checkout_lng, user_id, report_id, created_date, updated_date)
SELECT 1701432000000, 40.4168, -3.7038, 1701435600000, 40.4168, -3.7038,
    (SELECT id FROM users WHERE code = 'WORKER001' LIMIT 1),
    (SELECT id FROM reports WHERE number = 'RPT001' LIMIT 1),
    NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM reports WHERE number = 'RPT001')
UNION ALL
SELECT 1701518400000, 41.3851, 2.1734, NULL, NULL, NULL,
    (SELECT id FROM users WHERE code = 'WORKER002' LIMIT 1),
    (SELECT id FROM reports WHERE number = 'RPT002' LIMIT 1),
    NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM reports WHERE number = 'RPT002')
UNION ALL
SELECT 1701604800000, 39.4699, -0.3763, 1701608400000, 39.4699, -0.3763,
    (SELECT id FROM users WHERE code = 'WORKER003' LIMIT 1),
    (SELECT id FROM reports WHERE number = 'RPT003' LIMIT 1),
    NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM reports WHERE number = 'RPT003')
UNION ALL
SELECT 1701691200000, 40.4168, -3.7038, NULL, NULL, NULL,
    (SELECT id FROM users WHERE code = 'WORKER001' LIMIT 1),
    (SELECT id FROM reports WHERE number = 'RPT004' LIMIT 1),
    NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM reports WHERE number = 'RPT004');

-- =====================================================
-- 13. ALERTAS (Alerts)
-- =====================================================
INSERT IGNORE INTO alerts (message, state, created_date, updated_date) VALUES
('Extintor próximo a caducar en Cliente CLI001', 'Pendiente', NOW(), NOW()),
('Falta de material en almacén: Extintor CO2 5kg', 'Pendiente', NOW(), NOW()),
('Revisión pendiente en Cliente CLI003', 'Pendiente', NOW(), NOW()),
('Sistema de detección requiere mantenimiento en Cliente CLI004', 'Cerrado', NOW(), NOW()),
('Vehículo 1234ABC requiere revisión técnica', 'Pendiente', NOW(), NOW());

-- =====================================================
-- 14. FACTURACIÓN
-- =====================================================
INSERT IGNORE INTO facturacion (facturacion, ruta_id, parte_id, created_date, updated_date)
SELECT 450.00,
    (SELECT id FROM routes WHERE date = '2024-12-01' LIMIT 1),
    (SELECT id FROM partes WHERE title = 'Mantenimiento Extintores - Empresa ABC' AND date = '2024-12-01' LIMIT 1),
    NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM partes WHERE title = 'Mantenimiento Extintores - Empresa ABC')
UNION ALL
SELECT 2500.00,
    (SELECT id FROM routes WHERE date = '2024-12-02' LIMIT 1),
    (SELECT id FROM partes WHERE title = 'Instalación Sistema Detección - Comercial XYZ' AND date = '2024-12-02' LIMIT 1),
    NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM partes WHERE title = 'Instalación Sistema Detección - Comercial XYZ')
UNION ALL
SELECT 180.00,
    (SELECT id FROM routes WHERE date = '2024-12-03' LIMIT 1),
    (SELECT id FROM partes WHERE title = 'Reparation Extintor - Industrias DEF' AND date = '2024-12-03' LIMIT 1),
    NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM partes WHERE title = 'Reparation Extintor - Industrias DEF')
UNION ALL
SELECT 650.00,
    (SELECT id FROM routes WHERE date = '2024-12-01' LIMIT 1),
    (SELECT id FROM partes WHERE title = 'Mantenimiento Periódico - Oficinas Global' AND date = '2024-12-05' LIMIT 1),
    NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM partes WHERE title = 'Mantenimiento Periódico - Oficinas Global')
UNION ALL
SELECT 3200.00,
    (SELECT id FROM routes WHERE date = '2024-12-05' LIMIT 1),
    (SELECT id FROM partes WHERE title = 'Instalación CCTV - Hotel Residencia' AND date = '2024-12-06' LIMIT 1),
    NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM partes WHERE title = 'Instalación CCTV - Hotel Residencia');

-- =====================================================
-- 15. PLANTILLAS DE PARTES (ParteTemplate)
-- =====================================================
INSERT IGNORE INTO parte_templates (name, title, description, customer_id, address, type, categoria, periodico, frequency, coordination_method, gestiona, facturacion, created_date, updated_date)
SELECT 'Mantenimiento Mensual Extintores', 'Mantenimiento Mensual - Extintores', 'Plantilla para mantenimiento mensual de extintores',
    (SELECT id FROM customers WHERE code = 'CLI001' LIMIT 1),
    'Calle Mayor 123, Madrid', 'Mantenimiento', 'Extintores', true, 'Mensual', 'COORDINAR_HORARIOS', 1, 450.00, NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM customers WHERE code = 'CLI001')
UNION ALL
SELECT 'Revisión Trimestral Sistemas', 'Revisión Trimestral - Sistemas de Seguridad', 'Plantilla para revisión trimestral de sistemas',
    (SELECT id FROM customers WHERE code = 'CLI005' LIMIT 1),
    'Paseo de la Castellana 321, Madrid', 'Mantenimiento', 'Incendio', true, 'Trimestral', 'COORDINAR_HORARIOS', 1, 650.00, NOW(), NOW()
WHERE EXISTS (SELECT 1 FROM customers WHERE code = 'CLI005')
UNION ALL
SELECT 'Instalación Estándar CCTV', 'Instalación Sistema CCTV', 'Plantilla para instalación de sistemas de cámaras',
    NULL,
    'Dirección variable', 'Obra', 'CCTV', false, NULL, 'LLAMAR_ANTES', 2, 3200.00, NOW(), NOW();

-- =====================================================
-- FIN DEL SCRIPT
-- =====================================================
-- Credenciales de prueba:
-- Email: admin@extinguidor.com
-- Password: password123
--
-- Email: juan.perez@extinguidor.com
-- Password: password123
--
-- (Todos los usuarios tienen la misma contraseña: password123)
-- =====================================================

