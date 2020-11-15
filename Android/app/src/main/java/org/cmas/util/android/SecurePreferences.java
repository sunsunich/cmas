/*
 * Copyright (C) 2013, Daniel Abraham
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cmas.util.android;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import com.cmas.cmas_flutter.R;
import org.cmas.Globals;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Wrapper class for Android's {@link SharedPreferences} interface, which adds a layer of
 * encryption to the persistent storage and retrieval of sensitive key-value pairs of primitive
 * data types.
 * <p/>
 * This class provides important - but nevertheless imperfect - protection against simple attacks
 * by casual snoopers. It is crucial to remember that even encrypted data may still be susceptible
 * to attacks, especially on rooted or stolen devices!
 * <p/>
 * This class requires API level 8 (Android 2.2, a.k.a. "Froyo") or greater.
 *
 * @see <a href="http://www.codeproject.com/Articles/549119/Encryption-Wrapper-for-Android-SharedPreferences">CodeProject article</a>
 */
public class SecurePreferences implements SharedPreferences {

    private static boolean isSecure = false;
    private static SharedPreferences sFile;
    private static byte[] sKey;

    /**
     * Constructor.
     *
     * @param context the caller's context
     */
    public SecurePreferences(Context context) {
        // Proxy design pattern
        if (sFile == null) {
            sFile = context.getSharedPreferences(
                    context.getString(R.string.app_name),
                    Context.MODE_PRIVATE
            );
        }
        //  Log.d(SecurePreferences.class.getName(), "sFileSize=" + sFile.getAll().size());
        // Initialize encryption/decryption key
        if (isSecure) {
            try {
                final String key = generateAesKeyName(context);
                //   Log.d(SecurePreferences.class.getName(), "generateAesKeyName=" + String.valueOf(key));
                String value = sFile.getString(key, null);
                if (value == null) {
                    value = generateAesKeyValue();
                    //        Log.d(SecurePreferences.class.getName(), "generateAesKeyValue=" + String.valueOf(value));
                    sFile.edit().putString(key, value).commit();
                }
                sKey = decode(value);
                //   Log.d(SecurePreferences.class.getName(), "sKey=" + String.valueOf(sKey));
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private static String encode(byte[] input) {
        return Base64.encodeToString(input, Base64.NO_PADDING | Base64.NO_WRAP);
    }

    private static byte[] decode(String input) {
        return Base64.decode(input, Base64.NO_PADDING | Base64.NO_WRAP);
    }

    private static String generateAesKeyName(Context context) throws InvalidKeySpecException,
            NoSuchAlgorithmException {
        final char[] password = Globals.MOBILE_DB_PASS.toCharArray();
        final byte[] salt = Settings.Secure.getString(context.getContentResolver(),
                                                      Settings.Secure.ANDROID_ID).getBytes();

        // Number of PBKDF2 hardening rounds to use, larger values increase
        // computation time, you should select a value that causes
        // computation to take >100ms
        final int iterations = 1000;

        // Generate a 256-bit key
        final int keyLength = 256;

        final KeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
        return SecurePreferences.encode(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
                                                        .generateSecret(spec).getEncoded());
    }

    private static String generateAesKeyValue() throws NoSuchAlgorithmException {
        // Do *not* seed secureRandom! Automatically seeded from system entropy
        final SecureRandom random = new SecureRandom();

        // Use the largest AES key length which is supported by the OS
        final KeyGenerator generator = KeyGenerator.getInstance("AES");
        try {
            generator.init(256, random);
        } catch (Exception e) {
            try {
                generator.init(192, random);
            } catch (Exception e1) {
                generator.init(128, random);
            }
        }
        return SecurePreferences.encode(generator.generateKey().getEncoded());
    }

    private static String encrypt(String cleartext) {
        if (isSecure) {
            if (cleartext == null || cleartext.length() == 0) {
                return cleartext;
            }
            try {
                final Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(sKey, "AES"));
                return SecurePreferences.encode(cipher.doFinal(cleartext.getBytes("UTF-8")));
            } catch (Exception e) {
                Log.w(SecurePreferences.class.getName(), "encrypt", e);
                return null;
            }
        } else {
            return cleartext;
        }
    }

    private static String decrypt(String ciphertext) {
        if (isSecure) {
            if (ciphertext == null || ciphertext.length() == 0) {
                return ciphertext;
            }
            try {
                final Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(sKey, "AES"));
                return new String(cipher.doFinal(SecurePreferences.decode(ciphertext)), "UTF-8");
            } catch (Exception e) {
                Log.w(SecurePreferences.class.getName(), "decrypt", e);
                return null;
            }
        } else {
            return ciphertext;
        }
    }

    @Override
    public Map<String, ?> getAll() {
        final Map<String, ?> encryptedMap = sFile.getAll();
        if (isSecure) {
            final Map<String, String> decryptedMap = new HashMap<String, String>(encryptedMap.size());
            for (Entry<String, ?> entry : encryptedMap.entrySet()) {
                //  Log.d(SecurePreferences.class.getName(), "key=" + String.valueOf(entry.getKey()));
                // Log.d(SecurePreferences.class.getName(), "value=" + String.valueOf(entry.getValue()));
                try {
                    String decryptedKey = decrypt(entry.getKey());
                    if (decryptedKey != null) {
                        decryptedMap.put(decryptedKey,
                                         decrypt(entry.getValue().toString()));
                    }
                } catch (Exception e) {
                    // Ignore unencrypted key/value pairs
                }
            }
            return decryptedMap;
        } else {
            return encryptedMap;
        }
    }

    @Override
    public String getString(String key, String defaultValue) {
        final String encryptedValue =
                sFile.getString(encrypt(key), null);
        return (encryptedValue != null) ? decrypt(encryptedValue) : defaultValue;
    }

	@Override
	public Set<String> getStringSet(String key, Set<String> defaultValue)
	{
		final Set<String> encryptedValues=sFile.getStringSet(encrypt(key), null);
		if(encryptedValues!=null){
			Set<String> decrypted=new HashSet<String>();
			for(String value:encryptedValues){
				decrypted.add(decrypt(value));
			}
			return decrypted;
		}
		return defaultValue;
	}

	@Override
    public int getInt(String key, int defaultValue) {
        final String encryptedValue =
                sFile.getString(encrypt(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public long getLong(String key, long defaultValue) {
        final String encryptedValue =
                sFile.getString(encrypt(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        final String encryptedValue =
                sFile.getString(encrypt(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        final String encryptedValue =
                sFile.getString(encrypt(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public boolean contains(String key) {
        return sFile.contains(encrypt(key));
    }

    @Override
    public Editor edit() {
        return new Editor();
    }

    /**
     * Wrapper for Android's {@link SharedPreferences.Editor}.
     * <p/>
     * Used for modifying values in a {@link SecurePreferences} object. All changes you make in an
     * editor are batched, and not copied back to the original {@link SecurePreferences} until you
     * call {@link #commit()} or {@link #apply()}.
     */
    public class Editor implements SharedPreferences.Editor {
        private SharedPreferences.Editor mEditor;

        /**
         * Constructor.
         */
        private Editor() {
            mEditor = sFile.edit();
        }

        @Override
        public SharedPreferences.Editor putString(String key, String value) {
            mEditor.putString(encrypt(key), encrypt(value));
            return this;
        }

		@Override
		public SharedPreferences.Editor putStringSet(String key, Set<String> values)
		{
			Set<String> crypted=new HashSet<String>();
			for(String value:values){
				crypted.add(encrypt(value));
			}
			mEditor.putStringSet(encrypt(key),crypted);
			return this;
		}

		@Override
        public SharedPreferences.Editor putInt(String key, int value) {
            mEditor.putString(encrypt(key),
                              encrypt(Integer.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor putLong(String key, long value) {
            mEditor.putString(encrypt(key),
                              encrypt(Long.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor putFloat(String key, float value) {
            mEditor.putString(encrypt(key),
                              encrypt(Float.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor putBoolean(String key, boolean value) {
            mEditor.putString(encrypt(key),
                              encrypt(Boolean.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor remove(String key) {
            mEditor.remove(encrypt(key));
            return this;
        }

        @Override
        public SharedPreferences.Editor clear() {
            mEditor.clear();
            return this;
        }

        @Override
        public boolean commit() {
            return mEditor.commit();
        }

        @Override
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        public void apply() {
            mEditor.apply();
        }
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        sFile.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        sFile.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
