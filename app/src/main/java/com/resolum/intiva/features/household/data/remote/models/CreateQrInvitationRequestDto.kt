package com.resolum.intiva.features.household.data.remote.models

/**
 * Request body for creating a QR-based open invitation (no specific user targeted).
 * Intentionally empty so Gson serializes it as `{}`, avoiding null constraint violations
 * on `user_invited_id` in the backend.
 */
class CreateQrInvitationRequestDto
