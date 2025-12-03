# Documentación API Extinguidor App

## Información General

- **Base URL**: `http://localhost:8080` (desarrollo) / `https://extinguidor-backend-tj94j.ondigitalocean.app` (producción)
- **Formato de respuesta**: JSON
- **Autenticación**: JWT Bearer Token (excepto endpoints públicos)

## Roles del Sistema

El sistema tiene dos roles principales:

- **ADMIN**: Administrador con acceso completo al sistema
- **WORKER**: Trabajador con acceso limitado a funciones específicas

## Endpoints Públicos (Sin autenticación)

### 1. Autenticación

#### POST /auth/login
Login de usuario y obtención de token JWT.

**Permisos**: Público (sin autenticación)

**Request Body**:
```json
{
  "email": "admin@example.com",
  "password": "password123"
}
```

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "role": "ADMIN",
    "user": {
      "id": 1,
      "name": "Admin Usuario",
      "code": "ADM001",
      "email": "admin@example.com",
      "phone": "+34600000000",
      "role": "ADMIN",
      "photo": "https://ionicframework.com/docs/img/demos/avatar.svg",
      "activo": true
    }
  }
}
```

**Response 401 Unauthorized**:
```json
{
  "ok": false,
  "error": "Credenciales incorrectas"
}
```

---

#### POST /user/login
Login alternativo (mismo funcionamiento que /auth/login).

**Permisos**: Público (sin autenticación)

**Request/Response**: Igual que `/auth/login`

---

#### POST /user/create
Crear nuevo usuario (registro).

**Permisos**: Público (sin autenticación)

**Request Body**:
```json
{
  "name": "Juan Pérez",
  "code": "WRK001",
  "email": "juan.perez@example.com",
  "phone": "+34611222333",
  "password": "securePassword123",
  "role": "WORKER",
  "photo": "https://example.com/photo.jpg",
  "activo": true
}
```

**Response 201 Created**:
```json
{
  "ok": true,
  "data": {
    "user": {
      "id": 2,
      "name": "Juan Pérez",
      "code": "WRK001",
      "email": "juan.perez@example.com",
      "phone": "+34611222333",
      "role": "WORKER",
      "photo": "https://example.com/photo.jpg",
      "activo": true
    }
  },
  "message": "Usuario creado exitosamente"
}
```

---

## Endpoints con Autenticación

**Nota**: Todos los siguientes endpoints requieren el header:
```
Authorization: Bearer {token}
```

---

## 2. Gestión de Usuarios

### GET /user/list
Listar todos los usuarios.

**Permisos**: ADMIN

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "users": [
      {
        "id": 1,
        "name": "Admin Usuario",
        "code": "ADM001",
        "email": "admin@example.com",
        "phone": "+34600000000",
        "role": "ADMIN",
        "photo": "https://ionicframework.com/docs/img/demos/avatar.svg",
        "activo": true
      },
      {
        "id": 2,
        "name": "Juan Pérez",
        "code": "WRK001",
        "email": "juan.perez@example.com",
        "phone": "+34611222333",
        "role": "WORKER",
        "photo": "https://example.com/photo.jpg",
        "activo": true
      }
    ]
  }
}
```

---

### GET /user/worker
Listar solo trabajadores (usuarios con rol WORKER).

**Permisos**: ADMIN

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "workers": [
      {
        "id": 2,
        "name": "Juan Pérez",
        "code": "WRK001",
        "email": "juan.perez@example.com",
        "phone": "+34611222333",
        "role": "WORKER",
        "activo": true
      }
    ]
  }
}
```

---

### GET /user/{id}
Obtener usuario por ID.

**Permisos**: ADMIN

**Path Parameters**:
- `id` (Long): ID del usuario

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "user": {
      "id": 1,
      "name": "Admin Usuario",
      "code": "ADM001",
      "email": "admin@example.com",
      "phone": "+34600000000",
      "role": "ADMIN",
      "activo": true
    }
  }
}
```

---

### GET /user
Obtener perfil del usuario autenticado.

**Permisos**: ADMIN, WORKER

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "user": {
      "id": 2,
      "name": "Juan Pérez",
      "code": "WRK001",
      "email": "juan.perez@example.com",
      "phone": "+34611222333",
      "role": "WORKER",
      "activo": true
    }
  }
}
```

---

### PUT /user/update
Actualizar usuario.

**Permisos**: ADMIN, WORKER

**Request Body**:
```json
{
  "id": 2,
  "name": "Juan Pérez Actualizado",
  "code": "WRK001",
  "email": "juan.perez@example.com",
  "phone": "+34611222333",
  "password": "newPassword123",
  "role": "WORKER",
  "activo": true
}
```

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "user": {
      "id": 2,
      "name": "Juan Pérez Actualizado",
      "code": "WRK001",
      "email": "juan.perez@example.com",
      "phone": "+34611222333",
      "role": "WORKER",
      "activo": true
    },
    "token": "eyJhbGciOiJIUzUxMiJ9..."
  },
  "message": "Usuario actualizado exitosamente"
}
```

---

### DELETE /user/delete/{id}
Eliminar usuario.

**Permisos**: ADMIN

