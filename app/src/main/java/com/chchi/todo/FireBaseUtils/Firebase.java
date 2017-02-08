package com.chchi.todo.FireBaseUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by chchi on 1/25/17.
 */

public class Firebase {

    private static FirebaseDatabase mDatabase;
    private static FirebaseAuth mFirebaseAuth;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
    public static FirebaseAuth getFirebaseAuth(){
        if (mFirebaseAuth == null){
            mFirebaseAuth = FirebaseAuth.getInstance();
        }
        return mFirebaseAuth;
    }

}