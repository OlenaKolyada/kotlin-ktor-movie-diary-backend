# Vulpecula Postman Demo

Import into Postman:

- `Vulpecula.postman_collection.json`
- `Vulpecula.postman_environment.json`

Select environment: `Vulpecula Local`.

Run order:

1. `Health / GET /health`
2. `Happy Path - Real DB / 01 Create Entry`
3. `Happy Path - Real DB / 02 Read Entry`
4. `Happy Path - Real DB / 03 Update Entry`
5. `Happy Path - Real DB / 04 Search Entries`
6. `Happy Path - Real DB / 05 Delete Entry`

The create request saves `entryId` and `entryLock` into the Postman environment.
The update request saves `updatedEntryLock` for delete.

Use the `Errors` folder to show validation and concurrency failures.
