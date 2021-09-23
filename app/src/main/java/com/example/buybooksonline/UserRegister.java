package com.example.buybooksonline;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserRegister#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserRegister extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view;
    DatePickerDialog.OnDateSetListener setListener;
    EditText username,email,password,date_of_birth,ph_num,address;
    ImageButton ibutton;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Button register_button;
    boolean is_admin=false;
    String userID;
    int d, m, y;
    TextView tv;
    ProgressBar pb;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserRegister() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserRegister.
     */
    // TODO: Rename and change types and number of parameters
    public static UserRegister newInstance(String param1, String param2) {
        UserRegister fragment = new UserRegister();
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
        view= inflater.inflate(R.layout.fragment_user_register, container, false);
        username=view.findViewById(R.id.username_R);
        email=view.findViewById(R.id.email_R);
        password=view.findViewById(R.id.password_R);
        date_of_birth=view.findViewById(R.id.d_o_b_R);
        ibutton=view.findViewById(R.id.date_selector);
        ph_num=view.findViewById(R.id.phone);
        address=view.findViewById(R.id.address);
        register_button=view.findViewById(R.id.user_registration_button);
        fAuth = FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        pb=view.findViewById(R.id.pb_user_register);
        tv=view.findViewById(R.id.go_to_login);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),LoginActivity.class));
                getActivity().finish();
            }
        });
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        date_of_birth.setEnabled(false);
        ibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month=month+1;
                        d=day;
                        m=month;
                        y=year;

                        String date=day+"/"+month+"/"+year;
                        date_of_birth.setText(date);
                    }
                },year,month,day );
                datePickerDialog.show();
            }
        });
        register_button.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                String uname=username.getText().toString().trim();
                String eMail=email.getText().toString().trim();
                String pass=password.getText().toString().trim();
                String add=address.getText().toString().trim();
                String phnumber = ph_num.getText().toString().trim();
                if(TextUtils.isEmpty(uname)){
                    username.setError("username is required");
                    return;
                }
                if(TextUtils.isEmpty(eMail)){
                    email.setError("email is required");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    password.setError("Password is required");
                    return;
                }
                if(password.length()<6){
                    password.setError("Password length is too short");
                    return;
                }
                if(TextUtils.isEmpty(add)){
                    address.setError("Address is required");
                    return;
                }
                if(TextUtils.isEmpty(phnumber)){
                    ph_num.setError("Contact is required");
                    return;
                }
                pb.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(eMail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(),"User Created",Toast.LENGTH_SHORT).show();
                            userID=fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user=new HashMap<>();
                            user.put("uName",uname);
                            user.put("eMail",eMail);
                            user.put("address",add);
                            user.put("ph",phnumber);
                            user.put("day_o_b",d);
                            user.put("month_o_b",m);
                            user.put("year_o_b",y);
                            user.put("is_admin", is_admin);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(),"Data is also saved" ,Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getContext(),MainActivity2.class));
                                    getActivity().finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(),"Data is not saved" +e.getMessage() ,Toast.LENGTH_LONG).show();
                                }
                            });
                            //Intent uploadImage = new Intent(getContext(),UploadImage.class);
                            //uploadImage.putExtra("userId",userID);
                            //startActivity(uploadImage);
                        }else{
                            Toast.makeText(getContext(),"Error !"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            pb.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });//end of methods


        return view;
    }
}