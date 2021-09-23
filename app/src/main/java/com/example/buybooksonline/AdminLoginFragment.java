package com.example.buybooksonline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminLoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminLoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText email,password;
    String uId;
    ProgressBar progressbar;
    Button login;
    View view;
    FirebaseAuth fAuth;
    TextView textView;
    FirebaseFirestore fStore;
    boolean is_admin=false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminLoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminLoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminLoginFragment newInstance(String param1, String param2) {
        AdminLoginFragment fragment = new AdminLoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_login, container, false);
        email=view.findViewById(R.id.admin_email);
        password=view.findViewById(R.id.admin_password);
        login=view.findViewById(R.id.admin_login_button);
        progressbar=view.findViewById(R.id.pb_admin_login);
        textView=view.findViewById(R.id.admin_goToRegister);

       textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),RegisterActivity.class));
                getActivity().finish();
            }
        });
        fAuth=FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String eMail=email.getText().toString().trim();
                String pass=password.getText().toString().trim();
                if(TextUtils.isEmpty(eMail)){
                    email.setError("email is required");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    password.setError("Password is required");
                    return;
                }
                progressbar.setVisibility(View.VISIBLE);
                fStore=FirebaseFirestore.getInstance();
                fAuth.signInWithEmailAndPassword(eMail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            uId=fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=fStore.collection("users").document(uId);
                            documentReference.addSnapshotListener((Activity) getContext(), new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                    is_admin=value.getBoolean("is_admin");


                                }
                            });
                            if(is_admin){
                                Toast.makeText(getContext(),"Login Successful " +is_admin,Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getContext(),MainActivity2.class));
                                getActivity().finish();}
                            else {Toast.makeText(getContext(),"You are not admin",Toast.LENGTH_LONG).show();
                                FirebaseAuth.getInstance().signOut();}
                            progressbar.setVisibility(View.INVISIBLE);
                        }else{
                            Toast.makeText(getContext(),"Error ! "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });//end of methods


        return view;
    }
}