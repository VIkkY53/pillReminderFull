package android.example.idp;

import android.Manifest;
import android.app.ActionBar;
import android.app.StatusBarManager;
import android.content.pm.PackageManager;
import android.database.DatabaseErrorHandler;
import android.example.idp.ui.dashboard.DashboardFragment;
import android.example.idp.ui.home.HomeFragment;
import android.example.idp.ui.notifications.NotificationsFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<ListItemObject> MainList;
    androidx.appcompat.widget.Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainList=new ArrayList<ListItemObject>();
        toolbar=findViewById(R.id.toolbar_dashboard);
        setSupportActionBar(toolbar);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener);
        toolbar.setTitle("Upcoming Reminders");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager
                .PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},100);
        }

    }
    private  BottomNavigationView.OnNavigationItemSelectedListener navListener= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment=null;
            switch (menuItem.getItemId()){
                case R.id.navigation_home:
                    fragment=new HomeFragment();
                    toolbar.setTitle("Upcoming Reminders");
                    break;
                case R.id.navigation_notifications:
                    toolbar.setTitle("Settings");
                    fragment=new NotificationsFragment();
                    break;
                case R.id.navigation_dashboard:
                    toolbar.setTitle("My List");
                    fragment=new DashboardFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            return true;
        }

    };

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}