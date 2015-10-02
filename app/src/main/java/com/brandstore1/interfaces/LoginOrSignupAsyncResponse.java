package com.brandstore1.interfaces;

/**
 * Created by I076324 on 9/16/2015.
 */
public interface LoginOrSignupAsyncResponse {
    // Login methods
    public void updateSuggestionInSQLite();
    public void goToMainActivityScreen();
    public void updateInstanceID();

    // Signup methods
    public void onEmailAlreadyExists();
}
