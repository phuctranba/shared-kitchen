package com.github.phuctranba.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.item.ItemUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class FireBaseUtil {
    static FirebaseAuth mauth = FirebaseAuth.getInstance();
    static StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public static boolean createOrUpdateRecipe(Context context, ItemRecipe recipe) {
        final boolean[] success = {false};

        if(recipe.getRecipeImage()!=null){

            final StorageReference ref = storageReference.child("images/recipes/" + recipe.getRecipeId());

            ref.putFile(Uri.parse(recipe.getRecipeImage())).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    success[0] = false;
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            recipe.setRecipeImage(uri.toString());
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("myRecipes")
                                    .child(recipe.getRecipeId())
                                    .setValue(recipe).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    success[0] = task.isSuccessful();
                                }
                            });
                        }
                    });
                }
            });
        }else {

            FirebaseDatabase.getInstance().getReference("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("myRecipes")
                    .child(recipe.getRecipeId())
                    .setValue(recipe).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    success[0] = task.isSuccessful();
                }
            });
        }



        return success[0];
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
