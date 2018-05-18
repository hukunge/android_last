package com.test.ok.permission.helper;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Permissions helper for apps built against API < 23, which do not need runtime permissions.
 */
class LowApiPermissionsHelper extends PermissionHelper<Object> {

    public LowApiPermissionsHelper(@NonNull Object host) {
        super(host);
    }

    @Override
    public void directRequestPermissions(int requestCode, @NonNull String... perms) {
        throw new IllegalStateException("Should never be requesting permissions on API < 23!");
    }

    @Override
    public Context getContext() {
        return null;
    }
}
