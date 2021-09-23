package com.example.buybooksonline;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.grpc.Context;

public class HelperAdapter extends RecyclerView.Adapter {
    List<model> modelList;

    public HelperAdapter(List<com.example.buybooksonline.model> model) {
        this.modelList = model;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
    ViewHolderClass viewHolderClass = new ViewHolderClass(view);

        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass = (ViewHolderClass) holder;
        model model = modelList.get(position);
        viewHolderClass.bookname.setText(""+model.getBook());
        viewHolderClass.pages.setText("Pages " + model.getPages());
        viewHolderClass.price.setText(model.getPrice()+"PKR");
        viewHolderClass.qty.setText("in stock " +model.getQuantity());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(model.getKey()+".jpg");
        try{
            final File localFile=File.createTempFile("profile","jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                    viewHolderClass.book.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }catch (IOException e){

            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
    public class ViewHolderClass extends RecyclerView.ViewHolder{
        ImageView book;
        TextView bookname,pages,price,qty;
        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    Intent i = new Intent(v.getContext(),OrderBook.class);
                    model m = modelList.get(getAdapterPosition());
                    i.putExtra("book_name",m.getBook());
                    i.putExtra("pages",m.getPages());
                    i.putExtra("book_price",m.getPrice());
                    i.putExtra("quantity",m.getQuantity());
                    i.putExtra("key",m.getKey());
                    v.getContext().startActivity(i);
                }
            });
            book=itemView.findViewById(R.id.book_image);
            bookname=itemView.findViewById(R.id.bookname);
            pages=itemView.findViewById(R.id.bookpages);
            price=itemView.findViewById(R.id.bookprice);
            qty=itemView.findViewById(R.id.remqty);
        }
    }
}
