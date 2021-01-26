package com.github.phuctranba.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.phuctranba.core.item.ItemUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FireBaseUtil {
    static FirebaseAuth mauth = FirebaseAuth.getInstance();

    public static boolean Signin(Context context, String email, String password) {
        final boolean[] result = {false};

        mauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseDatabase.getInstance().getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ItemUser user = snapshot.getValue(ItemUser.class);

                                    MySharedPreferences.setPrefUser(context, user);

                                    result[0] = true;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    System.out.println("The read failed: " + error.getCode());
                                }
                            });
                } else {
                    result[0] = false;
                }
            }
        });
        return result[0];
    }

    public static boolean Signup(Context context, String email, String password, String name) {
        final boolean[] result = {false};

        mauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final ItemUser user = new ItemUser(name,false, email);

                    FirebaseDatabase.getInstance().getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                MySharedPreferences.setPrefUser(context,user);

                                result[0]=true;
                            } else {
                                result[0]=false;
                            }
                        }
                    });
                }
            }
        });
        return result[0];
    }

    public static boolean ResetPassword(Context context, String email) {
        final boolean[] result = {false};

        mauth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            result[0] = true;
                        }else {
                            result[0] = false;
                        }
                    }
                });
        return result[0];
    }



}
