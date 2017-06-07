package com.example.sasuke.dailysuvichar.newnewactivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.activity.AboutActivity;
import com.example.sasuke.dailysuvichar.view.adapter.RVGuruAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GuruActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_clear_black_24dp);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_guru, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_sort:{
                showPopup();
                break;
            }
            case R.id.about:{
                startActivity(new Intent(this, AboutActivity.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Sorting Option:")
                .setItems(new CharSequence[]{"Followers", "Astrology Gurus", "Yoga Gurus", "Pandits",
                        "Motivation Gurus", "Ayurveda Gurus", "Show All"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which) {
                            case 0: {
                                //Filter By Followers
                                break;
                            }
                            case 1: {
                                //Astrology Gurus1
                                break;
                            }
                            case 2: {
                                //Yoga Gurus2
                                break;
                            }
                            case 3: {
                                //Pandits Gurus3
                                break;
                            }
                            case 4: {
                                //Motivation Gurus4
                                break;
                            }
                            case 5: {
                                //Ayurveda Gurus5
                                break;
                            }
                            case 6: {
                                //SHOW ALL
                            }
                        }
                    }
                });
        builder.show();
    }
}
