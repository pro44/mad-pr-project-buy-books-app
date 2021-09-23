package com.example.buybooksonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class OrderBook extends AppCompatActivity {
ImageView iv,ivplus,ivminus;
TextView bookname,price,pages,qty,qty_pick,pricetag;
Button add_to_cart,order;
FirebaseAuth firebaseAuth;
String userId;
String order_id;
int amount;
int qty_item;
private FirebaseDatabase mDatabase;
private DatabaseReference mGetReference;
    private FirebaseDatabase mDatabase_book;
    private DatabaseReference mGetReference_book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i= getIntent();
        String getkey=i.getStringExtra("key");
        String getbook_name = i.getStringExtra("book_name");
        String getpages=i.getStringExtra("pages");
        String getprice = i.getStringExtra("book_price");
        String getqty = i.getStringExtra("quantity");

        firebaseAuth=FirebaseAuth.getInstance();
        userId=firebaseAuth.getUid();

        mDatabase = FirebaseDatabase.getInstance();
        mGetReference= mDatabase.getReference();

        mDatabase_book = FirebaseDatabase.getInstance();
        mGetReference_book = mDatabase.getReference();

        setContentView(R.layout.activity_order_book);
        iv=findViewById(R.id.book_image_order);
        ivminus=findViewById(R.id.image_minus);
        ivplus = findViewById(R.id.image_plus);
        qty_pick=findViewById(R.id.quantity_picker);
        pricetag=findViewById(R.id.price_tag);
        ivminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty_item = Integer.parseInt(qty_pick.getText().toString());
                if(qty_item>1)qty_item-=1;
                else qty_item=qty_item;
                amount=Integer.parseInt(getprice)*qty_item;
                qty_pick.setText(qty_item+"");
                pricetag.setText("Total " +amount);
            }
        });

        ivplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty_item = Integer.parseInt(qty_pick.getText().toString());
                qty_item+=1;
                amount=Integer.parseInt(getprice)*qty_item;
                qty_pick.setText(qty_item+"");
                pricetag.setText("Total " +amount);

            }
        });

        mDatabase = FirebaseDatabase.getInstance();
        mGetReference= mDatabase.getReference();
        add_to_cart = findViewById(R.id.add_to_cart);

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OrderBook.this,"Add to cart",Toast.LENGTH_LONG).show();
            }
        });

        order=findViewById(R.id.order_button);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount==0){
                    Toast.makeText(OrderBook.this,"Please Select the quantity",Toast.LENGTH_LONG).show();
                  return;
                }
                Calendar instance = Calendar.getInstance();
                int hour = instance.get(Calendar.HOUR);
                int minute = instance.get(Calendar.MINUTE);
                int second = instance.get(Calendar.SECOND);

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c)+" " + hour+":"+minute +":"+second;

                order_id=FirebaseDatabase.getInstance().getReference().push().getKey();
                HashMap<String,Object> map = new HashMap<>();
                map.put("order_id",order_id);
                map.put("book_id",getkey);
                map.put("quantity",qty_item+"");
                map.put("bill_amount",amount+"");
                map.put("order_time",formattedDate);
                mGetReference.child(userId+"_orders").child(order_id).updateChildren(map);
                mGetReference_book.child("Model").child(getkey).child("quantity").setValue(Integer.parseInt(getqty)-qty_item+"");

            }
        });
        bookname=findViewById(R.id.book_name_order);
        price=findViewById(R.id.book_price_order);
        pages=findViewById(R.id.bookpages_order);
        qty=findViewById(R.id.quantity_order);



        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(getkey+".jpg");
        try{
            final File localFile=File.createTempFile("profile","jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                    iv.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }catch (IOException e){

            e.printStackTrace();
        }
        bookname.setText(getbook_name);
        price.setText(getprice+" PKR");
        pages.setText("Pages: "+getpages);
        qty.setText("Remaining Quantity"+getqty);


    }
}