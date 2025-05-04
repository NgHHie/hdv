# Technician API Endpoints

Base URL: `/api/v1/technicians`

## List of endpoints:

### 1. Create a new technician

- **Method:** POST
- **Endpoint:** `/api/v1/technicians`
- **Request Body:** `TechnicianRequest` object in JSON format.

---

### 2. Get technician by ID

- **Method:** GET
- **Endpoint:** `/api/v1/technicians/{id}`
- **Path Variable:** `id` (Long)

---

### 3. Get technician by email

- **Method:** GET
- **Endpoint:** `/api/v1/technicians/email/{email}`
- **Path Variable:** `email` (String)

---

### 4. Get all technicians

- **Method:** GET
- **Endpoint:** `/api/v1/technicians`

---

### 5. Get all active technicians

- **Method:** GET
- **Endpoint:** `/api/v1/technicians/active`

---

### 6. Get technicians by specialization

- **Method:** GET
- **Endpoint:** `/api/v1/technicians/specialization/{specialization}`
- **Path Variable:** `specialization` (String)

---

### 7. Update technician

- **Method:** PUT
- **Endpoint:** `/api/v1/technicians/{id}`
- **Path Variable:** `id` (Long)
- **Request Body:** `TechnicianRequest` object in JSON format.

---

### 8. Delete technician

- **Method:** DELETE
- **Endpoint:** `/api/v1/technicians/{id}`
- **Path Variable:** `id` (Long)

---

### 9. Toggle technician active status

- **Method:** PATCH
- **Endpoint:** `/api/v1/technicians/{id}/toggle-status`
- **Path Variable:** `id` (Long)