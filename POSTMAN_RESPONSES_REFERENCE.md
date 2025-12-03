# Referencia de Responses para Postman Collection

Este documento contiene todas las estructuras de respuesta de ejemplo que deben agregarse a la colección de Postman.

## Estructura Base de Response

Todos los responses exitosos siguen esta estructura:
```json
{
  "ok": true,
  "data": { /* datos específicos */ },
  "message": "Mensaje opcional"
}
```

Los responses de error siguen esta estructura:
```json
{
  "ok": false,
  "error": "Descripción del error",
  "message": "Mensaje adicional opcional"
}
```

## Responses por Endpoint

### 1. Autenticación

#### POST /auth/login - Response 200 OK
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
      "activo": true,
      "createdDate": "2024-01-01T00:00:00",
      "updatedDate": "2024-11-29T10:00:00"
    }
  }
}
```

#### POST /auth/login - Response 401 Unauthorized
```json
{
  "ok": false,
  "error": "Credenciales incorrectas"
}
```

### 2. Usuarios

#### GET /user/list - Response 200 OK
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
        "activo": true,
        "createdDate": "2024-01-01T00:00:00",
        "updatedDate": "2024-11-29T10:00:00"
      }
    ]
  }
}
```

#### GET /user/worker - Response 200 OK
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
        "photo": "https://ionicframework.com/docs/img/demos/avatar.svg",
        "activo": true
      }
    ]
  }
}
```

#### GET /user/{id} - Response 200 OK
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
      "photo": "https://ionicframework.com/docs/img/demos/avatar.svg",
      "activo": true,
      "createdDate": "2024-01-01T00:00:00",
      "updatedDate": "2024-11-29T10:00:00"
    }
  }
}
```

#### GET /user - Response 200 OK (Perfil actual)
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
      "photo": "https://ionicframework.com/docs/img/demos/avatar.svg",
      "activo": true
    }
  }
}
```

#### POST /user/create - Response 201 Created
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
      "photo": "https://ionicframework.com/docs/img/demos/avatar.svg",
      "activo": true,
      "createdDate": "2024-11-29T10:00:00",
      "updatedDate": "2024-11-29T10:00:00"
    }
  },
  "message": "Usuario creado exitosamente"
}
```

#### PUT /user/update - Response 200 OK
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

#### DELETE /user/delete/{id} - Response 200 OK
```json
{
  "ok": true,
  "data": {
    "message": "Usuario eliminado exitosamente"
  }
}
```

### 3. Clientes

#### GET /customers - Response 200 OK
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
        "description": "Cliente del sector industrial",
        "gestiona": "Juan Pérez",
        "photo": "foto.jpg",
        "startDate": "2024-01-01",
        "endDate": "2024-12-31",
        "type": "ANUAL",
        "contractSystems": ["Extintores", "BIE"],
        "averageTime": 120,
        "delegation": "Madrid",
        "revisionFrequency": "TRIMESTRAL",
        "rate": "NORMAL",
        "mi": 0,
        "tipo": "Normal",
        "total": 1500.00,
        "documents": [
          {
            "name": "Contrato_2024.pdf",
            "url": "https://example.com/contratos/contrato1.pdf"
          }
        ],
        "createdDate": "2024-01-01T00:00:00",
        "updatedDate": "2024-11-29T10:00:00"
      }
    ]
  }
}
```

#### GET /customers/{id} - Response 200 OK
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

#### POST /customers/create - Response 201 Created
```json
{
  "ok": true,
  "data": {
    "customer": {
      "id": 2,
      "name": "Nueva Empresa S.L.",
      "code": "CLI002",
      "active": true,
      "createdDate": "2024-11-29T10:00:00",
      "updatedDate": "2024-11-29T10:00:00"
    }
  },
  "message": "Cliente creado exitosamente"
}
```

#### PUT /customers/update - Response 200 OK
```json
{
  "ok": true,
  "data": {
    "customer": {
      "id": 1,
      "name": "Empresa ABC S.L. Actualizada",
      "code": "CLI001",
      "active": true
    }
  },
  "message": "Cliente actualizado exitosamente"
}
```

#### DELETE /customers/{id} - Response 200 OK
```json
{
  "ok": true,
  "data": {
    "message": "Cliente eliminado exitosamente"
  }
}
```

### 4. Partes

#### GET /partes - Response 200 OK (con paginación)
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
        "articulos": [],
        "createdDate": "2024-11-01T00:00:00",
        "updatedDate": "2024-11-29T10:00:00"
      }
    ],
    "totalPages": 10,
    "totalItems": 95
  }
}
```

