package com.example.cookit.Model;

import com.example.cookit.Recipe;

public interface FirebaseChildEventListener {
    void onChildAdded(Recipe r);
    void onChildRemoved(Recipe r);
    void OnChildChanged(Recipe r);
}
