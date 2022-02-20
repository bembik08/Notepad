package ru.geekbrains.notepad.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.vk.api.sdk.auth.VKAccessToken;

import ru.geekbrains.notepad.MainActivity;
import ru.geekbrains.notepad.R;
import ru.geekbrains.notepad.data.Navigation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int RC_SIGN_IN = 40404;
    private static final String TAG = "Google_auth";
    private VKAccessToken access_token;
    private Navigation navigation;
    private GoogleSignInClient googleSignInClient;

    private SignInButton signInButton;
    private TextView emailView;
    private MaterialButton continue_;
    private MaterialButton signOutBtn;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment StartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartFragment newInstance() {
        return new StartFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        navigation = activity.getNavigation();
    }

    @Override
    public void onDetach() {
        navigation = null;
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        initGoogleSign();
        initView(view);
        enableSign();
        return view;
    }

    private void initGoogleSign() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                      .requestEmail()
                                      .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
    }

    private void initView(View view) {
        signInButton = view.findViewById(R.id.sign_in_button);
        MaterialButton vkSignInButton = view.findViewById(R.id.vk_sign_in_btn);
        vkSignInButton.setOnClickListener(view13 -> vkSignIn());
        signInButton.setOnClickListener(view1 -> signIn());
        emailView = view.findViewById(R.id.email);
        continue_ = view.findViewById(R.id.continue_);
        continue_.setOnClickListener(view12 -> navigation.addFragment(ContentNotesFragment.newInstance(), false));
        signOutBtn = view.findViewById(R.id.sign_out);
        signOutBtn.setOnClickListener(view14 -> signOut());
    }

    private void vkSignIn() {

    }

    private void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(task -> {
                    updateUi("");
                    enableSign();
                });
    }

    private void enableSign() {
        signInButton.setEnabled(true);
        continue_.setEnabled(false);
        signOutBtn.setEnabled(false);
    }

    private void signIn() {
        Intent intentSignIn  = googleSignInClient.getSignInIntent();
        startActivityForResult(intentSignIn, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            disableSignIn();
            updateUi(account.getEmail());
        } catch (ApiException e){
            Log.w(TAG, "sign in result failed code = " + e);

        }

    }

    private void disableSignIn() {
        signInButton.setEnabled(false);
        continue_.setEnabled(true);
        signOutBtn.setEnabled(true);
    }

    private void updateUi(String email) {
        emailView.setText(email);
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (account != null){
            disableSignIn();
            updateUi("");
        }
    }
}