# Diagramas UML — Entreprenly Platform

Diagramas de clases del dominio (DDD) por **bounded context**, en formato **PlantUML**.
Generados a partir del código real del backend (Spring Boot 3 / Java 21).

| Archivo | Contenido |
|---------|-----------|
| [`00-context-map.puml`](00-context-map.puml) | Mapa de contextos: los 6 BC y sus dependencias |
| [`01-iam.puml`](01-iam.puml) | IAM — `User`, `Role`, `Roles` |
| [`02-inventory.puml`](02-inventory.puml) | Inventory — productos y lotes (unidad/peso), alertas |
| [`03-chatbot.puml`](03-chatbot.puml) | Chatbot — conversación, mensajes, pedidos, sesión WhatsApp |
| [`04-sales.puml`](04-sales.puml) | Sales — ventas, ítems, caja |
| [`05-subscription.puml`](05-subscription.puml) | Subscription — planes, suscripciones, pagos |
| [`06-profile.puml`](06-profile.puml) | Profile — perfil, preferencias, notificaciones |

## Cómo renderizarlos

Cualquiera de estas opciones:

1. **Online (más rápido):** abre <https://www.plantuml.com/plantuml/uml> y pega el contenido del `.puml`.
2. **VS Code:** instala la extensión **"PlantUML"** (jebbs), abre el archivo y pulsa `Alt+D` para previsualizar.
3. **IntelliJ IDEA:** instala el plugin **PlantUML Integration**; el preview aparece al abrir el archivo.
4. **Línea de comandos** (requiere Java + Graphviz):
   ```bash
   java -jar plantuml.jar docs/uml/*.puml      # genera un PNG por archivo
   ```

## Convención de estereotipos

- `<<aggregate root>>` — raíz de agregado (entidad principal del contexto)
- `<<entity>>` — entidad con identidad propia dentro de un agregado
- `<<value object>>` — objeto de valor inmutable (en el código, un `record`)
- `<<enumeration>>` — enum del dominio

Relaciones: `*--` composición · `-->` asociación/dirección · `..>` dependencia (incl. ACL entre contextos).