**Path Parameters**:
- `id` (Long): ID del usuario a eliminar

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "message": "Usuario eliminado exitosamente"
  }
}
```

---

## 3. Gestión de Clientes

### GET /customers
Listar todos los clientes.

**Permisos**: ADMIN

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "customers": [
      {
        "id": 1,
        "name": "Empresa ABC S.L.",
        "email": "contacto@empresaabc.com",
        "nifCif": "B12345678",
        "address": "Calle Principal 123, Madrid",
        "phone": "+34912345678",
        "contactName": "María García",
        "code": "CLI001",
        "active": true,
        "zone": {
          "id": 1,
          "name": "Zona Centro"
        },
        "description": "Cliente importante del sector industrial",
        "gestiona": "Juan Pérez",
        "photo": "foto.jpg",
        "startDate": "2024-01-01",
        "endDate": "2024-12-31",
        "type": "ANUAL",
        "contractSystems": ["Extintores", "BIE", "Iluminación"],
        "averageTime": 120,
        "delegation": "Madrid",
        "revisionFrequency": "MENSUAL",
        "rate": "NORMAL",
        "mi": 0,
        "tipo": "Normal",
        "total": 1500.00,
        "documents": [
          {
            "name": "Contrato_2024.pdf",
            "url": "https://example.com/contratos/contrato1.pdf"
          }
        ]
      }
    ]
  }
}
```

---

### GET /customers/{id}
Obtener cliente por ID.

**Permisos**: ADMIN

**Path Parameters**:
- `id` (Long): ID del cliente

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "customer": {
      "id": 1,
      "name": "Empresa ABC S.L.",
      "email": "contacto@empresaabc.com",
      "nifCif": "B12345678",
      "address": "Calle Principal 123, Madrid",
      "phone": "+34912345678",
      "contactName": "María García",
      "code": "CLI001",
      "active": true
    }
  }
}
```

---

### POST /customers/create
Crear nuevo cliente.

**Permisos**: ADMIN

**Request Body**:
```json
{
  "name": "Nueva Empresa S.L.",
  "email": "info@nuevaempresa.com",
  "nifCif": "B98765432",
  "address": "Avenida Secundaria 456, Barcelona",
  "phone": "+34933333333",
  "contactName": "Pedro López",
  "code": "CLI002",
  "active": true,
  "description": "Nuevo cliente del sector retail",
  "gestiona": "Ana Martínez",
  "startDate": "2024-02-01",
  "endDate": "2025-01-31",
  "type": "ANUAL",
  "contractSystems": ["Extintores"],
  "averageTime": 90,
  "revisionFrequency": "TRIMESTRAL",
  "rate": "NORMAL",
  "total": 800.00
}
```

**Response 201 Created**:
```json
{
  "ok": true,
  "data": {
    "customer": {
      "id": 2,
      "name": "Nueva Empresa S.L.",
      "code": "CLI002",
      "active": true
    }
  },
  "message": "Cliente creado exitosamente"
}
```

---

### PUT /customers/update
Actualizar cliente.

**Permisos**: ADMIN

**Request Body**: Similar a POST /customers/create, incluir el campo `id`

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "customer": {
      "id": 2,
      "name": "Nueva Empresa S.L. Actualizada",
      "code": "CLI002"
    }
  },
  "message": "Cliente actualizado exitosamente"
}
```

---

### DELETE /customers/{id}
Eliminar cliente.

**Permisos**: ADMIN

**Path Parameters**:
- `id` (Long): ID del cliente

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "message": "Cliente eliminado exitosamente"
  }
}
```

---

## 4. Gestión de Partes

### GET /partes
Listar partes con paginación.

**Permisos**: ADMIN

**Query Parameters**:
- `page` (int, default: 0): Número de página
- `limit` (int, default: 10): Elementos por página

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "partes": [
      {
        "id": 1,
        "title": "Mantenimiento Extintores - Empresa ABC",
        "description": "Revisión anual de extintores",
        "date": "2024-11-30",
        "customer": {
          "id": 1,
          "name": "Empresa ABC S.L.",
          "code": "CLI001"
        },
        "address": "Calle Principal 123, Madrid",
        "state": "Pendiente",
        "type": "Mantenimiento",
        "categoria": "Extintores",
        "asignado": false,
        "eliminado": false,
        "periodico": true,
        "frequency": "ANUAL",
        "coordinationMethod": "COORDINAR_HORARIOS",
        "gestiona": 0,
        "facturacion": 150.00,
        "comentarios": [],
        "documentos": [],
        "articulos": []
      }
    ],
    "totalPages": 10,
    "totalItems": 95
  }
}
```

---

### GET /partes/{id}
Obtener parte por ID.

**Permisos**: ADMIN, WORKER

**Path Parameters**:
- `id` (Long): ID del parte

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "parte": {
      "id": 1,
      "title": "Mantenimiento Extintores - Empresa ABC",
      "description": "Revisión anual de extintores",
      "date": "2024-11-30",
      "customer": {
        "id": 1,
        "name": "Empresa ABC S.L."
      },
      "state": "Pendiente"
    }
  }
}
```

---

### GET /partes/contrato/{customerId}
Obtener partes por cliente.

**Permisos**: ADMIN

**Path Parameters**:
- `customerId` (Long): ID del cliente

**Response 200 OK**: Array de partes

---

### GET /partes/ruta/{routeId}
Obtener partes por ruta.

**Permisos**: ADMIN, WORKER

**Path Parameters**:
- `routeId` (Long): ID de la ruta

**Response 200 OK**: Array de partes

---

### GET /partes/worker/{workerId}
Obtener partes por trabajador.

**Permisos**: ADMIN, WORKER

**Path Parameters**:
- `workerId` (Long): ID del trabajador

**Query Parameters**:
- `date` (LocalDate, opcional): Fecha específica (formato: YYYY-MM-DD)

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "partes": [
      {
        "id": 1,
        "title": "Mantenimiento Extintores",
        "date": "2024-11-30",
        "state": "EnProgreso"
      }
    ]
  }
}
```

