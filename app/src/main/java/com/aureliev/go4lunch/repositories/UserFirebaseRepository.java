package com.aureliev.go4lunch.repositories;

import com.aureliev.go4lunch.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class UserFirebaseRepository implements UserFirebaseInterface {


 @Override
 public CollectionReference getCollectionUser() {
  return FirebaseFirestore.getInstance().collection("users");
 }

 @Override
 public Query getListUsers() {
  return null;
 }

 @Override
 public Task<Void> createUser(String uid, String email, String username, String urlPicture) {
  User user = new User(email,username,urlPicture);
  return getCollectionUser().document(uid).set(user);
 }

 @Override
 public Task<DocumentSnapshot> getUser(String uid) {
  return getCollectionUser().document(uid).get();
 }
}
