package com.github.phuctranba.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.phuctranba.core.item.EnumRecipeType;
import com.github.phuctranba.core.item.EnumStorage;
import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.item.ItemUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FireBaseUtil {
    static FirebaseAuth mauth = FirebaseAuth.getInstance();
    static StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public static void createOrUpdateRecipe(ItemRecipe recipe) {
        if (recipe.getRecipeImage() != null) {

            final StorageReference ref = storageReference.child("images/recipes/" + recipe.getRecipeId());

            ref.putFile(Uri.fromFile(new File(recipe.getRecipeImage()))).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                                    .setValue(recipe).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    createOrUpdateRecipePending(recipe);
                                }
                            });
                        }
                    });
                }
            });
        } else {
            FirebaseDatabase.getInstance().getReference("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("myRecipes")
                    .child(recipe.getRecipeId())
                    .setValue(recipe).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    createOrUpdateRecipePending(recipe);
                }
            });
        }
    }

    public static void createOrUpdateRecipePending(ItemRecipe recipe) {
        if (recipe.getRecipeStorage().equals(EnumStorage.PERSONAL))
            return;

        FirebaseDatabase.getInstance().getReference("pendings")
                .child(recipe.getRecipeId())
                .setValue(recipe);

        if (recipe.getRecipeStorage().equals(EnumStorage.PUBLISHED))
            createOrUpdateRecipePublish(recipe);
        else
            removeRecipePublish(recipe);
    }

    public static void createOrUpdateRecipePublish(ItemRecipe recipe) {
        FirebaseDatabase.getInstance().getReference("recipes")
                .child(recipe.getRecipeId())
                .setValue(recipe);
    }

    public static void removeRecipePublish(ItemRecipe recipe) {
        FirebaseDatabase.getInstance().getReference("recipes")
                .child(recipe.getRecipeId())
                .removeValue();
    }

    public static List<ItemRecipe> getAllPendingRecipe() {

        List<ItemRecipe> recipes = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("pendings")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        ItemRecipe recipe = snapshot.getValue(ItemRecipe.class);
                        recipes.add(recipe);
                        Log.d("zippp", "onChildAdded: " + recipe.getRecipeId());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        ItemRecipe recipe = snapshot.getValue(ItemRecipe.class);
                        recipes.add(recipe);
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        ItemRecipe recipe = snapshot.getValue(ItemRecipe.class);
                        recipes.add(recipe);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return recipes;
    }

    public static boolean ResetPassword(Context context, String email) {
        final boolean[] result = {false};

        mauth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            result[0] = true;
                        } else {
                            result[0] = false;
                        }
                    }
                });
        return result[0];
    }


}
