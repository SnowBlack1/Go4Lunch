package com.aureliev.go4lunch.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public interface UserFirebaseInterface {

    CollectionReference getCollectionUser();

    Query getListUsers();

    Task<Void> createUser(String uid, String email, String username, String urlPicture);

    Task<DocumentSnapshot> getUser(String uid);
// +3 variables à faire p/ restaurant ( model User)
}
