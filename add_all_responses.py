#!/usr/bin/env python3
"""
Script para agregar todas las respuestas faltantes a la colección de Postman.
Este script lee el archivo JSON y agrega respuestas de ejemplo para todos los endpoints que no las tienen.
"""

import json
import sys

def create_response_example(name, status, code, body, original_request):
    """Crea un objeto de respuesta de ejemplo para Postman"""
    return {
        "name": name,
        "originalRequest": original_request,
        "status": status,
        "code": code,
        "_postman_previewlanguage": "json",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "cookie": [],
        "body": body
    }

def add_responses_to_item(item):
    """Agrega respuestas a un item si no las tiene"""
    if "response" in item and len(item["response"]) == 0:
        # Determinar qué tipo de respuesta crear basado en el método y nombre
        method = item["request"].get("method", "GET")
        name = item.get("name", "")
        url_path = "/".join(item["request"]["url"].get("path", []))
        
        # Crear originalRequest básico
        original_request = {
            "method": method,
            "header": item["request"].get("header", []),
            "url": item["request"]["url"]
        }
        if "body" in item["request"]:
            original_request["body"] = item["request"]["body"]
        
        # Generar respuesta según el tipo de endpoint
        if method == "GET":
            if "list" in name.lower() or "Listar" in name:
                # Respuesta de lista
                body = '{\n  "ok": true,\n  "data": {\n    "items": []\n  }\n}'
            else:
                # Respuesta de objeto individual
                body = '{\n  "ok": true,\n  "data": {\n    "item": {}\n  }\n}'
            status = "OK"
            code = 200
        elif method == "POST":
            body = '{\n  "ok": true,\n  "data": {\n    "item": {}\n  },\n  "message": "Creado exitosamente"\n}'
            status = "Created"
            code = 201
        elif method == "PUT":
            body = '{\n  "ok": true,\n  "data": {\n    "item": {}\n  },\n  "message": "Actualizado exitosamente"\n}'
            status = "OK"
            code = 200
        elif method == "DELETE":
            body = '{\n  "ok": true,\n  "message": "Eliminado exitosamente"\n}'
            status = "OK"
            code = 200
        else:
            return  # No agregar respuesta para otros métodos
        
        item["response"] = [create_response_example(
            f"{name} - Respuesta",
            status,
            code,
            body,
            original_request
        )]
    
    # Procesar sub-items recursivamente
    if "item" in item:
        for sub_item in item["item"]:
            add_responses_to_item(sub_item)

def main():
    input_file = "extinguidor-api.postman_collection.json"
    output_file = "extinguidor-api.postman_collection.json"
    
    try:
        with open(input_file, 'r', encoding='utf-8') as f:
            collection = json.load(f)
        
        # Procesar todos los items
        if "item" in collection:
            for item in collection["item"]:
                add_responses_to_item(item)
        
        # Guardar el archivo actualizado
        with open(output_file, 'w', encoding='utf-8') as f:
            json.dump(collection, f, indent=2, ensure_ascii=False)
        
        print(f"✅ Respuestas agregadas exitosamente a {output_file}")
        
    except Exception as e:
        print(f"❌ Error: {e}", file=sys.stderr)
        sys.exit(1)

if __name__ == "__main__":
    main()

