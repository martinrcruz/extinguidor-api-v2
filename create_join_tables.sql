-- Script para crear las tablas intermedias faltantes
-- Ejecutar este script en MySQL antes de iniciar la aplicación

USE extinguidordb;

-- Crear tabla report_tools (Report <-> Material)
CREATE TABLE IF NOT EXISTS report_tools (
    report_id   BIGINT NOT NULL,
    material_id BIGINT NOT NULL,
    PRIMARY KEY (report_id, material_id),
    INDEX idx_report_tools_report (report_id),
    INDEX idx_report_tools_material (material_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crear tabla report_vehicles (Report <-> Vehicle)
CREATE TABLE IF NOT EXISTS report_vehicles (
    report_id  BIGINT NOT NULL,
    vehicle_id BIGINT NOT NULL,
    PRIMARY KEY (report_id, vehicle_id),
    INDEX idx_report_vehicles_report (report_id),
    INDEX idx_report_vehicles_vehicle (vehicle_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crear tabla report_workers (Report <-> User)
CREATE TABLE IF NOT EXISTS report_workers (
    report_id BIGINT NOT NULL,
    user_id   BIGINT NOT NULL,
    PRIMARY KEY (report_id, user_id),
    INDEX idx_report_workers_report (report_id),
    INDEX idx_report_workers_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crear tabla route_materials (Route <-> Material)
CREATE TABLE IF NOT EXISTS route_materials (
    route_id    BIGINT NOT NULL,
    material_id BIGINT NOT NULL,
    PRIMARY KEY (route_id, material_id),
    INDEX idx_route_materials_route (route_id),
    INDEX idx_route_materials_material (material_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Agregar claves foráneas para report_tools
ALTER TABLE report_tools
    ADD CONSTRAINT fk_report_tools_report
        FOREIGN KEY (report_id) REFERENCES reports(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    ADD CONSTRAINT fk_report_tools_material
        FOREIGN KEY (material_id) REFERENCES materials(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

-- Agregar claves foráneas para report_vehicles
ALTER TABLE report_vehicles
    ADD CONSTRAINT fk_report_vehicles_report
        FOREIGN KEY (report_id) REFERENCES reports(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    ADD CONSTRAINT fk_report_vehicles_vehicle
        FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

-- Agregar claves foráneas para report_workers
ALTER TABLE report_workers
    ADD CONSTRAINT fk_report_workers_report
        FOREIGN KEY (report_id) REFERENCES reports(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    ADD CONSTRAINT fk_report_workers_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

-- Agregar claves foráneas para route_materials
ALTER TABLE route_materials
    ADD CONSTRAINT fk_route_materials_route
        FOREIGN KEY (route_id) REFERENCES routes(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    ADD CONSTRAINT fk_route_materials_material
        FOREIGN KEY (material_id) REFERENCES materials(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

