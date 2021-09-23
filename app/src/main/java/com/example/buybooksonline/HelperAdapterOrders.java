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

public class HelperAdapterOrders extends RecyclerView.Adapter {
    List<Model_Order> modelList;

    public HelperAdapterOrders(List<com.example.buybooksonline.Model_Order> model) {
        this.modelList = model;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list,parent,false);
        HelperAdapterOrders.ViewHolderClass viewHolderClass = new HelperAdapterOrders.ViewHolderClass(view);

        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HelperAdapterOrders.ViewHolderClass viewHolderClass = (HelperAdapterOrders.ViewHolderClass) holder;
        Model_Order model = modelList.get(position);
        viewHolderClass.order_id.setText("Order id: "+model.getOrder_id());
        viewHolderClass.order_payment.setText( model.getBill_amount()+"PKR");
        viewHolderClass.qty.setText("Quantity " +model.getQuantity());
        viewHolderClass.time.setText("OrderTime: "+ model.getOrder_time());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(model.getBook_id()+".jpg");
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
        TextView order_id,order_progress,time,order_payment,qty;
        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    Intent i = new Intent(v.getContext(),OrderBook.class);
                    Model_Order m = modelList.get(getAdapterPosition());
                    /*i.putExtra("book_name",m.());
                    i.putExtra("pages",m.getPages());
                    i.putExtra("book_price",m.getPrice());
                    i.putExtra("quantity",m.getQuantity());
                    i.putExtra("key",m.getKey());
                    v.getContext().startActivity(i);*/
                }
            });
            book=itemView.findViewById(R.id.book_image);
            order_id=itemView.findViewById(R.id.order_id);
            order_payment=itemView.findViewById(R.id.order_payment);
            order_progress=itemView.findViewById(R.id.order_progress);
            time=itemView.findViewById(R.id.time);
            qty=itemView.findViewById(R.id.qty);
        }
    }
}

