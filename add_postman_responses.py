#!/usr/bin/env python3
"""
Script para agregar responses de ejemplo a la colección de Postman
"""
import json
import sys

def create_success_response(name, status_code, body_json, method="GET", path="/example"):
    """Crea un response de ejemplo exitoso"""
    return {
        "name": name,
        "originalRequest": {
            "method": method,
            "header": [{"key": "Content-Type", "value": "application/json"}],
            "url": {
                "raw": f"{{{{base_url}}}}{path}",
                "host": ["{{base_url}}"],
                "path": path.strip("/").split("/")
            }
        },
        "status": "OK" if status_code == 200 else ("Created" if status_code == 201 else "No Content"),
        "code": status_code,
        "_postman_previewlanguage": "json",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "cookie": [],
        "body": json.dumps(body_json, indent=2, ensure_ascii=False)
    }

def create_error_response(name, status_code, error_message, method="GET", path="/example"):
    """Crea un response de error"""
    return {
        "name": name,
        "originalRequest": {
            "method": method,
            "header": [{"key": "Content-Type", "value": "application/json"}],
            "url": {
                "raw": f"{{{{base_url}}}}{path}",
                "host": ["{{base_url}}"],
                "path": path.strip("/").split("/")
            }
        },
        "status": "Unauthorized" if status_code == 401 else ("Forbidden" if status_code == 403 else "Not Found"),
        "code": status_code,
        "_postman_previewlanguage": "json",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "cookie": [],
        "body": json.dumps({"ok": False, "error": error_message}, indent=2, ensure_ascii=False)
    }

def add_responses_to_item(item, endpoint_config):
    """Agrega responses de ejemplo a un item de Postman"""
    if "request" not in item:
        return
    
    method = item["request"].get("method", "GET")
    url_path = "/" + "/".join(item["request"]["url"].get("path", []))
    
    responses = []
    
    # Response exitoso
    if "success_response" in endpoint_config:
        responses.append(create_success_response(
            endpoint_config["success_response"]["name"],
            endpoint_config["success_response"]["code"],
            endpoint_config["success_response"]["body"],
            method,
            url_path
        ))
    
    # Response de error
    if "error_response" in endpoint_config:
        responses.append(create_error_response(
            endpoint_config["error_response"]["name"],
            endpoint_config["error_response"]["code"],
            endpoint_config["error_response"]["message"],
            method,
            url_path
        ))
    
    if responses:
        item["response"] = responses

# Configuraciones de responses por endpoint
RESPONSE_CONFIGS = {
    "Listar Usuarios": {
        "success_response": {
            "name": "Lista de Usuarios",
            "code": 200,
            "body": {
                "ok": True,
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
                            "activo": True,
                            "createdDate": "2024-01-01T00:00:00",
                            "updatedDate": "2024-11-29T10:00:00"
                        },
                        {
                            "id": 2,
                            "name": "Juan Pérez",
                            "code": "WRK001",
                            "email": "juan.perez@example.com",
                            "phone": "+34611222333",
                            "role": "WORKER",
                            "photo": "https://ionicframework.com/docs/img/demos/avatar.svg",
                            "activo": True,
                            "createdDate": "2024-01-15T00:00:00",
                            "updatedDate": "2024-11-29T10:00:00"
                        }
                    ]
                }
            }
        },
        "error_response": {
            "name": "No Autorizado",
            "code": 403,
            "message": "No autorizado - Se requiere rol ADMIN"
        }
    },
    "Listar Clientes": {
        "success_response": {
            "name": "Lista de Clientes",
            "code": 200,
            "body": {
                "ok": True,
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
                            "active": True,
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
        }
    }
}

if __name__ == "__main__":
    print("Script para agregar responses a Postman Collection")
    print("Este script necesita ser integrado manualmente con la colección")
    print("Por favor, use las funciones de actualización directa del archivo JSON")

