# mock-order-service

Mock order/refund service used to develop and test the engineering assistant. Not a real service and not meant
to run, but it compiles (`mvn compile`, Java 11+). It is self-contained: stored-procedure result handling, caller auth attributes and id hashing live in
`mock-order-persistence` under `com.example.mock.order.common` (no external shared library).
See `../README.md` for the planted scenarios.

Modules: `mock-order-api` (controllers, config) · `mock-order-service` (op_code handlers, services, translators,
swagger API/models) · `mock-order-persistence` (DAOs, jOOQ routines, SP result DTOs) · `mock-order-entity`.
Stored procedures and triggers: `db/sp/`. Tables: `db/schema/`.
