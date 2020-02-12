package com.example.yuanjiayi;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SettingsActivity extends AppCompatActivity {
    RecyclerView recyclera;
    RecyclerView.LayoutManager lma;
    CategoryAdapter adaptera;
    RecyclerView recyclerb;
    RecyclerView.LayoutManager lmb;
    CategoryAdapter adapterb;
    public Boolean cateBool[] = new Boolean[10];
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mToolbar = findViewById(R.id.settingstoolbar);
        mToolbar.setTitle("偏好设置");
        setSupportActionBar(mToolbar);

        Intent intent = getIntent();
        for(int i = 0; i < 10; i++){
            cateBool[i] = intent.getBooleanExtra(((Integer) i).toString(), true);
        }

        adaptera = new CategoryAdapter(this, true);
        adapterb = new CategoryAdapter(this, false);
        recyclera = findViewById(R.id.catrea);
        recyclerb = findViewById(R.id.catreb);
        lma = new GridLayoutManager(this, 1);
        lmb = new GridLayoutManager(this, 1);
        recyclera.setLayoutManager(lma);
        recyclerb.setLayoutManager(lmb);
        recyclera.setAdapter(adaptera);
        recyclerb.setAdapter(adapterb);
        recyclera.setItemAnimator(new DefaultItemAnimator());
        recyclerb.setItemAnimator(new DefaultItemAnimator());

        ret();
    }

    public void ret(){
        Intent intent = new Intent();
        for(int i = 0; i < 10; i++) {
            intent.putExtra(((Integer) i).toString(), cateBool[i]);
        }
        this.setResult(2, intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.okay){
            onBackPressed();
        }
        return true;
    }
}