---

### GET /partes/noasignados
Obtener partes no asignados.

**Permisos**: ADMIN

**Query Parameters**:
- `date` (LocalDate, opcional): Fecha específica (formato: YYYY-MM-DD), default: hoy

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "partes": [
      {
        "id": 2,
        "title": "Instalación BIE - Cliente XYZ",
        "date": "2024-12-01",
        "state": "Pendiente",
        "asignado": false
      }
    ]
  }
}
```

---

### POST /partes/create
Crear nuevo parte.

**Permisos**: ADMIN

**Request Body**:
```json
{
  "title": "Revisión sistema contra incendios",
  "description": "Revisión completa del sistema de protección contra incendios",
  "date": "2024-12-05",
  "customer": {
    "id": 1
  },
  "address": "Calle Principal 123, Madrid",
  "state": "Pendiente",
  "type": "Mantenimiento",
  "categoria": "BIE",
  "asignado": false,
  "periodico": true,
  "frequency": "TRIMESTRAL",
  "coordinationMethod": "COORDINAR_HORARIOS",
  "facturacion": 200.00
}
```

**Response 201 Created**:
```json
{
  "ok": true,
  "data": {
    "parte": {
      "id": 3,
      "title": "Revisión sistema contra incendios",
      "date": "2024-12-05",
      "state": "Pendiente"
    }
  },
  "message": "Parte creado exitosamente"
}
```

---

### POST /partes/update
Actualizar parte.

**Permisos**: ADMIN, WORKER

**Request Body**: Similar a POST /partes/create, incluir campo `id`

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "parte": {
      "id": 3,
      "title": "Revisión sistema contra incendios - Actualizado",
      "state": "EnProgreso"
    }
  },
  "message": "Parte actualizado exitosamente"
}
```

---

### PUT /partes/{id}/status
Actualizar estado del parte.

**Permisos**: ADMIN, WORKER

**Path Parameters**:
- `id` (Long): ID del parte

**Request Body**:
```json
{
  "status": "Finalizado"
}
```

**Estados válidos**: `Pendiente`, `EnProgreso`, `Finalizado`, `Cancelado`

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "parte": {
      "id": 3,
      "state": "Finalizado"
    }
  },
  "message": "Estado actualizado exitosamente"
}
```

---

### DELETE /partes/{id}
Eliminar parte.

**Permisos**: ADMIN

**Path Parameters**:
- `id` (Long): ID del parte

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "message": "Parte eliminado exitosamente"
  }
}
```

---

## 5. Gestión de Rutas

### GET /rutas
Listar todas las rutas.

**Permisos**: ADMIN

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "rutas": [
      {
        "id": 1,
        "encargado": {
          "id": 2,
          "name": "Juan Pérez"
        },
        "name": {
          "id": 1,
          "name": "Ruta Centro"
        },
        "date": "2024-12-01",
        "state": "Pendiente",
        "vehicle": {
          "id": 1,
          "plate": "1234ABC"
        },
        "users": [
          {
            "id": 2,
            "name": "Juan Pérez"
          },
          {
            "id": 3,
            "name": "Pedro Martínez"
          }
        ],
        "comentarios": "Ruta con 5 partes asignados",
        "herramientas": [],
        "eliminado": false
      }
    ]
  }
}
```

---

### GET /rutas/{id}
Obtener ruta por ID.

**Permisos**: ADMIN, WORKER

**Path Parameters**:
- `id` (Long): ID de la ruta

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "ruta": {
      "id": 1,
      "date": "2024-12-01",
      "state": "Pendiente"
    }
  }
}
```

---

### GET /rutas/worker/{workerId}
Obtener rutas por trabajador.

**Permisos**: ADMIN, WORKER

**Path Parameters**:
- `workerId` (Long): ID del trabajador

**Query Parameters**:
- `date` (LocalDate, opcional): Fecha específica

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "rutas": [
      {
        "id": 1,
        "date": "2024-12-01",
        "state": "Pendiente"
      }
    ]
  }
}
```

---

### GET /rutas/disponibles
Obtener rutas disponibles (sin asignar).

**Permisos**: ADMIN

**Query Parameters**:
- `date` (LocalDate, opcional): Fecha específica

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "rutas": []
  }
}
```

---

### GET /rutas/{routeId}/partes
Obtener partes de una ruta.

**Permisos**: ADMIN, WORKER

