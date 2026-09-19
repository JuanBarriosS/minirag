# 🧠 MiniRAG Académico

<div align="center">

**Asistente inteligente con Spring Boot + Spring AI que responde preguntas académicas usando RAG (Retrieval-Augmented Generation)**

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.16-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring AI](https://img.shields.io/badge/Spring%20AI-1.1.4-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Groq](https://img.shields.io/badge/Groq-GPT--OSS--20B-F55036?style=for-the-badge&logo=lightning&logoColor=white)
![H2](https://img.shields.io/badge/H2-Database-1E88E5?style=for-the-badge&logo=databricks&logoColor=white)

</div>

---

## 📖 ¿Qué hace este proyecto?

MiniRAG Académico permite hacer preguntas sobre un conjunto de documentos (Spring Boot, microservicios, Spring AI, REST) y obtener respuestas **basadas únicamente en esos documentos**, evitando que el modelo invente información fuera de ese contexto.

❓ "¿Qué es RAG?" → 🔍 Busca en los documentos → 🤖 Genera respuesta fundamentada


---

## 🏗️ Arquitectura

El proyecto implementa el patrón **RAG (Retrieval-Augmented Generation)**: antes de responder, el sistema busca los fragmentos de texto más relevantes para la pregunta y se los entrega al modelo como contexto, en lugar de dejarlo responder solo con su conocimiento general.

```
┌─────────────┐      POST /api/chat      ┌──────────────────┐
│  Frontend    │ ───────────────────────▶ │  ChatController   │
│ HTML/CSS/JS  │                          └────────┬──────────┘
└─────────────┘                                    │
                                                     ▼
                                          ┌──────────────────┐
                                          │   ChatService     │
                                          │ (QuestionAnswer   │
                                          │    Advisor)        │
                                          └────────┬──────────┘
                                     ┌──────────────┴──────────────┐
                                     ▼                              ▼
                          ┌────────────────────┐        ┌──────────────────┐
                          │   VectorStore       │        │       Groq        │
                          │ (SimpleVectorStore)  │        │   GPT-OSS-20B      │
                          │  Búsqueda semántica  │        │  Modelo generativo │
                          └──────────┬──────────┘        └──────────────────┘

                                                     ▼
                                            Fragmentos relevantes
                                  (documentos .txt → chunks → embeddings)
```
  


### Flujo de una pregunta

1. El usuario escribe una pregunta en el frontend.
2. `ChatController` recibe la petición y la delega a `ChatService`.
3. `QuestionAnswerAdvisor` convierte la pregunta en un embedding y busca en el `VectorStore` los fragmentos más similares (`topK` + `similarityThreshold`).
4. Esos fragmentos se agregan como contexto al prompt enviado al LLM (**Groq / GPT-OSS-20B**).
5. La respuesta generada se guarda en **H2** (historial) y se devuelve al usuario.

---

## 🛠️ Stack tecnológico

| Categoría | Tecnología |
|---|---|
| **Lenguaje** | Java 21 |
| **Framework** | Spring Boot 3.5.16 |
| **IA / RAG** | Spring AI 1.1.4 |
| **Modelo generativo (LLM)** | Groq — `openai/gpt-oss-20b` (API compatible con OpenAI) |
| **Embeddings** | Transformers ONNX — `intfloat/e5-small-v2` (local, sin API externa) |
| **Vector Store** | `SimpleVectorStore` (en memoria) |
| **Persistencia** | H2 + Spring Data JPA |
| **Frontend** | HTML, CSS, JavaScript (Vanilla) |
| **Gestión de dependencias** | Maven |

---

## 📂 Estructura del proyecto
```
minirag/
├── src/main/java/com/tecnologico/minirag/
│ ├── config/AiConfig.java # Configuración del VectorStore
│ ├── controller/ChatController.java # Endpoints REST
│ ├── service/
│ │ ├── ChatService.java # Lógica de RAG (QuestionAnswerAdvisor)
│ │ └── DocumentService.java # Carga y fragmentación de documentos
│ ├── model/Consulta.java # Entidad JPA (historial)
│ └── repository/ConsultaRepository.java
├── src/main/resources/
│ ├── documentos/ # Base de conocimiento (.txt)
│ ├── application.properties
│ └── static/ # Frontend
└── pom.xml
```

---

## 🚀 Cómo ejecutar el proyecto

### 1. Clonar el repositorio
```bash
git clone https://github.com/JuanBarriosS/minirag.git
cd minirag
```

### 2. Configurar tu API Key de Groq
Crea una variable de entorno con tu clave (nunca la escribas directamente en el código):
```bash
export GROQ_API_KEY="tu_clave_aqui"
```

### 3. Ejecutar la aplicación
```bash
./mvnw spring-boot:run
```

### 4. Abrir en el navegador

http://localhost:8080


---

## 🔌 Endpoints disponibles

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/api/chat` | Envía una pregunta y recibe una respuesta basada en RAG |
| `GET` | `/api/consultas` | Consulta el historial de preguntas y respuestas |
| `GET` | `/api/salud` | Verifica el estado de la aplicación |

**Ejemplo de petición:**
```json
POST /api/chat
{
  "pregunta": "¿Qué es RAG?"
}
```

---

## 👥 Autor

- Juan Camilo Barrios

---

<div align="center">

