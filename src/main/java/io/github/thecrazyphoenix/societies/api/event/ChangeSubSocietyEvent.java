package io.github.thecrazyphoenix.societies.api.event;

import io.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import io.github.thecrazyphoenix.societies.api.permission.SocietyPermission;
import io.github.thecrazyphoenix.societies.api.society.SubSociety;

public interface ChangeSubSocietyEvent extends ChangeAccountHolderEvent {
    /**
     * Retrieves the sub-society affected by this event.
     * This represents the old state of the sub-society, except for the Create event.
     * @return The sub-society.
     */
    SubSociety getSubSociety();

    /**
     * Called when a sub-society is created.
     */
    interface Create extends ChangeSubSocietyEvent, ChangeAccountHolderEvent.Create {}

    /**
     * Called when a sub-society is destroyed (i.e. deleted or the society becomes independent)
     */
    interface Destroy extends ChangeSubSocietyEvent, ChangeAccountHolderEvent.Destroy {}

    /**
     * Called when a sub-society's global permissions are changed.
     */
    interface ChangePermission extends ChangeSubSocietyEvent, ChangePermissionEvent {
        @Override
        SocietyPermission getChangedPermission();
    }

    /**
     * Called when a sub-society's permissions inside a claim are changed.
     */
    interface ChangeClaimPermission extends ChangeSubSocietyEvent, ChangePermissionEvent, ChangeClaimEvent {
        @Override
        ClaimPermission getChangedPermission();
    }
}