**Path Parameters**:
- `routeId` (Long): ID de la ruta

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "partes": [
      {
        "id": 1,
        "title": "Mantenimiento Extintores",
        "customer": {
          "name": "Empresa ABC S.L."
        }
      }
    ]
  }
}
```

---

### POST /rutas/create
Crear nueva ruta.

**Permisos**: ADMIN

**Request Body**:
```json
{
  "encargado": {
    "id": 2
  },
  "name": {
    "id": 1
  },
  "date": "2024-12-10",
  "state": "Pendiente",
  "vehicle": {
    "id": 1
  },
  "users": [
    { "id": 2 },
    { "id": 3 }
  ],
  "comentarios": "Ruta programada para el sector norte"
}
```

**Response 201 Created**:
```json
{
  "ok": true,
  "data": {
    "ruta": {
      "id": 2,
      "date": "2024-12-10",
      "state": "Pendiente"
    }
  },
  "message": "Ruta creada exitosamente"
}
```

---

### POST /rutas/update
Actualizar ruta.

**Permisos**: ADMIN

**Request Body**: Similar a POST /rutas/create, incluir campo `id`

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "ruta": {
      "id": 2,
      "date": "2024-12-10"
    }
  },
  "message": "Ruta actualizada exitosamente"
}
```

---

### POST /rutas/{id}/asignarPartes
Asignar partes a una ruta.

**Permisos**: ADMIN

**Path Parameters**:
- `id` (Long): ID de la ruta

**Request Body**:
```json
{
  "parteIds": [1, 2, 3, 4, 5]
}
```

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "message": "Partes asignados exitosamente"
  }
}
```

---

### DELETE /rutas/{id}
Eliminar ruta (eliminación lógica).

**Permisos**: ADMIN

**Path Parameters**:
- `id` (Long): ID de la ruta

**Response 204 No Content**

---

## 6. Gestión de Nombres de Rutas (RutaN)

### GET /rutasn
Listar todos los nombres de rutas.

**Permisos**: ADMIN

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "rutasN": [
      {
        "id": 1,
        "name": "Ruta Centro",
        "description": "Cubre la zona centro de Madrid"
      },
      {
        "id": 2,
        "name": "Ruta Norte",
        "description": "Cubre la zona norte de Madrid"
      }
    ]
  }
}
```

---

### GET /rutasn/{id}
Obtener nombre de ruta por ID.

**Permisos**: ADMIN

**Path Parameters**:
- `id` (Long): ID del nombre de ruta

---

### POST /rutasn/create
Crear nombre de ruta.

**Permisos**: ADMIN

**Request Body**:
```json
{
  "name": "Ruta Sur",
  "description": "Cubre la zona sur de Madrid"
}
```

**Response 201 Created**:
```json
{
  "ok": true,
  "data": {
    "rutaN": {
      "id": 3,
      "name": "Ruta Sur"
    }
  },
  "message": "Nombre de ruta creado exitosamente"
}
```

---

### PUT /rutasn/{id}
Actualizar nombre de ruta.

**Permisos**: ADMIN

---

### DELETE /rutasn/{id}
Eliminar nombre de ruta.

**Permisos**: ADMIN

---

## 7. Gestión de Vehículos

### GET /vehicle
Listar todos los vehículos.

**Permisos**: ADMIN

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "vehicles": [
      {
        "id": 1,
        "plate": "1234ABC",
        "brand": "Ford",
        "model": "Transit",
        "year": 2022,
        "type": "FURGONETA",
        "fuel": "DIESEL",
        "active": true
      }
    ]
  }
}
```

---

### GET /vehicle/{id}
Obtener vehículo por ID.

**Permisos**: ADMIN

---

### POST /vehicle/create
Crear vehículo.

**Permisos**: ADMIN

**Request Body**:
```json
{
  "plate": "5678DEF",
  "brand": "Mercedes",
  "model": "Sprinter",
  "year": 2023,
  "type": "FURGONETA",
  "fuel": "DIESEL",
  "active": true
}
```

**Response 201 Created**:
```json
{
  "ok": true,
  "data": {
    "vehicle": {
      "id": 2,
      "plate": "5678DEF",
      "brand": "Mercedes"
    }
  },
  "message": "Vehículo creado exitosamente"
}
```

---

### PUT /vehicle/update
Actualizar vehículo.

**Permisos**: ADMIN

---

### DELETE /vehicle/{id}
Eliminar vehículo.

**Permisos**: ADMIN

---

## 8. Gestión de Zonas

### GET /zone
Listar todas las zonas.

**Permisos**: ADMIN

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "zones": [
      {
        "id": 1,
        "name": "Zona Centro",
        "description": "Centro de Madrid",
        "active": true
      }
    ]
  }
}
```

---

### GET /zone/{id}
Obtener zona por ID.

**Permisos**: ADMIN

---

### POST /zone/create
Crear zona.

**Permisos**: ADMIN

**Request Body**:
```json
{
  "name": "Zona Este",
  "description": "Este de Madrid",
  "active": true
}
```

---

### PUT /zone/update
Actualizar zona.

**Permisos**: ADMIN

---

### DELETE /zone/{id}
Eliminar zona.

**Permisos**: ADMIN

---

## 9. Gestión de Materiales

### GET /material
Listar todos los materiales.

**Permisos**: ADMIN

**Response 200 OK**:
```json
{
  "ok": true,
  "data": [
    {
      "id": 1,
      "name": "Taladro",
      "description": "Taladro eléctrico Bosch",
      "quantity": 5,
      "active": true
    },
    {
      "id": 2,
      "name": "Escalera",
      "description": "Escalera telescópica 3m",
      "quantity": 3,
      "active": true
    }
  ]
}
```

