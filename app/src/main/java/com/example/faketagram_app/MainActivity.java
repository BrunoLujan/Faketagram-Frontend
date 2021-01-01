package com.example.faketagram_app;

import android.content.SharedPreferences;
import android.icu.lang.UScript;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.faketagram_app.model.Users;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView txtNamesNavDrawerMA, txtUserNameNavDrawerMA;
    private ImageView ivNavDrawerMA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        txtNamesNavDrawerMA = (TextView) hView.findViewById(R.id.txtNamesNavDrawer);
        txtUserNameNavDrawerMA = (TextView) hView.findViewById(R.id.txtUserNameNavDrawer);
        ivNavDrawerMA = (ImageView) hView.findViewById(R.id.ivImageNavDrawer);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_my_profile, R.id.nav_my_favorites, R.id.nav_search, R.id.nav_following, R.id.nav_follower, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        getDates();
    }

    @Override
    public void onBackPressed() { }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void getDates(){
        Call<Users> userResponseCall = Constant.CONNECTION.getLoggedUser(Constant.AUTHTOKEN);
        if (Constant.AUTHTOKEN != null){
            userResponseCall.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(Call<Users> call, Response<Users> response) {
                    if (response.isSuccessful()){
                        if (response.body().getEmail() != null){
                            Constant.LOGGEDUSER = response.body();
                            if (Constant.LOGGEDUSER.getImage_storage_path() != null){
                                Picasso.get().load(Constant.PROFILEIMAGE + Constant.LOGGEDUSER.getImage_storage_path()).fit().into(ivNavDrawerMA);
                            }
                            txtNamesNavDrawerMA.setText(Constant.LOGGEDUSER.getName() + " " + Constant.LOGGEDUSER.getLastname());
                            txtUserNameNavDrawerMA.setText("@" + Constant.LOGGEDUSER.getUsername());
                        } else {
                            Log.d("BODY", "NEL");
                        }
                    } else {
                        Constant.Message(getApplicationContext(),Constant.AUTHTOKEN);
                    }
                }

                @Override
                public void onFailure(Call<Users> call, Throwable t) {
                    Constant.Message(getApplicationContext(), t.toString());
                    Log.d("ACÁ",t.toString());
                }
            });
        } else {
            Constant.Message(getApplicationContext(), "FUE NULO BRO:(");
        }
    }
}