package org.cmas.service;

import android.content.Context;

public interface CodeService {

    String checkCode(Context context, String username, String code);
}