---

### GET /material/{id}
Obtener material por ID.

**Permisos**: ADMIN

---

### POST /material/create
Crear material.

**Permisos**: ADMIN

**Request Body**:
```json
{
  "name": "Destornillador",
  "description": "Set de destornilladores profesional",
  "quantity": 10,
  "active": true
}
```

---

### PUT /material/update
Actualizar material.

**Permisos**: ADMIN

---

### DELETE /material/{id}
Eliminar material.

**Permisos**: ADMIN

---

## 10. Gestión de Artículos

### GET /articulos
Listar todos los artículos.

**Permisos**: ADMIN

**Query Parameters**:
- `page` (int, default: 1): Número de página
- `limit` (int, default: 100): Elementos por página
- `search` (string, opcional): Búsqueda por texto
- `grupo` (string, opcional): Filtrar por grupo
- `familia` (string, opcional): Filtrar por familia

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "articulos": [
      {
        "id": 1,
        "codigo": "EXT-CO2-6KG",
        "grupo": "Extintores",
        "familia": "CO2",
        "descripcionArticulo": "Extintor CO2 6Kg",
        "precioVenta": 85.50,
        "stock": 25,
        "active": true
      }
    ],
    "pagination": {
      "currentPage": 1,
      "totalPages": 1,
      "totalItems": 1,
      "itemsPerPage": 100,
      "hasNextPage": false,
      "hasPrevPage": false
    }
  }
}
```

---

### GET /articulos/{id}
Obtener artículo por ID.

**Permisos**: ADMIN

---

### POST /articulos
Crear artículo.

**Permisos**: ADMIN

**Request Body**:
```json
{
  "codigo": "EXT-ABC-9KG",
  "grupo": "Extintores",
  "familia": "Polvo ABC",
  "descripcionArticulo": "Extintor Polvo ABC 9Kg",
  "precioVenta": 75.00,
  "stock": 15,
  "active": true
}
```

---

### PUT /articulos/{id}
Actualizar artículo.

**Permisos**: ADMIN

---

### DELETE /articulos/{id}
Eliminar artículo (lógica).

**Permisos**: ADMIN

---

## 11. Gestión de Alertas

### GET /alertas
Listar todas las alertas.

**Permisos**: ADMIN, WORKER

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "alertas": [
      {
        "id": 1,
        "title": "Cliente sin contactar",
        "description": "El cliente ABC no ha sido contactado para la revisión de diciembre",
        "state": "PENDING",
        "priority": "HIGH",
        "createdDate": "2024-11-28T10:30:00"
      }
    ]
  }
}
```

---

### GET /alertas/{id}
Obtener alerta por ID.

**Permisos**: ADMIN, WORKER

---

### POST /alertas
Crear alerta.

**Permisos**: ADMIN, WORKER

**Request Body**:
```json
{
  "title": "Material faltante",
  "description": "Falta material para la ruta del día 5 de diciembre",
  "state": "PENDING",
  "priority": "MEDIUM"
}
```

---

### PUT /alertas/{id}
Actualizar estado de alerta.

**Permisos**: ADMIN, WORKER

**Request Body**:
```json
{
  "state": "RESOLVED"
}
```

**Estados válidos**: `PENDING`, `IN_PROGRESS`, `RESOLVED`, `DISMISSED`

---

### DELETE /alertas/{id}
Eliminar alerta.

**Permisos**: ADMIN, WORKER

---

## 12. Gestión de Checkins

### GET /checkin/list
Listar todos los checkins.

**Permisos**: ADMIN, WORKER

**Response 200 OK**:
```json
[
  {
    "id": 1,
    "reportId": 1,
    "userId": 2,
    "latitude": 40.416775,
    "longitude": -3.703790,
    "timestamp": "2024-11-29T08:30:00",
    "type": "CHECK_IN"
  }
]
```

---

### GET /checkin/{id}
Obtener checkin por ID.

**Permisos**: ADMIN, WORKER

---

### GET /checkin/report/{reportId}
Listar checkins por reporte.

**Permisos**: ADMIN, WORKER

---

### GET /checkin/user/{userId}
Listar checkins por usuario.

**Permisos**: ADMIN, WORKER

---

### POST /checkin
Crear checkin.

**Permisos**: ADMIN, WORKER

**Request Body**:
```json
{
  "reportId": 1,
  "userId": 2,
  "latitude": 40.416775,
  "longitude": -3.703790,
  "timestamp": "2024-11-29T08:30:00",
  "type": "CHECK_IN"
}
```

---

### PUT /checkin/{id}
Actualizar checkin.

**Permisos**: ADMIN, WORKER

---

### DELETE /checkin/{id}
Eliminar checkin.

**Permisos**: ADMIN, WORKER

---

## 13. Gestión de Reportes

### GET /report/list
Listar todos los reportes.

**Permisos**: ADMIN, WORKER

**Response 200 OK**:
```json
[
  {
    "id": 1,
    "customerId": 1,
    "routeId": 1,
    "userId": 2,
    "type": "MAINTENANCE",
    "status": "COMPLETED",
    "title": "Revisión Anual Extintores",
    "description": "Se han revisado 15 extintores",
    "date": "2024-11-29",
    "observations": "Todo correcto"
  }
]
```

