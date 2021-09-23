package com.example.buybooksonline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.w3c.dom.Text;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBookFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    EditText book_name,books_qty,book_price,book_pages;
    Button upload_book;
    ImageView iv;
    Uri url=null;
    String key;
    StorageReference storageReference;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mGetReference;
    private static final int GALLERY_REQUEST=1;
    ProgressDialog progressDialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddBookFragment newInstance(String param1, String param2) {
        AddBookFragment fragment = new AddBookFragment();
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
        view = inflater.inflate(R.layout.fragment_add_book, container, false);
        progressDialog =  new ProgressDialog(getActivity());
        progressDialog.setMessage("Uploading book...");
        progressDialog.setCancelable(false);
        book_name=view.findViewById(R.id.book_name);
        book_price=view.findViewById(R.id.book_price);
        books_qty=view.findViewById(R.id.quantity);
        book_pages=view.findViewById(R.id.no_of_pages);
        iv=view.findViewById(R.id.upload_book_image);
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance();
         mGetReference= mDatabase.getReference();
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*for some fixed size image
                * CropImage.activity()
               .setAspectRatio(1,1)    //for 1:1 ratio

               .start(MainActivity.this);*/
                CropImage.activity().start(getContext(),AddBookFragment.this);


            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();

        upload_book=view.findViewById(R.id.upload_book);
        upload_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String price=book_price.getText().toString().trim();
                String qty=books_qty.getText().toString().trim();
                String pages=book_pages.getText().toString().trim();
                String bookname=book_name.getText().toString();
                /*if(TextUtils.isEmpty(book_price.getText().toString()))price=0.0;
                else price = Double.parseDouble(book_price.getText().toString());
                if(TextUtils.isEmpty(books_qty.getText().toString()))qty=0;
                else qty = Integer.parseInt(books_qty.getText().toString());
                if(TextUtils.isEmpty(book_pages.getText().toString()))pages=0;
                else pages = Integer.parseInt(book_pages.getText().toString());*/
                if(TextUtils.isEmpty(bookname)||TextUtils.isEmpty(book_price.getText().toString().trim())
                        ||TextUtils.isEmpty(books_qty.getText().toString().trim())||TextUtils.isEmpty(book_pages.getText().toString().trim())
                        ||url==null){
                if(url==null) Toast.makeText(getContext(),"Please select the image of book",Toast.LENGTH_LONG).show();
                if(TextUtils.isEmpty(bookname)){
                    book_name.setError("insert book name");
                }
                if(book_price.getText().toString().trim().length()<=0){
                    book_price.setError("Insert price");
                }
                if(books_qty.getText().toString().trim().length()<=0){
                    books_qty.setError("Insert quantity");
                }
                if(book_pages.getText().toString().trim().length()<=0){
                    book_pages.setError("Insert no of pages");
                }
                return;
                }
                else {progressDialog.show();
                    key =FirebaseDatabase.getInstance().getReference().push().getKey();

                    HashMap<String,Object> map=new HashMap<>();
                    map.put("key",key);
                    map.put("seller",userEmail);
                    map.put("book",bookname);
                    map.put("price",price);
                    map.put("quantity",qty);
                    map.put("pages",pages);
                    model mod = new model(key,bookname,userEmail,price,qty,pages);
                    //mDatabase.child("Books").child(key).setValue(mod);
                    mGetReference.child("Model").child(key).updateChildren(map);
                    uploadImageOnFirebase(url);
                    book_name.setText("");
                    book_price.setText("");
                    book_pages.setText("");
                    books_qty.setText("");
                }
            }
        });
/*        mGetReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();

                    for (String key : dataMap.keySet()){

                        Object data = dataMap.get(key);

                        try{
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;

                            Model model = new Model((String) userData.get("key"), (String) userData.get("book"),
                                    (String)userData.get("seller"),(double)userData.get("price"),(int)userData.get("quantity"),(int)userData.get("pages"));


                        }catch (ClassCastException cce){

// If the object can’t be casted into HashMap, it means that it is of type String.

                            try{

                                String mString = String.valueOf(dataMap.get(key));


                            }catch (ClassCastException cce2){

                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        return view;
    }
    private void uploadImageOnFirebase(Uri url){
            StorageReference fileReference = storageReference.child(key + ".jpg");
            fileReference.putFile(url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "book uploaded", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "image upload failed", Toast.LENGTH_LONG).show();
                }
            });
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                url = result.getUri();
                Picasso.get().load(url).into(iv);

            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception e = result.getError();
                Toast.makeText(getContext(),"Error"+e,Toast.LENGTH_LONG).show();
            }
        }


    }//end of onActivity Result method
}