#### GET /partes/{id} - Response 200 OK
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
      "address": "Calle Principal 123, Madrid",
      "state": "Pendiente",
      "type": "Mantenimiento",
      "categoria": "Extintores",
      "asignado": false,
      "createdDate": "2024-11-01T00:00:00",
      "updatedDate": "2024-11-29T10:00:00"
    }
  }
}
```

#### GET /partes/worker/{workerId} - Response 200 OK
```json
{
  "ok": true,
  "data": {
    "partes": [
      {
        "id": 1,
        "title": "Mantenimiento Extintores",
        "date": "2024-11-30",
        "state": "EnProgreso",
        "customer": {
          "name": "Empresa ABC S.L."
        }
      }
    ]
  }
}
```

#### POST /partes/create - Response 201 Created
```json
{
  "ok": true,
  "data": {
    "parte": {
      "id": 3,
      "title": "Revisión sistema contra incendios",
      "date": "2024-12-05",
      "state": "Pendiente",
      "createdDate": "2024-11-29T10:00:00",
      "updatedDate": "2024-11-29T10:00:00"
    }
  },
  "message": "Parte creado exitosamente"
}
```

#### POST /partes/update - Response 200 OK
```json
{
  "ok": true,
  "data": {
    "parte": {
      "id": 1,
      "title": "Revisión Extintores - Cliente ABC [Actualizado]",
      "state": "EnProgreso"
    }
  },
  "message": "Parte actualizado exitosamente"
}
```

#### PUT /partes/{id}/status - Response 200 OK
```json
{
  "ok": true,
  "data": {
    "parte": {
      "id": 1,
      "state": "Finalizado"
    }
  },
  "message": "Estado actualizado exitosamente"
}
```

#### DELETE /partes/{id} - Response 200 OK
```json
{
  "ok": true,
  "data": {
    "message": "Parte eliminado exitosamente"
  }
}
```

### 5. Rutas

#### GET /rutas - Response 200 OK
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
          }
        ],
        "comentarios": "Ruta con 5 partes asignados",
        "herramientas": [],
        "eliminado": false,
        "createdDate": "2024-11-25T00:00:00",
        "updatedDate": "2024-11-29T10:00:00"
      }
    ]
  }
}
```

#### GET /rutas/{id} - Response 200 OK
```json
{
  "ok": true,
  "data": {
    "ruta": {
      "id": 1,
      "date": "2024-12-01",
      "state": "Pendiente",
      "encargado": {
        "id": 2,
        "name": "Juan Pérez"
      }
    }
  }
}
```

#### GET /rutas/{routeId}/partes - Response 200 OK
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

#### POST /rutas/create - Response 201 Created
```json
{
  "ok": true,
  "data": {
    "ruta": {
      "id": 2,
      "date": "2024-12-10",
      "state": "Pendiente",
      "createdDate": "2024-11-29T10:00:00",
      "updatedDate": "2024-11-29T10:00:00"
    }
  },
  "message": "Ruta creada exitosamente"
}
```

#### POST /rutas/{id}/asignarPartes - Response 200 OK
```json
{
  "ok": true,
  "data": {
    "message": "Partes asignados exitosamente"
  }
}
```

### 6. Estadísticas

#### GET /statistics/dashboard - Response 200 OK
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

#### GET /statistics/workers - Response 200 OK
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

### 7. Reportes

#### GET /report/list - Response 200 OK
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
    "observations": "Todo correcto",
    "createdDate": "2024-11-29T08:00:00",
    "updatedDate": "2024-11-29T10:00:00"
  }
]
```

#### POST /report - Response 201 Created
```json
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
  "observations": "Todo correcto",
  "createdDate": "2024-11-29T10:00:00",
  "updatedDate": "2024-11-29T10:00:00"
}
```

### 8. Facturación

#### GET /facturacion - Response 200 OK
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
        "status": "PAID",
        "createdDate": "2024-11-29T10:00:00",
        "updatedDate": "2024-11-29T10:00:00"
      }
    ]
  }
}
```

#### GET /facturacion/daily-aggregation - Response 200 OK
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

### 9. Archivos

#### POST /file/upload/parte - Response 200 OK
```json
"parte_20241129_123456_documento.pdf"
```

#### GET /file/download/parte/{filename} - Response 200 OK
Archivo PDF (binary)

### 10. Alertas

#### GET /alertas - Response 200 OK
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
        "createdDate": "2024-11-28T10:30:00",
        "updatedDate": "2024-11-28T10:30:00"
      }
    ]
  }
}
```

### 11. Checkins

#### GET /checkin/list - Response 200 OK
```json
[
  {
    "id": 1,
    "reportId": 1,
    "userId": 2,
    "latitude": 40.416775,
    "longitude": -3.703790,
    "timestamp": "2024-11-29T08:30:00",
    "type": "CHECK_IN",
    "createdDate": "2024-11-29T08:30:00"
  }
]
```

### 12. Comentarios

#### GET /comment/parte/{parteId} - Response 200 OK
```json
[
  {
    "id": 1,
    "parteId": 1,
    "userId": 2,
    "userName": "Juan Pérez",
    "text": "Cliente requiere revisión adicional",
    "createdDate": "2024-11-29T10:00:00",
    "updatedDate": "2024-11-29T10:00:00"
  }
]
```

## Notas Importantes

1. **Todos los responses exitosos** incluyen el campo `"ok": true`
2. **Todos los responses de error** incluyen `"ok": false` y un campo `"error"`
3. **Fechas** se formatean como strings ISO 8601: `"2024-11-29T10:00:00"`
4. **Algunos endpoints** retornan arrays directamente (sin wrapper `data`)
5. **Endpoints de archivos** retornan strings con el nombre del archivo o el archivo binario
6. **Endpoints de eliminación** retornan `204 No Content` o `200 OK` con mensaje

## Cómo Agregar Responses a Postman

1. Abre Postman
2. Selecciona el request
3. Haz clic en "Examples" en la parte inferior
4. Haz clic en "Add Example"
5. Copia el JSON de respuesta correspondiente
6. Guarda el ejemplo

O usa el formato JSON de la colección directamente agregando el array `"response"` con los ejemplos.