---

### GET /report/{id}
Obtener reporte por ID.

**Permisos**: ADMIN, WORKER

---

### GET /report/customer/{customerId}
Listar reportes por cliente.

**Permisos**: ADMIN, WORKER

---

### GET /report/route/{routeId}
Listar reportes por ruta.

**Permisos**: ADMIN, WORKER

---

### GET /report/user/{userId}
Listar reportes por usuario.

**Permisos**: ADMIN, WORKER

---

### GET /report/status/{status}
Listar reportes por estado.

**Permisos**: ADMIN, WORKER

**Estados válidos**: `PENDING`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`

---

### GET /report/type/{type}
Listar reportes por tipo.

**Permisos**: ADMIN, WORKER

**Tipos válidos**: `MAINTENANCE`, `INSTALLATION`, `REPAIR`, `INSPECTION`

---

### GET /report/worker/{workerId}
Listar reportes por trabajador.

**Permisos**: ADMIN, WORKER

---

### POST /report
Crear reporte.

**Permisos**: ADMIN, WORKER

**Request Body**:
```json
{
  "customerId": 1,
  "routeId": 1,
  "userId": 2,
  "type": "MAINTENANCE",
  "status": "COMPLETED",
  "title": "Revisión Trimestral",
  "description": "Revisión completa de sistema contra incendios",
  "date": "2024-11-29",
  "observations": "Todo en orden"
}
```

---

### PUT /report/{id}
Actualizar reporte.

**Permisos**: ADMIN, WORKER

---

### PUT /report/{id}/status
Actualizar estado de reporte.

**Permisos**: ADMIN, WORKER

**Request Body**: Estado como string en el cuerpo

---

### DELETE /report/{id}
Eliminar reporte.

**Permisos**: ADMIN

---

## 14. Gestión de Comentarios

### GET /comment/list
Listar todos los comentarios.

**Permisos**: ADMIN, WORKER

---

### GET /comment/{id}
Obtener comentario por ID.

**Permisos**: ADMIN, WORKER

---

### GET /comment/parte/{parteId}
Listar comentarios por parte.

**Permisos**: ADMIN, WORKER

**Response 200 OK**:
```json
[
  {
    "id": 1,
    "parteId": 1,
    "userId": 2,
    "userName": "Juan Pérez",
    "text": "Cliente requiere revisión adicional",
    "createdDate": "2024-11-29T10:00:00"
  }
]
```

---

### POST /comment
Crear comentario.

**Permisos**: ADMIN, WORKER

**Request Body**:
```json
{
  "parteId": 1,
  "userId": 2,
  "text": "Se necesita material adicional para esta tarea"
}
```

---

### PUT /comment/{id}
Actualizar comentario.

**Permisos**: ADMIN, WORKER

---

### DELETE /comment/{id}
Eliminar comentario.

**Permisos**: ADMIN, WORKER

---

## 15. Gestión de Facturación

### GET /facturacion
Listar todas las facturaciones.

**Permisos**: ADMIN

**Response 200 OK**:
```json
{
  "ok": true,
  "data": {
    "facturacion": [
      {
        "id": 1,
        "routeId": 1,
        "parteId": 1,
        "amount": 150.00,
        "date": "2024-11-29",
        "invoiceNumber": "FAC-2024-001",
        "status": "PAID"
      }
    ]
  }
}
```

---

### GET /facturacion/{id}
Obtener facturación por ID.

**Permisos**: ADMIN

---

### GET /facturacion/ruta/{rutaId}
Listar facturaciones por ruta.

**Permisos**: ADMIN

---

### GET /facturacion/parte/{parteId}
Listar facturaciones por parte.

**Permisos**: ADMIN

---

### POST /facturacion/create
Crear facturación.

**Permisos**: ADMIN

**Request Body**:
```json
{
  "routeId": 1,
  "parteId": 1,
  "amount": 200.00,
  "date": "2024-12-01",
  "invoiceNumber": "FAC-2024-002",
  "status": "PENDING"
}
```

---

### PUT /facturacion/update/{id}
Actualizar facturación.

**Permisos**: ADMIN

---

### DELETE /facturacion/{id}
Eliminar facturación.

**Permisos**: ADMIN

---

### GET /facturacion/daily-aggregation
Obtener agregación de facturación por día.

**Permisos**: ADMIN

**Response 200 OK**:
```json
[
  {
    "date": "2024-11-29",
    "totalAmount": 1500.00,
    "totalInvoices": 10
  },
  {
    "date": "2024-11-30",
    "totalAmount": 2300.00,
    "totalInvoices": 15
  }
]
```

---

### GET /facturacion/daily-aggregation/range
Obtener agregación de facturación en rango de fechas.

**Permisos**: ADMIN

**Query Parameters**:
- `startDate` (LocalDate, requerido): Fecha inicio (formato: YYYY-MM-DD)
- `endDate` (LocalDate, requerido): Fecha fin (formato: YYYY-MM-DD)

---

## 16. Gestión de Archivos

### POST /file/upload/parte
Subir archivo de parte.

**Permisos**: ADMIN, WORKER

**Request**: Multipart form-data
- `file`: Archivo a subir

**Response 200 OK**:
```json
"parte_20241129_123456_documento.pdf"
```

---

### POST /file/upload/cliente
Subir archivo de cliente.

**Permisos**: ADMIN

**Request**: Multipart form-data
- `file`: Archivo a subir

---

### POST /file/upload/image
Subir imagen.

**Permisos**: ADMIN, WORKER

**Request**: Multipart form-data
- `file`: Archivo de imagen

---

### GET /file/download/parte/{filename}
Descargar archivo de parte.

**Permisos**: ADMIN, WORKER

**Response**: Archivo PDF

---

### GET /file/download/cliente/{filename}
Descargar archivo de cliente.

**Permisos**: ADMIN

**Response**: Archivo PDF

---

### GET /file/image/{filename}
Obtener imagen.

**Permisos**: ADMIN, WORKER

**Response**: Archivo de imagen (JPEG/PNG)

---

### DELETE /file/parte/{filename}
Eliminar archivo de parte.

**Permisos**: ADMIN

---

### DELETE /file/cliente/{filename}
Eliminar archivo de cliente.

**Permisos**: ADMIN

---

### DELETE /file/image/{filename}
Eliminar imagen.

**Permisos**: ADMIN

---

## 17. Estadísticas

### GET /statistics/dashboard
Obtener estadísticas del dashboard.

**Permisos**: ADMIN

**Response 200 OK**:
```json
{
  "totalClients": 150,
  "totalWorkers": 25,
  "totalPartes": 450,
  "totalRoutes": 85,
  "partesCompletados": 380,
  "partesPendientes": 50,
  "partesEnProgreso": 20,
  "facturacionMensual": 45000.00,
  "facturacionAnual": 450000.00,
  "clientesActivos": 142,
  "trabajadoresActivos": 23,
  "vehiculosActivos": 12
}
```

---

### GET /statistics/workers
Obtener estadísticas de todos los trabajadores.

**Permisos**: ADMIN

**Response 200 OK**:
```json
[
  {
    "workerId": 2,
    "workerName": "Juan Pérez",
    "totalPartes": 45,
    "partesCompletados": 40,
    "partesPendientes": 3,
    "partesEnProgreso": 2,
    "totalRutas": 20,
    "horasTrabajadas": 180.5,
    "facturacionGenerada": 8500.00
  }
]
```

---

### GET /statistics/workers/{workerId}
Obtener estadísticas de un trabajador específico.

**Permisos**: ADMIN

**Path Parameters**:
- `workerId` (Long): ID del trabajador

---

## 18. Exportación de Datos

### GET /export/partes/excel
Exportar partes a Excel.

**Permisos**: ADMIN

**Response**: Archivo Excel (.xlsx)

---

### GET /export/partes/pdf
Exportar partes a PDF.

**Permisos**: ADMIN

**Response**: Archivo PDF

---

### GET /export/clientes/excel
Exportar clientes a Excel.

**Permisos**: ADMIN

**Response**: Archivo Excel (.xlsx)

---

## 19. Plantillas de Partes

### GET /parte-templates
Listar todas las plantillas.

**Permisos**: ADMIN

**Response 200 OK**:
```json
[
  {
    "id": 1,
    "customerId": 1,
    "title": "Revisión Trimestral Extintores",
    "description": "Plantilla para revisión trimestral",
    "type": "Mantenimiento",
    "categoria": "Extintores",
    "frequency": "TRIMESTRAL",
    "active": true
  }
]
```

---

### GET /parte-templates/{id}
Obtener plantilla por ID.

**Permisos**: ADMIN

---

### GET /parte-templates/customer/{customerId}
Obtener plantillas por cliente.

**Permisos**: ADMIN

---

### POST /parte-templates
Crear plantilla.

**Permisos**: ADMIN

**Request Body**:
```json
{
  "customerId": 1,
  "title": "Revisión Anual BIE",
  "description": "Plantilla para revisión anual de BIEs",
  "type": "Mantenimiento",
  "categoria": "BIE",
  "frequency": "ANUAL",
  "active": true
}
```

---

### PUT /parte-templates/{id}
Actualizar plantilla.

**Permisos**: ADMIN

---

### DELETE /parte-templates/{id}
Eliminar plantilla.

**Permisos**: ADMIN

---

### POST /parte-templates/{id}/create-parte
Crear parte desde plantilla.

**Permisos**: ADMIN

**Path Parameters**:
- `id` (Long): ID de la plantilla

**Query Parameters**:
- `date` (LocalDate, requerido): Fecha del parte (formato: YYYY-MM-DD)

**Response 201 Created**: Objeto Parte creado

---

## 20. Reportes Avanzados

### GET /reports/advanced
Generar reporte avanzado.

**Permisos**: ADMIN

**Query Parameters**:
- `startDate` (LocalDate, requerido): Fecha inicio (formato: YYYY-MM-DD)
- `endDate` (LocalDate, requerido): Fecha fin (formato: YYYY-MM-DD)

**Response 200 OK**:
```json
{
  "dateRange": {
    "startDate": "2024-11-01",
    "endDate": "2024-11-30"
  },
  "totalPartes": 95,
  "partesCompletados": 80,
  "partesPendientes": 10,
  "partesEnProgreso": 5,
  "totalFacturacion": 18500.00,
  "customerReport": [
    {
      "customerId": 1,
      "customerName": "Empresa ABC S.L.",
      "totalPartes": 15,
      "completedPartes": 13,
      "totalAmount": 2250.00
    }
  ],
  "workerReport": [
    {
      "workerId": 2,
      "workerName": "Juan Pérez",
      "totalPartes": 25,
      "completedPartes": 23,
      "totalHours": 48.5
    }
  ],
  "dailyReport": [
    {
      "date": "2024-11-01",
      "totalPartes": 3,
      "completedPartes": 3,
      "totalAmount": 600.00
    }
  ]
}
```

---

## 21. Auditoría

### GET /audit/{entityName}
Obtener historial de auditoría de una entidad.

**Permisos**: ADMIN

**Path Parameters**:
- `entityName` (String): Nombre de la entidad (ej: "Customer", "Parte", "User")

**Response 200 OK**:
```json
[
  {
    "entityName": "Customer",
    "entityId": 1,
    "revisionType": "UPDATE",
    "revisionDate": "2024-11-29T10:30:00",
    "modifiedBy": "admin@example.com",
    "changes": {
      "phone": {
        "oldValue": "+34912345678",
        "newValue": "+34912345679"
      }
    }
  }
]
```

---

### GET /audit/{entityName}/{entityId}
Obtener historial de auditoría de una entidad específica.

**Permisos**: ADMIN

**Path Parameters**:
- `entityName` (String): Nombre de la entidad
- `entityId` (Long): ID de la entidad

---

## Enumeraciones del Sistema

### Role
- `ADMIN`
- `WORKER`

### ParteState
- `Pendiente`
- `EnProgreso`
- `Finalizado`
- `Cancelado`

### ParteType
- `Mantenimiento`
- `Instalacion`
- `Reparacion`
- `Inspeccion`

### Categoria
- `Extintores`
- `BIE`
- `Iluminacion`
- `Senalizacion`
- `DeteccionIncendios`
- `Otros`

### Frequency
- `DIARIO`
- `SEMANAL`
- `MENSUAL`
- `TRIMESTRAL`
- `SEMESTRAL`
- `ANUAL`

### CoordinationMethod
- `COORDINAR_HORARIOS`
- `ACUDIR_DIRECTAMENTE`
- `LLAMAR_ANTES`

### ContractType
- `ANUAL`
- `BIENAL`
- `PUNTUAL`

### RevisionFrequency
- `MENSUAL`
- `TRIMESTRAL`
- `SEMESTRAL`
- `ANUAL`

### Rate
- `NORMAL`
- `REDUCIDA`
- `ESPECIAL`

### AlertState
- `PENDING`
- `IN_PROGRESS`
- `RESOLVED`
- `DISMISSED`

### ReportStatus
- `PENDING`
- `IN_PROGRESS`
- `COMPLETED`
- `CANCELLED`

### ReportType
- `MAINTENANCE`
- `INSTALLATION`
- `REPAIR`
- `INSPECTION`

### VehicleType
- `FURGONETA`
- `CAMION`
- `TURISMO`

### VehicleFuel
- `GASOLINA`
- `DIESEL`
- `ELECTRICO`
- `HIBRIDO`

---

## Códigos de Error HTTP

- **200 OK**: Solicitud exitosa
- **201 Created**: Recurso creado exitosamente
- **204 No Content**: Solicitud exitosa sin contenido de respuesta
- **400 Bad Request**: Solicitud incorrecta (error en los datos enviados)
- **401 Unauthorized**: No autenticado o token inválido
- **403 Forbidden**: No autorizado (sin permisos para la acción)
- **404 Not Found**: Recurso no encontrado
- **500 Internal Server Error**: Error interno del servidor

---

## Estructura de Respuesta Estándar

Todas las respuestas (excepto archivos y algunas específicas) siguen este formato:

**Éxito**:
```json
{
  "ok": true,
  "data": { /* datos solicitados */ },
  "message": "Mensaje opcional"
}
```

**Error**:
```json
{
  "ok": false,
  "error": "Descripción del error",
  "message": "Mensaje adicional opcional"
}
```

---

## Notas de Implementación

1. **Autenticación JWT**: El token debe incluirse en el header `Authorization: Bearer {token}` en todos los endpoints protegidos.

2. **Paginación**: Algunos endpoints soportan paginación con los parámetros `page` y `limit`.

3. **Fechas**: Todas las fechas se manejan en formato ISO 8601 (`YYYY-MM-DD` para LocalDate, `YYYY-MM-DDTHH:mm:ss` para LocalDateTime).

4. **Eliminación**: La mayoría de eliminaciones son lógicas (soft delete), marcando el campo `eliminado` o `active` en lugar de eliminar el registro.

5. **Auditoría**: Todas las entidades principales tienen auditoría automática mediante Hibernate Envers.

6. **CORS**: El backend está configurado para aceptar peticiones desde los orígenes especificados en la configuración de seguridad.

7. **Archivos**: Los archivos se almacenan en el directorio `uploads/` del servidor y se sirven a través de los endpoints de descarga correspondientes.

---

## Contacto y Soporte

Para más información sobre la API o para reportar problemas, contactar con el equipo de desarrollo.

**Versión de la API**: 1.0  
**Última actualización**: Noviembre 2024

