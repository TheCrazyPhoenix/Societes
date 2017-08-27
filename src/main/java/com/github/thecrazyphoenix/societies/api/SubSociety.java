package com.github.thecrazyphoenix.societies.api;

import com.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import com.github.thecrazyphoenix.societies.api.permission.SocietyPermission;
import com.github.thecrazyphoenix.societies.api.rank.Taxable;

public interface SubSociety extends Society, Taxable, PermissionHolder<SocietyPermission> {
    // Empty
}
