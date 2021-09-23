package com.example.buybooksonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {

    private TabLayout tLayOut;
    private TabItem ti1,ti2;
    private ViewPager vpager;
    private PageAdapter pa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        tLayOut = findViewById(R.id.tab_layout);

        ti1 = findViewById(R.id.login_user);
        ti2 = findViewById(R.id.login_admin);


        vpager = findViewById(R.id.view_pager);



        pa= new PageAdapter(getSupportFragmentManager(), tLayOut.getTabCount());
        vpager.setAdapter(pa);
        tLayOut.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0||tab.getPosition()==1)
                    pa.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(tab.getPosition()==0||tab.getPosition()==1)
                    pa.notifyDataSetChanged();
            }
        });//end of tablayout listener
        vpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tLayOut));

    }//end of onCreate
}