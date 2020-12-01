package com.aureliev.go4lunch;



import com.aureliev.go4lunch.repositories.UserFirebaseRepository;

public class ViewModel extends androidx.lifecycle.ViewModel {
    private UserFirebaseRepository mUserFirebaseRepository;

    public ViewModel(UserFirebaseRepository userFirebaseRepository){
        this.mUserFirebaseRepository = userFirebaseRepository;
    }
}
