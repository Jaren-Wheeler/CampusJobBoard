/**
 * Barrel file for the Super Admin module.
 * Exposes all public functions used by the dashboard:
 *  - access control utilities
 *  - API request helpers
 *  - UI rendering methods
 *  - event handler bindings
 *
 * Any file within the superadmin/ folder can import from this
 * module instead of referencing individual files.
 */

export * from "./access-control.js";
export * from "./admin-api.js";
export * from "./admin-ui.js";
export * from "./admin-events.js";
