package com.example.buybooksonline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
NavigationView nav;
ActionBarDrawerToggle toggle;
DrawerLayout drawerLayout;
Toolbar toolbar;
boolean isAdmin=true;
FirebaseAuth fAuth;
FirebaseFirestore fStore;
    MenuItem item_addbook;
    TextView header_name,header_email;
    View header;
    String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_layout);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        nav=findViewById(R.id.navigation_view);
        Menu menuNav = nav.getMenu();
        drawerLayout=findViewById(R.id.drawer);
        item_addbook = menuNav.findItem(R.id.menu_upload_book);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav.setNavigationItemSelectedListener(this);
        header=nav.getHeaderView(0);
        header_name=header.findViewById(R.id.nave_header_name);
        header_email=header.findViewById(R.id.nave_header_email);
        //nav_header_image=header.findViewById(R.id.nav_header_image_icon);
        uId=fAuth.getCurrentUser().getUid();
        DocumentReference documentReference=fStore.collection("users").document(uId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                header_email.setText(value.getString("eMail"));
                isAdmin=(boolean)value.getBoolean("is_admin");
                header_name.setText(value.getString("uName"));
            }
        });
        MenuItem nav_item2 = menuNav.findItem(R.id.menu_upload_book);
        if(isAdmin)
            nav_item2.setEnabled(true);

        else if(!isAdmin) nav_item2.setEnabled(false);
        /*mStorageReference = FirebaseStorage.getInstance().getReference().child(uId+".jpg");
        try{
            final File localFile=File.createTempFile("profile","jpg");
            mStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                    nav_header_image.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }catch (IOException e){

            e.printStackTrace();
        }
*/

    }
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_home:
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.flContent,new HomeFragment()
                ).commit();
                break;
            case R.id.menu_upload_book:
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.flContent,new AddBookFragment()
                ).commit();
                break;
            case R.id.menu_my_orders:
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.flContent,new ShowOrderFragment()
                ).commit();
                break;
            case R.id.menu_logout:
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}